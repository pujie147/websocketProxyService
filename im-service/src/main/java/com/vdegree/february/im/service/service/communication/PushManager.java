package com.vdegree.february.im.service.service.communication;

import com.google.gson.Gson;
import com.vdegree.february.im.api.ws.ProtoContext;
import com.vdegree.february.im.api.ws.PushProto;
import com.vdegree.february.im.common.constant.WSPorxyBroadcastConstant;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.common.constant.type.PushType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 推送client 的管理工具
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/31 15:36
 */
@Component
class PushManager {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Gson gson;

    public void pushProto(IMCMD imcmd, Object message, List<Long> pushUserIds){
        PushProto pushProto = PushProto.buildPush(imcmd, message);
        ProtoContext protoContext = new ProtoContext();
        protoContext.setResponseProto(gson.toJson(pushProto));
        protoContext.getBaseProto().setCmd(imcmd);
        protoContext.getInternalProto().setPustUserIds(pushUserIds);
        protoContext.getInternalProto().setPushType(PushType.PUSH_CONTAIN_USER);
        protoContext.getInternalProto().setImCMDType(imcmd.getType());
        rabbitTemplate.convertAndSend(WSPorxyBroadcastConstant.EXCHANGE_NAME,null,protoContext);
    }

}
