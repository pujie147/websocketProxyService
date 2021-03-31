package com.vdegree.february.im.ws.handler.netty;



import com.google.gson.Gson;
import com.vdegree.february.im.api.ws.BaseProto;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


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
public class EncodeHandler extends MessageToMessageEncoder<BaseProto> {
    @Autowired
    private Gson gson;
    @Override
    protected void encode(ChannelHandlerContext ctx, BaseProto baseProto, List<Object> out) throws Exception {
        baseProto.setResponseTime(System.currentTimeMillis());
        out.add(new TextWebSocketFrame(gson.toJson(baseProto)));
    }
}