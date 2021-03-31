package com.vdegree.february.im.ws.handler;

import com.google.gson.Gson;
import com.vdegree.february.im.api.IMController;
import com.vdegree.february.im.api.ws.WSProtoContext;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.ws.cache.CacheChannelGroupManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 21:02
 */
@IMController(cmd=IMCMD.PUSH)
@Log4j2
public class PushHandler implements BaseWsProxyHandle {
    @Autowired
    private CacheChannelGroupManager cacheChannelGroupManager;

    @Autowired
    private Gson gson;

    @Override
    public void execute(WSProtoContext wsProtoContext) {
        log.info("push Handler : "+ wsProtoContext.toString());
//        PushProto pushProto = PushProto.buildPush(proto);
//        switch (pushProto.getPushType()){
//            case PUSH_ALL_USER:
//                cacheChannelGroupManager.writeALLAndFlush(new TextWebSocketFrame(gson.toJson(pushProto)));
//                break;
//            case PUSH_CONTAIN_USER:
//                cacheChannelGroupManager.writeInUserIdsAndFlush(pushProto.getPushUserIds(),new TextWebSocketFrame(gson.toJson(pushProto)));
//                break;
//            case PUSH_NON_CONTAIN_USER:
//                cacheChannelGroupManager.writeNoInUserIdsAndFlush(pushProto.getPushUserIds(),new TextWebSocketFrame(gson.toJson(pushProto)));
//                break;
//            default:
//                log.error("pushType not exist");
//        }
//        log.error("用户：{} 连接已失效 cmd:{}",proto.getSendUserId(),proto.getCmd().getType());
    }
}
