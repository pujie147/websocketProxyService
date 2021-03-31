package com.vdegree.february.im.ws.handler.netty;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/15 15:50
 */

import com.google.gson.Gson;
import com.vdegree.february.im.api.ws.WSProtoContext;
import com.vdegree.february.im.common.constant.ChannelAttrConstant;
import com.vdegree.february.im.common.constant.ImServiceQueueConstant;
import com.vdegree.february.im.ws.cache.CacheChannelGroupManager;
import io.netty.channel.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 * @Description: 处理消息的handler
 * TextWebSocketFrame： 在netty中，是用于为websocket专门处理文本的对象，frame是消息的载体
 *  这里已经指定了类型 如果这里是Object 那么下面还需判断是不是TextWebSocketFrame类型
 */
@Log4j2
@Component
@ChannelHandler.Sharable
public class ChatHandler extends SimpleChannelInboundHandler<WSProtoContext> {
    @Autowired
    private CacheChannelGroupManager cacheChannelGroupManager;

    @Autowired
    private Gson gson;

    @Autowired
    RabbitTemplate rabbitTemplate;

//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        // 握手完成 进行初始化
//        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
//            cacheChannelGroupManager.add(ctx.channel().attr(ChannelAttrConstant.USERID).get(),ctx.channel());
//        }
//        super.userEventTriggered(ctx, evt);
//    }



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WSProtoContext msg)
            throws Exception {
        msg.getInternalProto().setWsProxyStartTime(System.currentTimeMillis());
        msg.getInternalProto().setSendUserId(ctx.channel().attr(ChannelAttrConstant.USERID).get());
        rabbitTemplate.convertAndSend(ImServiceQueueConstant.EXCHANGE_NAME,ImServiceQueueConstant.ROUTING_KEY, msg);
    }
}