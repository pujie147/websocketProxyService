package com.vdegree.february.im.service.mqlistener;

import com.vdegree.february.im.api.ws.BaseProto;
import com.vdegree.february.im.api.ws.RequestProto;
import com.vdegree.february.im.common.constant.ImServiceQueueConstant;
import com.vdegree.february.im.common.constant.WSPorxyBroadcastConstant;
import com.vdegree.february.im.service.ControllerManger;
import com.vdegree.february.im.service.handle.BaseImServiceHandle;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


/**
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 19:03
 */
@Component
@Log4j2
public class ReceiverWSProxy {
    @Autowired
    @Qualifier(value = "controllerManager")
    private ControllerManger controllerManager;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = ImServiceQueueConstant.QUEUE_NAME),
            exchange = @Exchange(value = ImServiceQueueConstant.EXCHANGE_NAME, type = ExchangeTypes.DIRECT),
            key = ImServiceQueueConstant.ROUTING_KEY))
    public void process(RequestProto msg){
        try {
            System.out.println("ReceiverWSProxy: " + msg);
            BaseImServiceHandle handler = controllerManager.get(msg.getCmd().getType());
            if (handler != null) {
                BaseProto reponse = handler.execute(msg);
                rabbitTemplate.convertAndSend(WSPorxyBroadcastConstant.EXCHANGE_NAME, null, reponse);
                return;
            }
        }catch (Exception e){
            log.error(e);
        }
        log.error("未找到cmd:{} , sendUser:{}",msg.getCmd().getType(),msg.getSendUserId());
    }
}
