package com.vdegree.february.im.ws.handler.netty;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/15 15:50
 */

import com.google.gson.Gson;
import com.vdegree.february.im.common.constant.ErrorEnum;
import com.vdegree.february.im.api.ws.ReponseProto;
import com.vdegree.february.im.api.ws.RequestProto;
import com.vdegree.february.im.common.constant.ChannelAttrConstant;
import com.vdegree.february.im.ws.cache.CacheChannelGroupManager;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import static com.vdegree.february.im.common.constant.IMCMD.REQUEST_HEARTBEAT;

/**
 * @Description: 处理消息的handler
 * TextWebSocketFrame： 在netty中，是用于为websocket专门处理文本的对象，frame是消息的载体
 *  这里已经指定了类型 如果这里是Object 那么下面还需判断是不是TextWebSocketFrame类型
 */
@Log4j2
@Component
@ChannelHandler.Sharable
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Autowired
    private CacheChannelGroupManager cacheChannelGroupManager;

    // 用于记录和管理所有客户端的channle
    @Autowired
    public CacheChannelGroupManager clients;

    @Autowired
    private Gson gson;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 握手完成 进行初始化
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
//           clients.add(ctx.channel().attr(ChannelAttrConstant.USERID).get(),ctx.channel());// TODO 占时注释
            long userId = RandomUtils.nextLong(1000,2000);
            clients.add(userId,ctx.channel());
            ctx.channel().attr(ChannelAttrConstant.USERID).set(userId);
        }
        super.userEventTriggered(ctx, evt);
    }



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg)
            throws Exception {
        // 获取客户端传输过来的消息
        String content = msg.text();
        System.out.println("接受到的数据：" + content);
        RequestProto requestProto = gson.fromJson(content, RequestProto.class);
        Long userId = ctx.channel().attr(ChannelAttrConstant.USERID).get();
        requestProto.setSendUserId(userId);
        if(requestProto.getCmd().equals(REQUEST_HEARTBEAT)){
            // TODO update 1、数据转换迁移至decodehandle 2、心跳移至独立handle
            ReponseProto reponseProto = ReponseProto.buildReponse(requestProto);
            if(cacheChannelGroupManager.refresh(userId)){
                log.debug("用户：{} 心跳成功",userId);
                ctx.channel().writeAndFlush(new TextWebSocketFrame(gson.toJson(reponseProto)));
                return;
            }
            log.error("用户：{} 心跳失败 用户失效",userId);
            reponseProto.setError(ErrorEnum.HEART_BEAT_ERROR);
            ctx.channel().writeAndFlush(new TextWebSocketFrame(gson.toJson(reponseProto))).addListener(future -> {
                if(future.isSuccess()){
                    ctx.close();
                }
            });
            return;
        }else{
            requestProto.setWSProxyStartTime(System.currentTimeMillis());
            rabbitTemplate.convertAndSend("IMServiceDirectExchange","IMServiceDirectRouting", requestProto);
        }
    }
}