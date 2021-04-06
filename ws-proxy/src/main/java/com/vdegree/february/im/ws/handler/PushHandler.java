package com.vdegree.february.im.ws.handler;

import com.vdegree.february.im.api.IMCMDRouting;
import com.vdegree.february.im.api.ws.ProtoContext;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.ws.cache.CacheChannelGroupManager;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * wsproxy 推送 handler
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 21:02
 */
@IMCMDRouting(cmd=IMCMD.PUSH)
@Log4j2
public class PushHandler implements BaseWsProxyHandle {
    @Autowired
    private CacheChannelGroupManager cacheChannelGroupManager;

    @Override
    public void execute(ProtoContext protoContext) {
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
}
