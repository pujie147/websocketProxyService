package com.vdegree.february.im.service.handle;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.vdegree.february.im.api.ws.ProtoContext;
import com.vdegree.february.im.api.ws.RequestProto;
import com.vdegree.february.im.api.ws.message.request.ConfirmEntryRoomRequestMsg;
import com.vdegree.february.im.common.cache.RoomDataRedisManger;
import com.vdegree.february.im.common.cache.UserDataRedisManger;
import com.vdegree.february.im.common.constant.type.ErrorEnum;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.api.IMCMDRouting;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 1.接收客户端发起的聊天邀请
 * 2.回调appservice
 * 4.return
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 19:56
 */
@IMCMDRouting(cmd = IMCMD.REQUEST_CONFIRM_ENTER_ROOM)
public class ConfirmEnterRoomHandle implements BaseImServiceHandle {

    @Autowired
    private UserDataRedisManger userDataRedisManger;

    @Autowired
    private RoomDataRedisManger roomDataRedisManger;

    @Autowired
    private Gson gson;

    @Override
    public ProtoContext execute(ProtoContext protoContext) {
        RequestProto<ConfirmEntryRoomRequestMsg> msg = gson.fromJson(protoContext.getJson(), new TypeToken<RequestProto<ConfirmEntryRoomRequestMsg>>(){}.getType());
        userDataRedisManger.putRoomId(protoContext.getInternalProto().getSendUserId(),msg.getMessage().getRoomId());
        if(roomDataRedisManger.getSendUserId(msg.getMessage().getRoomId()).compareTo(protoContext.getInternalProto().getSendUserId())==0){
            roomDataRedisManger.incConfirmSendUserCount(msg.getMessage().getRoomId());
            if(roomDataRedisManger.incConfirminvitedUserCount(msg.getMessage().getRoomId())>1){
                // TODO 双方都确认 可以发起回调
            }
        }else if(roomDataRedisManger.getInvitedUserId(msg.getMessage().getRoomId()).compareTo(protoContext.getInternalProto().getSendUserId())==0){
            roomDataRedisManger.incConfirminvitedUserCount(msg.getMessage().getRoomId());
            if(roomDataRedisManger.incConfirmSendUserCount(msg.getMessage().getRoomId())>1){
                // TODO 双方都确认 可以发起回调
            }
        }else{
            protoContext.buildFailResponseProto(ErrorEnum.ILLEGAL_USER);
        }
        return protoContext.buildSuccessResponseProto();
    }
}
