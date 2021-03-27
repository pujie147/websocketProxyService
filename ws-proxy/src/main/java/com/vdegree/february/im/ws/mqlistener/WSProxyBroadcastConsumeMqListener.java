package com.vdegree.february.im.ws.mqlistener;

import com.vdegree.february.im.common.constant.IMCMD;
import com.vdegree.february.im.api.ws.BaseProto;
import com.vdegree.february.im.ws.handler.BaseWsProxyHandle;
import com.vdegree.february.im.ws.handler.ControllerManger;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
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
public class WSProxyBroadcastConsumeMqListener {
    @Autowired
    @Qualifier("controllerManager")
    private ControllerManger controllerManager;

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "WSProxyBroadcastConsumeQueue"),
            exchange = @Exchange(value = "WSProxyBroadcastConsumeExchange", type = ExchangeTypes.FANOUT),
            key = "WSProxyBroadcastConsumeRoutingKey"))
    public void process(BaseProto msg){
        System.out.println("WSProxyBroadcastConsumeMqListener: "+msg.toString());
        // 判断消费类型
        IMCMD consumeType = IMCMD.getConsumeType(msg.getCmd());
        BaseWsProxyHandle handle = controllerManager.getHandler(consumeType.getType());
        if(handle!=null){
            handle.execute(msg);
            return;
        }
        log.error("找不到对应hanlde cmd :{} user:{}",msg.getCmd().getType(),msg.getSendUserId());
    }
}
