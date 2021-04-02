package com.vdegree.february.im.service.communication;

import com.google.gson.Gson;
import com.vdegree.february.im.api.im2ws.IM2WSProto;
import com.vdegree.february.im.api.ws.ProtoContext;
import com.vdegree.february.im.api.ws.PushProto;
import com.vdegree.february.im.api.ws.ResponseProto;
import com.vdegree.february.im.common.constant.WSPorxyBroadcastConstant;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.common.constant.type.PushType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/31 15:36
 */
@Component
public class IM2WSManager {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void sendProto(IMCMD imcmd, IM2WSProto message){
        ProtoContext protoContext = new ProtoContext();
        protoContext.getInternalProto().setImCMDType(imcmd.getType());
        protoContext.getInternalProto().setIm2WSProto(message);
        rabbitTemplate.convertAndSend(WSPorxyBroadcastConstant.EXCHANGE_NAME,null,protoContext);
    }

}
