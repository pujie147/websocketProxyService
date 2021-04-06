package com.vdegree.february.im.service.mqlistener;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.vdegree.february.im.api.ws.*;
import com.vdegree.february.im.common.constant.ImServiceQueueConstant;
import com.vdegree.february.im.common.constant.WSPorxyBroadcastConstant;
import com.vdegree.february.im.common.constant.type.ErrorEnum;
import com.vdegree.february.im.service.communication.HandlerInfo;
import com.vdegree.february.im.service.communication.MQRoutingManger;
import com.vdegree.february.im.service.handle.BaseImServiceHandle;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 19:03
 */
@Component
@Log4j2
public class ReceiverWSProxy {
    @Autowired
    @Qualifier(value = "routingManger")
    private MQRoutingManger routingManger;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Gson gson;



    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = ImServiceQueueConstant.QUEUE_NAME),
            exchange = @Exchange(value = ImServiceQueueConstant.EXCHANGE_NAME, type = ExchangeTypes.DIRECT),
            key = ImServiceQueueConstant.ROUTING_KEY))
    public void process(ProtoContext msg){
        try {
            System.out.println("ReceiverWSProxy: " + msg);
            if(msg.getBaseProto().getCmd()==null) {
                msg.setBaseProto(gson.fromJson(msg.getJson(), BaseProto.class));
            }
            HandlerInfo handlerInfo = routingManger.get(msg.getBaseProto().getCmd().getType());
            if(handlerInfo!=null){
                List<Object> list = builParameter(handlerInfo.getParams(),msg);
                try {
                    Object responseMsg = handlerInfo.getMethod().invoke(handlerInfo.getClazz(), list.toArray());
                    if (responseMsg instanceof ErrorEnum) {
                        msg.buildFailResponseProto((ErrorEnum) responseMsg,gson);
                    } else if (responseMsg instanceof ResponseProto) {
                        msg.setResponseProto(gson.toJson(responseMsg));
                    } else if (responseMsg instanceof Object) {
                        msg.buildSuccessResponseProto(responseMsg,gson);
                    }else{
                        msg.buildSuccessResponseProto(gson);
                    }
                }catch (Exception e){
                    log.error(e);
                    msg.buildFailResponseProto(ErrorEnum.SYSTEM_ERROR,gson);
                }
                rabbitTemplate.convertAndSend(WSPorxyBroadcastConstant.EXCHANGE_NAME,null,msg);
            }
            return;
        }catch (Exception e){
            log.error(e);
        }
        log.error("未找到reqeust userId:{} json :{}",msg.getInternalProto().getSendUserId(),msg.getJson());
    }

    public boolean supportsParameter(Class<?> paramType) {
        return (InternalProto.class.isAssignableFrom(paramType) ||
                RequestProto.class.isAssignableFrom(paramType) ||
                ResponseProto.class.isAssignableFrom(paramType) ||
                ProtoContext.class.isAssignableFrom(paramType));
    }

    public List<Object> builParameter(List<Class> params,ProtoContext msg){
        List<Object> list = Lists.newArrayList();
        params.forEach(param-> {
            if (supportsParameter(param)) {
                if (InternalProto.class.isAssignableFrom(param)) {
                    list.add(msg.getInternalProto());
                } else if (RequestProto.class.isAssignableFrom(param)) {
                    list.add(gson.fromJson(msg.getJson(),TypeToken.of(param).getType()));
                } else if (ProtoContext.class.isAssignableFrom(param)) {
                    list.add(msg);
                }
            } else {
                list.add(new Object());
            }
        });
        return list;
    }


}
