package com.vdegree.february.im.ws.mqlistener;

import com.vdegree.february.im.api.ws.ProtoContext;
import com.vdegree.february.im.common.constant.WSPorxyBroadcastConstant;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.ws.handler.BaseWsProxyHandle;
import com.vdegree.february.im.ws.handler.RoutingManger;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * wsproxy 接收 imservice 广播消费
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 19:03
 */
@Component
@Log4j2
public class WSProxyBroadcastConsumeMqListener {
    @Autowired
    @Qualifier("routingManager")
    private RoutingManger controllerManager;

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = WSPorxyBroadcastConstant.QUEUE_NAME),
            exchange = @Exchange(value = WSPorxyBroadcastConstant.EXCHANGE_NAME, type = ExchangeTypes.FANOUT),
            key = WSPorxyBroadcastConstant.ROUTING_KEY)) // TODO queue name 每一台 wsProxy 服务要唯一
    public void process(ProtoContext protoContext){
        System.out.println("WSProxyBroadcastConsumeMqListener: "+ protoContext.toString());
        // 判断消费类型
        IMCMD consumeType = IMCMD.getConsumeType(protoContext.getInternalProto().getImCMDType());
        BaseWsProxyHandle handle = controllerManager.getHandler(consumeType.getType());
        if(handle!=null){
            handle.execute(protoContext);
            return;
        }
        log.error("找不到对应hanlde cmd :{} user:{}", protoContext.getInternalProto().getImCMDType());
    }
}
