package com.vdegree.february.im.ws.mqlistener;

import com.vdegree.february.im.api.BaseHandle;
import com.vdegree.february.im.api.WSCMD;
import com.vdegree.february.im.api.ws.base.reponse.ReponseProto;
import com.vdegree.february.im.common.utils.SpringContextUtil;
import com.vdegree.february.im.ws.cache.CacheChannelGroupManager;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 19:03
 */
@Component
public class WSProxyBroadcastConsume {
    @Autowired
    private SpringContextUtil springContextUtil;

    @Autowired
    private CacheChannelGroupManager cacheChannelGroupManager;

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "WSProxyBroadcastConsumeQueue"),
            exchange = @Exchange(value = "WSProxyBroadcastConsumeExchange", type = ExchangeTypes.FANOUT),
            key = "WSProxyBroadcastConsumeRoutingKey"))
    public void process(ReponseProto msg){
        System.out.println("Receiver2: "+msg.toString());
//        msg.getCmd() TODO 区分是响应 还是 推送
//        cacheChannelGroupManager.writeInUserIdsAndFlush(msg.)
//        springContextUtil.getBean(msg.getCmd().getHandBean(), BaseHandle.class).exector(msg);
        WSCMD consumeType = WSCMD.getConsumeType(msg.getCmd());
        BaseHandle baseHandle = springContextUtil.getBean(consumeType.getHandBean(), BaseHandle.class);
        

    }
}
