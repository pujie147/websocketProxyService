package com.vdegree.february.im.ws.handler;

import com.google.gson.Gson;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.api.IMController;
import com.vdegree.february.im.api.ws.BaseProto;
import com.vdegree.february.im.ws.cache.CacheChannelGroupManager;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO
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

    @Autowired
    private Gson gson;

    @Override
    public void execute(BaseProto proto) {
        log.info("request Handler : "+proto.toString());
        Channel channel = null;
        if(cacheChannelGroupManager.containsUserId(proto.getSendUserId())) {
            channel = cacheChannelGroupManager.getChannelByUserId(proto.getSendUserId());
            if(channel!=null){
                channel.writeAndFlush(new TextWebSocketFrame(gson.toJson(proto)));
                return;
            }
        }
//        log.error("用户：{} 连接已失效 cmd:{}",proto.getSendUserId(),proto.getCmd().getType());
    }
}
