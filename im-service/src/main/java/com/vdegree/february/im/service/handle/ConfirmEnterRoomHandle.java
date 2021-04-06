package com.vdegree.february.im.service.handle;

import com.google.gson.Gson;
import com.vdegree.february.im.api.IMCMDUp;
import com.vdegree.february.im.api.ws.InternalProto;
import com.vdegree.february.im.api.ws.RequestProto;
import com.vdegree.february.im.api.ws.message.request.ConfirmEntryRoomRequestMsg;
import com.vdegree.february.im.common.cache.RoomDataRedisManger;
import com.vdegree.february.im.common.cache.UserDataRedisManger;
import com.vdegree.february.im.common.constant.type.ErrorEnum;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.api.IMCMDRouting;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 确认进入房间
 * 在双方确认是触发回调
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 19:56
 */
@IMCMDUp
public class ConfirmEnterRoomHandle {

    @Autowired
    private UserDataRedisManger userDataRedisManger;

    @Autowired
    private RoomDataRedisManger roomDataRedisManger;

    @Autowired
    private Gson gson;

    @IMCMDRouting(cmd = IMCMD.REQUEST_CONFIRM_ENTER_ROOM)
    public ErrorEnum execute(RequestProto<ConfirmEntryRoomRequestMsg> msg, InternalProto internalProto) {
        userDataRedisManger.putRoomId(internalProto.getSendUserId(),msg.getMessage().getRoomId());
        if(roomDataRedisManger.getSendUserId(msg.getMessage().getRoomId()).compareTo(internalProto.getSendUserId())==0){
            roomDataRedisManger.incConfirmSendUserCount(msg.getMessage().getRoomId());
            if(roomDataRedisManger.incConfirminvitedUserCount(msg.getMessage().getRoomId())>1){
                // TODO 双方都确认 可以发起回调
            }
        }else if(roomDataRedisManger.getInvitedUserId(msg.getMessage().getRoomId()).compareTo(internalProto.getSendUserId())==0){
            roomDataRedisManger.incConfirminvitedUserCount(msg.getMessage().getRoomId());
            if(roomDataRedisManger.incConfirmSendUserCount(msg.getMessage().getRoomId())>1){
                // TODO 双方都确认 可以发起回调
            }
        }else{
            return ErrorEnum.ILLEGAL_USER;
        }
        return ErrorEnum.SUCCESS;
    }
}
