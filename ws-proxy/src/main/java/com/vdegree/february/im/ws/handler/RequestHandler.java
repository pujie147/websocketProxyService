package com.vdegree.february.im.ws.handler;

import com.vdegree.february.im.api.ws.ProtoContext;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.api.IMCMDRouting;
import com.vdegree.february.im.ws.cache.CacheChannelGroupManager;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * wsproxy rquest Handler
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 21:02
 */
@IMCMDRouting(cmd=IMCMD.REQUEST)
@Log4j2
public class RequestHandler implements BaseWsProxyHandle {
    @Autowired
    private CacheChannelGroupManager cacheChannelGroupManager;

    @Override
    public void execute(ProtoContext protoContext) {
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
