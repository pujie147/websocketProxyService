package com.vdegree.february.im.service.handle;

import com.google.common.collect.Lists;
import com.vdegree.february.im.common.routing.IMCMDUp;
import com.vdegree.february.im.api.im2ws.message.IM2WSInitRoomHeartbeatProto;
import com.vdegree.february.im.api.ws.InternalProto;
import com.vdegree.february.im.api.ws.message.push.EnterRoomPushMsg;
import com.vdegree.february.im.api.ws.message.push.InvitedUserEnterRoomPushMsg;
import com.vdegree.february.im.api.ws.message.push.QuitRoomPushMsg;
import com.vdegree.february.im.api.ws.message.push.RefuseInvitationPushMsg;
import com.vdegree.february.im.api.ws.message.request.ConfirmEntryRoomRequestMsg;
import com.vdegree.february.im.api.ws.message.request.ConfirmInvitationRequestMsg;
import com.vdegree.february.im.api.ws.message.request.InvitedUserEnterRoomRequestMsg;
import com.vdegree.february.im.api.ws.message.request.QuitRoomRequestMsg;
import com.vdegree.february.im.common.cache.RoomDataRedisManger;
import com.vdegree.february.im.common.cache.UserDataRedisManger;
import com.vdegree.february.im.common.constant.type.ErrorEnum;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.common.routing.IMCMDRouting;
import com.vdegree.february.im.common.constant.type.ReplyType;
import com.vdegree.february.im.common.constant.type.RoomType;
import com.vdegree.february.im.common.utils.agora.RtcTokenBuilderUtil;
import com.vdegree.february.im.service.communication.IM2WSManager;
import com.vdegree.february.im.service.communication.PushManager;
import io.netty.util.internal.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * 1对1房间处理
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 19:56
 */
@IMCMDUp
@Log4j2
public class OneVOneRoomHandle {

    @Autowired
    private UserDataRedisManger userDataRedisManger;

    @Autowired
    private RoomDataRedisManger roomDataRedisManger;

    @Autowired
    private PushManager pushManager;

    @Autowired
    private RtcTokenBuilderUtil rtcTokenBuilderUtil;

    @Autowired
    private IM2WSManager im2WSManager;

    
    /**
     * @Author DELL
     * @Date 10:35 2021/4/7
     * @Description 邀请用户进入房间
     * @param: msg
     * @param: internalProto
     * @Return com.vdegree.february.im.common.constant.type.ErrorEnum 
     * @Exception 
     **/
    @IMCMDRouting(cmd = IMCMD.REQUEST_INVITED_USER_ENTER_ROOM)
    public ErrorEnum invitedUserEnterRoomHandle(InvitedUserEnterRoomRequestMsg msg,InternalProto internalProto){
        // TODO 回调 appservice 是否能发起邀请
        InvitedUserEnterRoomPushMsg pushMsg = new InvitedUserEnterRoomPushMsg();
        pushMsg.setRoomType(msg.getRoomType());
        pushMsg.setSendUserId(internalProto.getSendUserId());
        pushManager.pushProto(IMCMD.PUSH_INVITED_USER_ENTER_ROOM,pushMsg, Lists.newArrayList(msg.getInvitedUserId()));
        return ErrorEnum.SUCCESS;
    }

