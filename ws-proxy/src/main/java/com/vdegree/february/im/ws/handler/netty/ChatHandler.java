package com.vdegree.february.im.ws.handler.netty;

import com.vdegree.february.im.api.ws.ProtoContext;
import com.vdegree.february.im.common.constant.ChannelAttrConstant;
import com.vdegree.february.im.common.constant.ImServiceQueueConstant;
import io.netty.channel.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * netty 发送mq 给 imservice 消费队列
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/15 15:50
 */
@Log4j2
@Component
@ChannelHandler.Sharable
public class ChatHandler extends SimpleChannelInboundHandler<ProtoContext> {
    @Autowired
    RabbitTemplate rabbitTemplate;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtoContext msg)
            throws Exception {
        msg.getInternalProto().setWsProxyStartTime(System.currentTimeMillis());
        msg.getInternalProto().setSendUserId(ctx.channel().attr(ChannelAttrConstant.USERID).get());
        rabbitTemplate.convertAndSend(ImServiceQueueConstant.EXCHANGE_NAME,ImServiceQueueConstant.ROUTING_KEY, msg);
    }
}