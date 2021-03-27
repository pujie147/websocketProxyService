package com.vdegree.february.im.service.handle;

import com.vdegree.february.im.api.IMCMD;
import com.vdegree.february.im.api.IMController;
import com.vdegree.february.im.api.ws.PushProto;
import com.vdegree.february.im.api.ws.ReponseProto;
import com.vdegree.february.im.api.ws.RequestProto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 19:56
 */
@IMController(cmd = IMCMD.REQUEST_SEND_1V1_ROOM_REQUEST)
public class Send1v1RoomRequestHandle implements BaseImServiceHandle {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ReponseProto execute(RequestProto requestProto) {

//        PushProto.buildPush(IMCMD,null);
//        rabbitTemplate.convertAndSend("WSProxyBroadcastConsumeExchange", null, reponse);
        return ReponseProto.buildReponse(requestProto);
    }
}
