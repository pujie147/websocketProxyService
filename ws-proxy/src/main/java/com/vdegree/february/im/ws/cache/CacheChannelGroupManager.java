package com.vdegree.february.im.ws.cache;

import com.google.common.cache.RemovalCause;
import com.google.common.collect.Sets;
import com.vdegree.february.im.common.cache.RoomDataRedisManger;
import com.vdegree.february.im.common.cache.UserDataRedisManger;
import com.vdegree.february.im.common.constant.ChannelAttrConstant;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/19 13:36
 */
@Component
@Log4j2
public class CacheChannelGroupManager extends DefaultChannelGroup {
    private final EventExecutor executor;
    private UserCacheManager userCacheManager;
    private RoomCacheManager roomCacheManager;
    /** 缓存项最大数量 */
    @Value("${ws.service.connection-max}")
    private Long connectionMax;

    /** 缓存时间：秒 */
    @Value("${ws.service.idle-time}")
    private Long idelTime;

    @Autowired
    private UserDataRedisManger userDataRedisManger;
    @Autowired
    private RoomDataRedisManger roomDataRedisManger;




    public CacheChannelGroupManager(EventExecutor executor) {
        super(executor);
        this.executor = executor;
    }

    public CacheChannelGroupManager() {
        this(GlobalEventExecutor.INSTANCE);
    }

    @PostConstruct
    private void init() throws Exception {
        userCacheManager = new UserCacheManager(removalNotification ->  {
            removalNotification.getValue().close();
            if(RemovalCause.SIZE.equals(removalNotification.getCause())){
                log.error("删除用户 {} 数据 用户已下线 原因:{} 链接缓存超出范围",removalNotification.getKey(),removalNotification.getCause().name());
            }else{
                log.info("删除用户 {} 数据 用户已下线 原因:{}",removalNotification.getKey(),removalNotification.getCause().name());
            }
        },connectionMax.intValue(),idelTime);

        roomCacheManager = new RoomCacheManager(removalNotification ->  {
            if(RemovalCause.SIZE.equals(removalNotification.getCause())){
                log.error("删除用户 {} 数据 用户已下线 原因:{} 链接缓存超出范围",removalNotification.getKey(),removalNotification.getCause().name());
            }else{
                log.info("删除用户 {} 数据 用户已下线 原因:{}",removalNotification.getKey(),removalNotification.getCause().name());
            }
        },connectionMax.intValue(),idelTime);
    }

    /**
     * @Author DELL
     * @Date 20:22 2021/3/25
     * @Description 初始化 redis的用户有效时间 和 本地的缓存
     * @param: userId
     * @param: channel
     * @Return boolean
     * @Exception
     **/
    public boolean add(Long userId,Channel channel) {
        userDataRedisManger.buildNewRedisData(userId);
//        userDataRedisManger.generateRedisUserEffectiveTime(userId);
        userCacheManager.put(userId,channel);
        return super.add(channel);
    }

    /**
     * @Author DELL
     * @Date 20:22 2021/3/25
     * @Description 重写父类的方法可以在连接断开监听被调用
     * @param: o
     * @Return boolean
     * @Exception
     **/
    @Override
    public boolean remove(Object o) {
        Long userId = null;
        if (o instanceof ChannelId) {
            userId = find((ChannelId) o).attr(ChannelAttrConstant.USERID).get();
        } else if (o instanceof Channel) {
            userId = ((Channel) o).attr(ChannelAttrConstant.USERID).get();
        }
        this.deleteRoom(this.getRoomIdByUserId(userId));
        return super.remove(o);
    }

    /**
     * @Author DELL
     * @Date 17:52 2021/3/25
     * @Description 刷新本地缓存和redis缓存的ideltime
     * @param: userId
     * @Return void
     * @Exception
     **/
    public boolean refreshUser(Long userId){
        if(userCacheManager.refreshLocalCacheIdelTime(userId)) {
            if(userDataRedisManger.refreshRedisUserEffectiveTime(userId)) {
                return true;
            }
        }
        return false;
    }

    public boolean refreshRoom(Long userId){
        if(roomCacheManager.refreshLocalCacheIdelTime(userId)) {
            String roomId = roomCacheManager.getRoomIdByUserId(userId);
            if(roomDataRedisManger.refreshRedisRoomEffectiveTime(roomId)){
                return true;
            }
        }
        return false;
    }

    public String getRoomIdByUserId(Long userId){
        return roomCacheManager.getRoomIdByUserId(userId);
    }

    public void deleteRoom(String roomId){
        Long sendUserId = roomDataRedisManger.getSendUserId(roomId);
        roomCacheManager.remove(sendUserId);
    }

    public void deleteUser(Long userId){
        userCacheManager.remove(userId);
    }

    /**
     * @Author DELL
     * @Date 15:32 2021/3/25
     * @Description
        获取缓存值 通过userId查询 channel
     * @param: key
     * @Return java.util.Optional<io.netty.channel.Channel>
     * @Exception
     **/
    public Channel getChannelByUserId(Long userId) {
        return userCacheManager.getChannelByUserId(userId);
    }

    /**
     * @Author DELL
     * @Date 13:48 2021/3/27
     * @Description 是否包含userId
     * @param: userId
     * @Return java.lang.Boolean 
     * @Exception 
     **/
    public Boolean containsUserId(Long userId) {
        return userCacheManager.containsUserId(userId);
    }

    /**
     * @Author DELL
     * @Date 16:22 2021/3/25
     * @Description 发送所有用户
     * @param: message
     * @Return io.netty.channel.group.ChannelGroupFuture
     * @Exception
     **/
    public ChannelGroupFuture writeALLAndFlush(TextWebSocketFrame message) {
        return super.writeAndFlush(message);
    }

    /**
     * @Author DELL
     * @Date 16:22 2021/3/25
     * @Description 发送userids中包含的用户
     * @param: userIds
     * @param: message
     * @Return io.netty.channel.group.ChannelGroupFuture
     * @Exception
     **/
    public ChannelGroupFuture writeInUserIdsAndFlush(List<Long> userIds , TextWebSocketFrame message) {
        final ChannelGroupFuture future;
        Map<Channel, ChannelFuture> futures = new LinkedHashMap<Channel, ChannelFuture>((int) userCacheManager.size());
        userIds.forEach(userId->{
            try {
                Channel c = userCacheManager.getChannelByUserId(userId);
                if (c != null) {
                    futures.put(c, c.writeAndFlush(message));
                }
            }catch (Exception e){
                log.info("当前服务 消息push失败！！！ 用户{}  message {}",userId,message);
            }
        });
        future = new DefaultChannelGroupFuture(this, futures, this.executor);
        return future;
    }

    /**
     * @Author DELL
     * @Date 16:22 2021/3/25
     * @Description 发送userIds 中不包含的用户
     * @param: userIds
     * @param: message
     * @Return io.netty.channel.group.ChannelGroupFuture 
     * @Exception 
     **/
    public ChannelGroupFuture writeNoInUserIdsAndFlush(List<Long> userIds , TextWebSocketFrame message) {
        final ChannelGroupFuture future;
        Map<Channel, ChannelFuture> futures = new LinkedHashMap<Channel, ChannelFuture>((int) userCacheManager.size());
        HashSet<Long> userIdSet = Sets.newHashSet(userIds);
        userCacheManager.userIds().forEach(userId -> {
            if(!userIdSet.contains(userId)){//不包含的用户发送
                Channel c = userCacheManager.getChannelByUserId(userId);
                futures.put(c, c.writeAndFlush(message));
            }
        });
        future = new DefaultChannelGroupFuture(this, futures, this.executor);
        return future;
    }
}
