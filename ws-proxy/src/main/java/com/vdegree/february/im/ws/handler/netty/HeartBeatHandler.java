package com.vdegree.february.im.ws.handler.netty;



import com.vdegree.february.im.api.ws.HeartBeatProto;
import com.vdegree.february.im.api.ws.ResponseProto;
import com.vdegree.february.im.api.ws.ProtoContext;
import com.vdegree.february.im.common.constant.ChannelAttrConstant;
import com.vdegree.february.im.common.constant.type.ErrorEnum;
import com.vdegree.february.im.common.constant.type.IsInRoomEnum;
import com.vdegree.february.im.ws.cache.CacheChannelGroupManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * ws proxy 独立处理 心跳
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/15 15:50
 */
@Log4j2
@Component
@ChannelHandler.Sharable
public class HeartBeatHandler extends SimpleChannelInboundHandler<HeartBeatProto> {
    @Autowired
    private CacheChannelGroupManager cacheChannelGroupManager;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatProto msg)
            throws Exception {
        Long userId = ctx.channel().attr(ChannelAttrConstant.USERID).get();
        ResponseProto responseProto = msg.buildResponseProto();
        if(cacheChannelGroupManager.refreshUser(userId)) {
            // 本地心跳有效
            log.debug("用户：{} 心跳成功",userId);
            if(msg.getMessage()!=null && msg.getMessage().getIsInRoom()!=null) {
                if (IsInRoomEnum.InRoom.compareTo(msg.getMessage().getIsInRoom()) == 0) {
                    if (!cacheChannelGroupManager.refreshRoom(userId)) {
                        responseProto.setError(ErrorEnum.ROOM_HEART_BEAT_ERROR);
                    }
                }
            }
            ctx.channel().writeAndFlush(responseProto);
            return;
        }else{
            // 本地心跳失效
            log.error("用户：{} 心跳失败 用户失效",userId);
            cacheChannelGroupManager.deleteUser(userId);
            String roomId = cacheChannelGroupManager.getRoomIdByUserId(userId);
            if(roomId!=null){
                cacheChannelGroupManager.deleteRoom(userId);
            }
            responseProto.setError(ErrorEnum.HEART_BEAT_ERROR);
            ctx.channel().writeAndFlush(responseProto).addListener(future -> {
                if(future.isSuccess()){
                    ctx.close();
                }
            });
        }
    }
}