    /**
     * @Author DELL
     * @Date 13:39 2021/4/7
     * @Description 确认邀请
     * @param: msg
     * @param: internalProto
     * @Return com.vdegree.february.im.common.constant.type.ErrorEnum 
     * @Exception 
     **/
    @IMCMDRouting(cmd = IMCMD.REQUEST_CONFIRM_INVITATION_ROOM)
    public ErrorEnum confirmInvitationHandle(ConfirmInvitationRequestMsg msg,InternalProto internalProto){
        Long sendUserId = msg.getSendUserId(); // 邀请人
        Long invitedUserId = internalProto.getSendUserId(); // 被邀请人
        if(ReplyType.ACCEPT.equals(msg.getReplyType())){
            String roomId = msg.getRoomType().generate(sendUserId, invitedUserId);
            EnterRoomPushMsg pushMsg = new EnterRoomPushMsg();
            pushMsg.setRoomType(msg.getRoomType());
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
            im2WSManager.sendProto(IMCMD.IM_WP_INIT_ROOM_HEARBEAT,new IM2WSInitRoomHeartbeatProto(sendUserId,roomId));
        }else{
            RefuseInvitationPushMsg pushMsg = new RefuseInvitationPushMsg();
            pushMsg.setInvitedUserId(invitedUserId);
            pushMsg.setReplyType(msg.getReplyType());
            pushMsg.setRoomType(msg.getRoomType());
            pushManager.pushProto(IMCMD.PUSH_ENTER_ROOM, pushMsg,Lists.newArrayList(sendUserId));
        }
        //TODO 回调appservice 告知是否接受
        return ErrorEnum.SUCCESS;
    }


    /**
     * @Author DELL
     * @Date 10:26 2021/4/7
     * @Description 确认进入房间
     *  * 在双方确认是触发回调
     * @param: msg
     * @param: internalProto
     * @Return com.vdegree.february.im.common.constant.type.ErrorEnum
     * @Exception
     **/
    @IMCMDRouting(cmd = IMCMD.REQUEST_CONFIRM_ENTER_ROOM)
    public ErrorEnum confirmEnterRoomHandle(ConfirmEntryRoomRequestMsg msg, InternalProto internalProto) {
        userDataRedisManger.putRoomId(internalProto.getSendUserId(),msg.getRoomId());
        if(roomDataRedisManger.getSendUserId(msg.getRoomId()).compareTo(internalProto.getSendUserId())==0){
            roomDataRedisManger.incConfirmSendUserCount(msg.getRoomId());
            if(roomDataRedisManger.incConfirminvitedUserCount(msg.getRoomId())>1){
                // TODO 双方都确认 可以发起回调
            }
        }else if(roomDataRedisManger.getInvitedUserId(msg.getRoomId()).compareTo(internalProto.getSendUserId())==0){
            roomDataRedisManger.incConfirminvitedUserCount(msg.getRoomId());
            if(roomDataRedisManger.incConfirmSendUserCount(msg.getRoomId())>1){
                // TODO 双方都确认 可以发起回调
            }
        }else{
            return ErrorEnum.ILLEGAL_USER;
        }
        return ErrorEnum.SUCCESS;
    }

    /**
     * @Author DELL
     * @Date 13:47 2021/4/7
     * @Description 退出房间
     * @param: msg
     * @param: internalProto
     * @Return com.vdegree.february.im.common.constant.type.ErrorEnum
     * @Exception
     **/
    @IMCMDRouting(cmd = IMCMD.REQUEST_QUIT_ROOM)
    public ErrorEnum quitRoomHandle(QuitRoomRequestMsg msg,InternalProto internalProto){
        String roomId = msg.getRoomId();
        RoomType roomType = msg.getRoomType();
        if(!StringUtil.isNullOrEmpty(roomId)){
            if(roomId.startsWith(roomType.getRoomPrefix())) {
                try {
                    Long sendUserId = roomDataRedisManger.getSendUserId(roomId);
                    Long invitedUserId = roomDataRedisManger.getInvitedUserId(roomId);
                    ArrayList<Long> pushUserids = Lists.newArrayList(sendUserId, invitedUserId);
                    pushUserids.remove(internalProto.getSendUserId());
                    pushManager.pushProto(IMCMD.PUSH_QUIT_ROOM, new QuitRoomPushMsg(roomId, roomType), pushUserids);
                    userDataRedisManger.delRoomId(sendUserId);
                    userDataRedisManger.delRoomId(invitedUserId);
                    // 删除房间session信息
                    roomDataRedisManger.delete(roomId);
                    //TODO 回调appService 退出房间
                    return ErrorEnum.SUCCESS;
                }catch (Exception e){
                    log.error(e);
                }
            }
        }
        return ErrorEnum.PARAM_ERROR;
    }
}
