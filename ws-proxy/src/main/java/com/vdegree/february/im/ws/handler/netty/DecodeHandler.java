package com.vdegree.february.im.ws.handler.netty;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vdegree.february.im.api.ws.HeartBeatProto;
import com.vdegree.february.im.api.ws.ProtoContext;
import com.vdegree.february.im.common.constant.ChannelAttrConstant;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.ws.cache.CacheChannelGroupManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 解码 handler
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/15 15:50
 */
@Log4j2
@Component
@ChannelHandler.Sharable
public class DecodeHandler extends MessageToMessageDecoder<TextWebSocketFrame> {
    @Autowired
    private CacheChannelGroupManager cacheChannelGroupManager;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    private Gson gson;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 握手完成 进行初始化
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            cacheChannelGroupManager.add(ctx.channel().attr(ChannelAttrConstant.USERID).get(),ctx.channel());
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) throws Exception {
        ProtoContext protoContext = ProtoContext.buildContext(msg.text());
        if(IMCMD.REQUEST_HEARTBEAT.equals(protoContext.getBaseProto().getCmd())){
            HeartBeatProto heartBeatProto = gson.fromJson(msg.text(), HeartBeatProto.class);
            out.add(heartBeatProto);
        }else {
            out.add(protoContext);
        }
    }
}