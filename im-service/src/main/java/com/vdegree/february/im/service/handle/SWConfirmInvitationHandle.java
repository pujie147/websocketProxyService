package com.vdegree.february.im.service.handle;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.vdegree.february.im.api.ws.*;
import com.vdegree.february.im.api.ws.message.push.RefuseInvitationPushMsg;
import com.vdegree.february.im.common.cache.RoomDataRedisManger;
import com.vdegree.february.im.common.cache.UserDataRedisManger;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.api.IMCMDRouting;
import com.vdegree.february.im.common.constant.type.ReplyType;
import com.vdegree.february.im.api.ws.message.push.EnterRoomPushMsg;
import com.vdegree.february.im.api.ws.message.request.ConfirmInvitationRequestMsg;
import com.vdegree.february.im.common.utils.RoomIdGenerateUtil;
import com.vdegree.february.im.common.utils.agora.RtcTokenBuilderUtil;
import com.vdegree.february.im.service.PushManager;
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
@IMCMDRouting(cmd = IMCMD.REQUEST_CONFIRM_INVITATION_ROOM,value = "swConfirmInvitationHandle")
public class SWConfirmInvitationHandle extends ConfirmInvitationHandle{
    @Autowired
    private UserDataRedisManger userDataRedisManger;
    @Autowired
    private RoomDataRedisManger roomDataRedisManger;
    @Autowired
    private Gson gson;
    @Autowired
    private RtcTokenBuilderUtil rtcTokenBuilderUtil;
    @Autowired
    private PushManager pushManager;

    @Override
    public ProtoContext execute(ProtoContext protoContext) {
        RequestProto<ConfirmInvitationRequestMsg> invitedUserEnterRoomRequestMsg = gson.fromJson(protoContext.getJson(), new TypeToken<RequestProto<ConfirmInvitationRequestMsg>>(){}.getType());
        Long sendUserId = invitedUserEnterRoomRequestMsg.getMessage().getSendUserId(); // 邀请人
        Long invitedUserId = protoContext.getInternalProto().getSendUserId(); // 被邀请人
        if(ReplyType.ACCEPT.equals(invitedUserEnterRoomRequestMsg.getMessage().getReplyType())){
            String roomId = RoomIdGenerateUtil.generate(sendUserId, invitedUserId, invitedUserEnterRoomRequestMsg.getMessage().getRoomType());
            EnterRoomPushMsg pushMsg = new EnterRoomPushMsg();
            pushMsg.setRoomType(invitedUserEnterRoomRequestMsg.getMessage().getRoomType());
            pushMsg.setRoomId(roomId);

            // 邀请人
            String token = rtcTokenBuilderUtil.build(sendUserId.intValue(), roomId);
            pushMsg.setToken(token);
            pushManager.pushProto(IMCMD.PUSH_ENTER_ROOM, pushMsg,Lists.newArrayList(sendUserId));
            userDataRedisManger.putRoomId(sendUserId,roomId);

            // 被邀请人
            token = rtcTokenBuilderUtil.build(invitedUserId.intValue(), roomId);
            pushMsg.setToken(token);
            pushManager.pushProto(IMCMD.PUSH_ENTER_ROOM, pushMsg,Lists.newArrayList(invitedUserId));
            userDataRedisManger.putRoomId(invitedUserId,roomId);

            roomDataRedisManger.buildNewRedisData(roomId,sendUserId,invitedUserId);
        }else{
            RefuseInvitationPushMsg pushMsg = new RefuseInvitationPushMsg();
            pushMsg.setInvitedUserId(invitedUserId);
            pushMsg.setReplyType(invitedUserEnterRoomRequestMsg.getMessage().getReplyType());
            pushMsg.setRoomType(invitedUserEnterRoomRequestMsg.getMessage().getRoomType());
            pushManager.pushProto(IMCMD.PUSH_ENTER_ROOM, pushMsg,Lists.newArrayList(sendUserId));
        }
        return protoContext.buildSuccessResponseProto();
    }
}
