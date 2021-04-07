package com.vdegree.february.im.service.service.communication;

import com.vdegree.february.im.api.im2ws.IM2WSProto;
import com.vdegree.february.im.api.ws.ProtoContext;
import com.vdegree.february.im.common.constant.WSPorxyBroadcastConstant;
import com.vdegree.february.im.common.constant.type.IMCMD;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Im -> wsProxy 发送mq 管理
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/31 15:36
 */
@Component
class IM2WSManager {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void sendProto(IMCMD imcmd, IM2WSProto message){
        ProtoContext protoContext = new ProtoContext();
        protoContext.getInternalProto().setImCMDType(imcmd.getType());
        protoContext.getInternalProto().setIm2WSProto(message);
        rabbitTemplate.convertAndSend(WSPorxyBroadcastConstant.EXCHANGE_NAME,null,protoContext);
    }

}
