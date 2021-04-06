//package com.vdegree.february.im.service.handle;
//
//import com.google.common.collect.Lists;
//import com.google.common.reflect.TypeToken;
//import com.google.gson.Gson;
//import com.vdegree.february.im.api.ws.*;
//import com.vdegree.february.im.api.ws.message.push.InvitedUserEnterRoomPushMsg;
//import com.vdegree.february.im.api.ws.message.request.InvitedUserEnterRoomRequestMsg;
//import com.vdegree.february.im.common.constant.type.IMCMD;
//import com.vdegree.february.im.api.IMCMDRouting;
//import com.vdegree.february.im.service.communication.PushManager;
//import org.springframework.beans.factory.annotation.Autowired;
//
///**
// * 邀请用户进入房间
// *
// * @author DELL
// * @version 1.0
// * @date 2021/3/26 19:56
// */
//@IMCMDRouting(cmd = IMCMD.REQUEST_INVITED_USER_ENTER_ROOM)
//public class InvitedUserEnterRoomHandle implements BaseImServiceHandle {
//
//    @Autowired
//    private PushManager pushManager;
//    @Autowired
//    private Gson gson;
//
//    @Override
//    public ProtoContext execute(ProtoContext protoContext) {
//        RequestProto<InvitedUserEnterRoomRequestMsg> invitedUserEnterRoomRequestMsg = gson.fromJson(protoContext.getJson(),new TypeToken<RequestProto<InvitedUserEnterRoomRequestMsg>>(){}.getType());
//        // TODO 回调 appservice 是否能发起邀请
//        InvitedUserEnterRoomPushMsg pushMsg = new InvitedUserEnterRoomPushMsg();
//        pushMsg.setRoomType(invitedUserEnterRoomRequestMsg.getMessage().getRoomType());
//        pushMsg.setSendUserId(protoContext.getInternalProto().getSendUserId());
//        pushManager.pushProto(IMCMD.PUSH_INVITED_USER_ENTER_ROOM,pushMsg,Lists.newArrayList(invitedUserEnterRoomRequestMsg.getMessage().getInvitedUserId()));
//        return protoContext.buildSuccessResponseProto();
//    }
//
//}
