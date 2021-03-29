package com.vdegree.february.im.ws.cache;

import com.google.common.collect.Sets;
import com.vdegree.february.im.common.cache.HeartBeatRedisManger;
import com.vdegree.february.im.common.constant.ChannelAttrConstant;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CacheChannelGroupManager extends DefaultChannelGroup {
    private final EventExecutor executor;
    private CacheManager cacheManager;
    @Autowired
    public HeartBeatRedisManger heartBeatRedisManger;

    public CacheChannelGroupManager(EventExecutor executor) {
        super(executor);
        this.executor = executor;
    }

    public CacheChannelGroupManager() {
        this(GlobalEventExecutor.INSTANCE);
    }

    @PostConstruct
    private void init() throws Exception {
        cacheManager = new CacheManager(removalNotification ->  {
            System.out.println("删除用户 "+removalNotification.getKey() +" 数据 用户已下线");
        });
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
        heartBeatRedisManger.generateRedisUserEffectiveTime(userId);
        cacheManager.put(userId,channel);
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
        if (o instanceof ChannelId) {
            cacheManager.remove(find((ChannelId) o).attr(ChannelAttrConstant.USERID).get());
        } else if (o instanceof Channel) {
            cacheManager.remove(((Channel) o).attr(ChannelAttrConstant.USERID).get());
        }
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
    public boolean refresh(Long userId){
        if(cacheManager.refreshLocalCacheIdelTime(userId)) {
            heartBeatRedisManger.refreshRedisUserAndRoomEffectiveTime(userId);
            return true;
        }
        return false;
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
        return cacheManager.getChannelByUserId(userId);
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
        return cacheManager.containsUserId(userId);
    }
//    /**
//     * @Author DELL
//     * @Date 15:33 2021/3/25
//     * @Description
//        通过channelId查询userId
//     * @param: channelId
//     * @Return java.lang.Long
//     * @Exception
//     **/
//    public Long getUserIdByChannelId(String channelId){
//        return cacheManager.getUserIdByChannelId(channelId);
//    }

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
        Map<Channel, ChannelFuture> futures = new LinkedHashMap<Channel, ChannelFuture>((int) cacheManager.size());
        userIds.forEach(userId->{
            Channel c = cacheManager.getChannelByUserId(userId);
            if(c!=null) {
                futures.put(c, c.writeAndFlush(message));
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
        Map<Channel, ChannelFuture> futures = new LinkedHashMap<Channel, ChannelFuture>((int) cacheManager.size());
        HashSet<Long> userIdSet = Sets.newHashSet(userIds);
        cacheManager.userIds().forEach(userId -> {
            if(!userIdSet.contains(userId)){//不包含的用户发送
                Channel c = cacheManager.getChannelByUserId(userId);
                futures.put(c, c.writeAndFlush(message));
            }
        });
        future = new DefaultChannelGroupFuture(this, futures, this.executor);
        return future;
    }
}
