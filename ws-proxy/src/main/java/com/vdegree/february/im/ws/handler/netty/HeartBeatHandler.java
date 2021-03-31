package com.vdegree.february.im.ws.handler.netty;



import com.vdegree.february.im.api.ws.HeartBeatProto;
import com.vdegree.february.im.api.ws.ResponseProto;
import com.vdegree.february.im.api.ws.WSProtoContext;
import com.vdegree.february.im.common.constant.ChannelAttrConstant;
import com.vdegree.february.im.common.constant.type.ErrorEnum;
import com.vdegree.february.im.ws.cache.CacheChannelGroupManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TODO
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

    @Autowired
    private WSProtoContext wsProtoContext;

//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        // 握手完成 进行初始化
//        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
//            cacheChannelGroupManager.add(ctx.channel().attr(ChannelAttrConstant.USERID).get(),ctx.channel());
//        }
//        super.userEventTriggered(ctx, evt);
//    }



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatProto msg)
            throws Exception {
        Long userId = ctx.channel().attr(ChannelAttrConstant.USERID).get();
        ResponseProto responseProto = msg.buildResponseProto();
        if(cacheChannelGroupManager.refreshUser(userId)) {
            log.debug("用户：{} 心跳成功",userId);
            ctx.channel().writeAndFlush(responseProto);
            return;
        }else{
            log.error("用户：{} 心跳失败 用户失效",userId);
            responseProto.setError(ErrorEnum.HEART_BEAT_ERROR);
            ctx.channel().writeAndFlush(responseProto).addListener(future -> {
                if(future.isSuccess()){
                    ctx.close();
                }
            });
        }
    }
}