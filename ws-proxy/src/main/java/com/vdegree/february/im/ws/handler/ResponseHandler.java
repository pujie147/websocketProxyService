package com.vdegree.february.im.ws.handler;

import com.vdegree.february.im.common.routing.IMCMDRouting;
import com.vdegree.february.im.common.routing.IMCMDUp;
import com.vdegree.february.im.api.ws.ProtoContext;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.ws.cache.CacheChannelGroupManager;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 服务器发送给客户端消息的handle
 *
 * @author DELL
 * @version 1.0
 * @date 2021/4/7 13:51
 */
@IMCMDUp
@Log4j2
public class ResponseHandler {
    @Autowired
    private CacheChannelGroupManager cacheChannelGroupManager;


    @IMCMDRouting(cmd= IMCMD.PUSH)
    public void pushHandler(ProtoContext protoContext){
        log.info("push Handler : "+ protoContext.toString());
        switch (protoContext.getInternalProto().getPushType()){
            case PUSH_ALL_USER:
                cacheChannelGroupManager.writeALLAndFlush(new TextWebSocketFrame(protoContext.getResponseProto()));
                break;
            case PUSH_CONTAIN_USER:
                cacheChannelGroupManager.writeInUserIdsAndFlush(protoContext.getInternalProto().getPustUserIds(),new TextWebSocketFrame(protoContext.getResponseProto()));
                break;
            case PUSH_NON_CONTAIN_USER:
                cacheChannelGroupManager.writeNoInUserIdsAndFlush(protoContext.getInternalProto().getPustUserIds(),new TextWebSocketFrame(protoContext.getResponseProto()));
                break;
            default:
                log.error("pushType not exist");
        }
//        log.error("用户：{} 连接已失效 cmd:{}",proto.getSendUserId(),proto.getCmd().getType());
    }

    @IMCMDRouting(cmd= IMCMD.REQUEST)
    public void requestHandler(ProtoContext protoContext){
        log.info("request Handler : "+ protoContext.toString());
        Channel channel = null;
        if(cacheChannelGroupManager.containsUserId(protoContext.getInternalProto().getSendUserId())) {
            channel = cacheChannelGroupManager.getChannelByUserId(protoContext.getInternalProto().getSendUserId());
            if(channel!=null){
                channel.writeAndFlush(new TextWebSocketFrame(protoContext.getResponseProto()));
                return;
            }
        }
        log.error("用户：{} 连接已失效 cmd:{}",protoContext.getInternalProto().getSendUserId(),protoContext.getBaseProto().getCmd().getType());
    }
}
