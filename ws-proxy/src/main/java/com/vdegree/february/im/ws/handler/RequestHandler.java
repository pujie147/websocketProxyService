package com.vdegree.february.im.ws.handler;

import com.vdegree.february.im.api.ws.ProtoContext;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.api.IMController;
import com.vdegree.february.im.ws.cache.CacheChannelGroupManager;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO 把request组合成一个大对象RequestContext 包括 baseProto、requestProto、internalProto 的属性还有 buildresponseProto 和 buildPushProto 方法
 * InternalProto是内部服务器要用的数据和链路追踪信息 包括
 *  1、消息来源userId
 *  2、消息接收时间
 *  3、消息在imService收发时间
 *  4、消息是imService执行结束时间
 *
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 21:02
 */
@IMController(cmd=IMCMD.REQUEST)
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
