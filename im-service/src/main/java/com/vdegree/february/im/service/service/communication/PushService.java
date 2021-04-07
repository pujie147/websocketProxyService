package com.vdegree.february.im.service.service.communication;

import com.google.common.collect.Lists;
import com.vdegree.february.im.api.ws.message.push.*;
import com.vdegree.february.im.common.constant.type.ErrorEnum;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.common.constant.type.ReplyType;
import com.vdegree.february.im.common.constant.type.RoomType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/4/7 14:31
 */
@Component
public class PushService {
    @Autowired
    private PushManager pushManager;
//    邀请用户进入房间
    public void invitedUserEnterRoom(RoomType roomType,Long sendUserId,Long invitedUserId){
        pushManager.pushProto(IMCMD.PUSH_INVITED_USER_ENTER_ROOM,new InvitedUserEnterRoomPushMsg(sendUserId,roomType), Lists.newArrayList(invitedUserId));
    }

//            拒绝邀请
    public void refuseInvitation(Long invitedUserId, ReplyType replyType, RoomType roomType,Long sendUserId){
        pushManager.pushProto(IMCMD.PUSH_REFUSE_INVITATION, new RefuseInvitationPushMsg(invitedUserId,replyType,roomType),Lists.newArrayList(sendUserId));
    }
//    进入房间
    public void enterRoom(String roomId,RoomType roomType,String token,Long sendUserId){
        pushManager.pushProto(IMCMD.PUSH_ENTER_ROOM, new EnterRoomPushMsg(roomId,roomType,token),Lists.newArrayList(sendUserId));
    }
//            退出房间
    public void quitRoom(String roomId,RoomType roomType,Long sendUserId,Long invitedUserId){
        pushManager.pushProto(IMCMD.PUSH_QUIT_ROOM, new QuitRoomPushMsg(sendUserId,roomId, roomType), Lists.newArrayList(invitedUserId));
    }

//    服务器事件
    public void serviceEvent(ErrorEnum errorEnum,Long sendUserId){
        pushManager.pushProto(IMCMD.PUSH_SERVICE_EVENT, new ServiceEventPushMsg(ErrorEnum.ROOM_FAILURE), Lists.newArrayList(sendUserId));
    }


//    抢单邀请
    public void grabOrderInvitation(Long sendUserid,Integer enterRoomCode,RoomType roomType,List<Long> invitationUserIds){
        pushManager.pushProto(IMCMD.PUSH_GRAB_ORDER_INVITATION, new GrabOrderInvitationPushMsg(sendUserid,roomType,enterRoomCode), invitationUserIds);
    }
//            抢单结束
}
