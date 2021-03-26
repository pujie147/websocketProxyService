package com.vdegree.february.im.service.mqlistener;

import com.vdegree.february.im.api.BaseHandle;
import com.vdegree.february.im.api.ws.base.reponse.ReponseProto;
import com.vdegree.february.im.api.ws.base.request.RequestProto;
import com.vdegree.february.im.common.utils.SpringContextUtil;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 19:03
 */
@Component
public class ReceiverWSProxy {

    @Autowired
    private SpringContextUtil springContextUtil;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "IMServiceDirectQueue"),
            exchange = @Exchange(value = "IMServiceDirectExchange", type = ExchangeTypes.DIRECT),
            key = "IMServiceDirectRouting"))
    public void process(RequestProto msg){
        System.out.println("Receiver2: "+msg.toString());
        ReponseProto response = springContextUtil.getBean(msg.getCmd().getHandBean(), BaseHandle.class).exector(msg);
        rabbitTemplate.convertAndSend("WSProxyBroadcastConsumeExchange",null,response);
    }
}
