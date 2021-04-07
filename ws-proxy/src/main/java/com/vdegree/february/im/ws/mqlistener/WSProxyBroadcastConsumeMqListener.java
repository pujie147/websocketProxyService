package com.vdegree.february.im.ws.mqlistener;

import com.google.common.collect.Lists;
import com.vdegree.february.im.api.ws.ProtoContext;
import com.vdegree.february.im.common.constant.WSPorxyBroadcastConstant;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.common.routing.HandlerInfo;
import com.vdegree.february.im.common.routing.MQRoutingManger;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

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
    private MQRoutingManger routingManger;

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "#{queue.name}"),
            exchange = @Exchange(value = WSPorxyBroadcastConstant.EXCHANGE_NAME, type = ExchangeTypes.FANOUT),
            key = WSPorxyBroadcastConstant.ROUTING_KEY))
    public void process(ProtoContext protoContext){
        log.debug("WSProxyBroadcastConsumeMqListener: "+ protoContext.toString());
        // 判断消费类型
        try {
            IMCMD consumeType = IMCMD.getConsumeType(protoContext.getInternalProto().getImCMDType());
            HandlerInfo routingInfo = routingManger.get(consumeType.getType());
            if (routingInfo != null) {
                List<Object> params = builParameter(routingInfo.getParams(), protoContext);
                Object result = routingInfo.getMethod().invoke(routingInfo.getClazz(), params.toArray());
                return;
            }
        }catch (Exception e){
            log.error(e);
        }
    }

    public boolean supportsParameter(Class<?> paramType) {
        return (ProtoContext.class.isAssignableFrom(paramType)

        );
    }

    public List<Object> builParameter(List<Class> params, ProtoContext msg) throws RuntimeException{
        List<Object> list = Lists.newArrayList();
        params.forEach(param-> {
            if (supportsParameter(param)) {
                if (ProtoContext.class.isAssignableFrom(param)) {
                    list.add(msg);
                }
            } else {
                throw new RuntimeException("no supports parameter");
            }
        });
        return list;
    }


}
