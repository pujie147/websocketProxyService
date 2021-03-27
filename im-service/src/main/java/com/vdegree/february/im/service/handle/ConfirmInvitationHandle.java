package com.vdegree.february.im.service.handle;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.vdegree.february.im.api.ws.message.push.RefuseInvitationPushMsg;
import com.vdegree.february.im.common.cache.RoomDataRedisManger;
import com.vdegree.february.im.common.cache.RoomHeartBeatRedisManger;
import com.vdegree.february.im.common.cache.UserDataRedisManger;
import com.vdegree.february.im.common.constant.IMCMD;
import com.vdegree.february.im.api.IMController;
import com.vdegree.february.im.api.ws.BaseProto;
import com.vdegree.february.im.api.ws.PushProto;
import com.vdegree.february.im.api.ws.ReponseProto;
import com.vdegree.february.im.api.ws.RequestProto;
import com.vdegree.february.im.common.constant.ReplyType;
import com.vdegree.february.im.api.ws.message.push.EnterRoomPushMsg;
import com.vdegree.february.im.api.ws.message.request.ConfirmInvitationRequestMsg;
import com.vdegree.february.im.common.utils.RoomIdGenerateUtil;
import com.vdegree.february.im.common.utils.agora.RtcTokenBuilderUtil;
import com.vdegree.february.im.common.utils.agora.media.RtcTokenBuilder;
import org.checkerframework.checker.units.qual.A;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

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
@IMController(cmd = IMCMD.REQUEST_CONFIRM_INVITATION_ROOM)
public class ConfirmInvitationHandle implements BaseImServiceHandle {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private UserDataRedisManger userDataRedisManger;
    @Autowired
    private RoomHeartBeatRedisManger roomHeartBeatRedisManger;

    @Autowired
    private Gson gson;
    @Autowired
    private RtcTokenBuilderUtil rtcTokenBuilderUtil;

    @Override
    public ReponseProto execute(RequestProto requestProto) {
        ConfirmInvitationRequestMsg invitedUserEnterRoomRequestMsg = gson.fromJson(requestProto.getMsg(), ConfirmInvitationRequestMsg.class);
        Long sendUserId = invitedUserEnterRoomRequestMsg.getSendUserId(); // 邀请人
        Long invitedUserId = requestProto.getSendUserId(); // 被邀请人
        if(ReplyType.ACCEPT.equals(invitedUserEnterRoomRequestMsg.getReplyType())){
            String roomId = RoomIdGenerateUtil.generate(sendUserId, invitedUserId, invitedUserEnterRoomRequestMsg.getRoomType());
            EnterRoomPushMsg pushMsg = new EnterRoomPushMsg();
            pushMsg.setRoomType(invitedUserEnterRoomRequestMsg.getRoomType());
            pushMsg.setRoomId(roomId);

            // 邀请人
            String token = rtcTokenBuilderUtil.build(sendUserId.intValue(), roomId);
            pushMsg.setToken(token);
            PushProto pushProto = PushProto.buildPush(IMCMD.PUSH_ENTER_ROOM, gson.toJson(pushMsg));
            pushProto.setPushUserIds(Lists.newArrayList(sendUserId));
            rabbitTemplate.convertAndSend("WSProxyBroadcastConsumeExchange",null,(BaseProto)pushProto);
            userDataRedisManger.putRoomId(sendUserId,roomId);


            // 被邀请人
            token = rtcTokenBuilderUtil.build(invitedUserId.intValue(), roomId);
            pushMsg.setToken(token);
            pushProto = PushProto.buildPush(IMCMD.PUSH_ENTER_ROOM, gson.toJson(pushMsg));
            pushProto.setPushUserIds(Lists.newArrayList(invitedUserId));
            rabbitTemplate.convertAndSend("WSProxyBroadcastConsumeExchange",null,(BaseProto)pushProto);

            userDataRedisManger.putRoomId(invitedUserId,roomId);
            roomHeartBeatRedisManger.generateRedisUserEffectiveTime(roomId,sendUserId,invitedUserId);

        }else{
            RefuseInvitationPushMsg pushMsg = new RefuseInvitationPushMsg();
            pushMsg.setInvitedUserId(invitedUserId);
            pushMsg.setReplyType(invitedUserEnterRoomRequestMsg.getReplyType());
            pushMsg.setRoomType(invitedUserEnterRoomRequestMsg.getRoomType());
            PushProto pushProto = PushProto.buildPush(IMCMD.PUSH_REFUSE_INVITATION, gson.toJson(pushMsg));
            pushProto.setPushUserIds(Lists.newArrayList(sendUserId));
            rabbitTemplate.convertAndSend("WSProxyBroadcastConsumeExchange",(BaseProto)pushProto);
        }
        return ReponseProto.buildReponse(requestProto);
    }
}