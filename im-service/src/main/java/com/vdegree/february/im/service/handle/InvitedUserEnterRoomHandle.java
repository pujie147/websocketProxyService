package com.vdegree.february.im.service.handle;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.api.IMController;
import com.vdegree.february.im.api.ws.BaseProto;
import com.vdegree.february.im.api.ws.PushProto;
import com.vdegree.february.im.api.ws.ResponseProto;
import com.vdegree.february.im.api.ws.RequestProto;
import com.vdegree.february.im.api.ws.message.push.InvitedUserEnterRoomPushMsg;
import com.vdegree.february.im.api.ws.message.request.InvitedUserEnterRoomRequestMsg;
import com.vdegree.february.im.common.constant.type.PushType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 1.接收客户端发起的聊天邀请
 * 2.回调appservice判断是否有可以邀请
 * 3.push 被邀请用户客户端
 * 4.return
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 19:56
 */
@IMController(cmd = IMCMD.REQUEST_INVITED_USER_ENTER_ROOM)
public class InvitedUserEnterRoomHandle implements BaseImServiceHandle {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Gson gson;

    @Override
    public ResponseProto execute(RequestProto requestProto) {
        RequestProto<InvitedUserEnterRoomRequestMsg> invitedUserEnterRoomRequestMsgRequestProto = gson.fromJson(requestProto.getJson(), new TypeToken<RequestProto<InvitedUserEnterRoomRequestMsg>>(){}.getType());

        InvitedUserEnterRoomPushMsg pushMsg = new InvitedUserEnterRoomPushMsg();
        pushMsg.setRoomType(invitedUserEnterRoomRequestMsgRequestProto.getMessage().getRoomType());
        pushMsg.setSendUserId(requestProto.getSendUserId());
        PushProto pushProto = PushProto.buildPush(IMCMD.PUSH_INVITED_USER_ENTER_ROOM, gson.toJson(pushMsg), PushType.PUSH_CONTAIN_USER,Lists.newArrayList(invitedUserEnterRoomRequestMsgRequestProto.getMessage().getInvitedUserId()));
        rabbitTemplate.convertAndSend("WSProxyBroadcastConsumeExchange",null,(BaseProto)pushProto);
        return ResponseProto.buildResponse(requestProto);
    }
}
