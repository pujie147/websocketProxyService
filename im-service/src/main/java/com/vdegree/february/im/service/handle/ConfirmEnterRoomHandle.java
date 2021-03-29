package com.vdegree.february.im.service.handle;

import com.google.gson.Gson;
import com.vdegree.february.im.api.ws.message.request.ConfirmEntryRoomRequestMsg;
import com.vdegree.february.im.common.cache.RoomDataRedisManger;
import com.vdegree.february.im.common.cache.UserDataRedisManger;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.api.IMController;
import com.vdegree.february.im.api.ws.ResponseProto;
import com.vdegree.february.im.api.ws.RequestProto;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 1.接收客户端发起的聊天邀请
 * 2.回调appservice
 * 4.return
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 19:56
 */
@IMController(cmd = IMCMD.REQUEST_CONFIRM_ENTER_ROOM)
public class ConfirmEnterRoomHandle implements BaseImServiceHandle {

    @Autowired
    private UserDataRedisManger userDataRedisManger;

    @Autowired
    private RoomDataRedisManger roomDataRedisManger;

    @Autowired
    private Gson gson;

    @Override
    public ResponseProto execute(RequestProto requestProto) {
        ConfirmEntryRoomRequestMsg msg = gson.fromJson(gson.toJson(requestProto.getJson()), ConfirmEntryRoomRequestMsg.class);
        userDataRedisManger.putRoomId(requestProto.getSendUserId(),msg.getRoomId());
        if(roomDataRedisManger.getSendUserId(msg.getRoomId()).compareTo(requestProto.getSendUserId())==0){
            roomDataRedisManger.incConfirmSendUserCount(msg.getRoomId());
            if(roomDataRedisManger.incConfirminvitedUserCount(msg.getRoomId())>0){
                // TODO 双方都确认 可以发起回调
            }
        }else if(roomDataRedisManger.getInvitedUserId(msg.getRoomId()).compareTo(requestProto.getSendUserId())==0){
            roomDataRedisManger.incConfirminvitedUserCount(msg.getRoomId());
            if(roomDataRedisManger.incConfirmSendUserCount(msg.getRoomId())>0){
                // TODO 双方都确认 可以发起回调
            }
        }else{
//            return TODO 返回异常 用户不是该房间成员
        }
        return ResponseProto.buildResponse(requestProto);
    }
}
