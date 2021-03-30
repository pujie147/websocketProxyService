package com.vdegree.february.im.ws.mqlistener;

import com.vdegree.february.im.api.ws.WSRequestProtoContext;
import com.vdegree.february.im.common.constant.WSPorxyBroadcastConstant;
import com.vdegree.february.im.common.constant.type.IMCMD;
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
            value = @Queue(value = WSPorxyBroadcastConstant.QUEUE_NAME),
            exchange = @Exchange(value = WSPorxyBroadcastConstant.EXCHANGE_NAME, type = ExchangeTypes.FANOUT),
            key = WSPorxyBroadcastConstant.ROUTING_KEY))
    public void process(WSRequestProtoContext wsRequestProtoContext){
        System.out.println("WSProxyBroadcastConsumeMqListener: "+wsRequestProtoContext.toString());
        // 判断消费类型
        IMCMD consumeType = IMCMD.getConsumeType(wsRequestProtoContext.getBaseProto().getCmd());
        BaseWsProxyHandle handle = controllerManager.getHandler(consumeType.getType());
        if(handle!=null){
            handle.execute(wsRequestProtoContext);
            return;
        }
        log.error("找不到对应hanlde cmd :{} user:{}",wsRequestProtoContext.getBaseProto().getCmd().getType(),wsRequestProtoContext.getInternalProto().getSendUserId());
    }
}
