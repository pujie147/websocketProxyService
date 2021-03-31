//package com.vdegree.february.im.service.handle;
//
//import com.google.gson.Gson;
//import com.vdegree.february.im.api.IMController;
//import com.vdegree.february.im.api.ws.*;
//import com.vdegree.february.im.common.cache.GrabOrderRedisManger;
//import com.vdegree.february.im.common.cache.UserDataRedisManger;
//import com.vdegree.february.im.common.constant.type.IMCMD;
//import com.vdegree.february.im.common.utils.agora.RtcTokenBuilderUtil;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//
///**
// * TODO
// * @author DELL
// * @version 1.0
// * @date 2021/3/26 19:56
// */
//@IMController(cmd = IMCMD.REQUEST_GRAB_ORDER_APPLICATION)
//public class GrabOrderApplicationHandle implements BaseImServiceHandle {
//
//    @Autowired
//    private GrabOrderRedisManger grabOrderRedisManger;
//
//    @Autowired
//    private Gson gson;
//
//    @Autowired
//    private RtcTokenBuilderUtil rtcTokenBuilderUtil;
//
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    @Autowired
//    private UserDataRedisManger userDataRedisManger;
//
//
//    @Override
//    public ProtoContext execute(ProtoContext protoContext) {
////        RequestProto<GrabOrderApplicationRequestMsg> msg = gson.fromJson(requestProto.getJson(),new TypeToken<RequestProto<GrabOrderApplicationRequestMsg>>(){}.getType());
////        Long count = grabOrderRedisManger.incGrabOrderPersonCount(msg.getMessage().getSendUserId(), msg.getMessage().getEnterRoomCode());
////        if(count!=null && count<=1){
////            Long sendUserId = msg.getMessage().getSendUserId();
////            Long invitedUserId = requestProto.getSendUserId();
////            String roomId = RoomIdGenerateUtil.generate(sendUserId, invitedUserId, msg.getMessage().getRoomType());
////            EnterRoomPushMsg pushMsg = new EnterRoomPushMsg();
////            pushMsg.setRoomType(msg.getMessage().getRoomType());
////            pushMsg.setRoomId(roomId);
////
////            // 邀请人
////            String token = rtcTokenBuilderUtil.build(sendUserId.intValue(), roomId);
////            pushMsg.setToken(token);
////            BaseProto pushProto = PushProto.buildPush(IMCMD.PUSH_ENTER_ROOM, gson.toJson(pushMsg), PushType.PUSH_CONTAIN_USER, Lists.newArrayList(sendUserId));
////            rabbitTemplate.convertAndSend(WSPorxyBroadcastConstant.EXCHANGE_NAME,null,pushProto);
////            userDataRedisManger.putRoomId(sendUserId,roomId);
////
////            // 被邀请人
////            token = rtcTokenBuilderUtil.build(invitedUserId.intValue(), roomId);
////            pushMsg.setToken(token);
////            pushProto = PushProto.buildPush(IMCMD.PUSH_ENTER_ROOM, gson.toJson(pushMsg),PushType.PUSH_CONTAIN_USER,Lists.newArrayList(invitedUserId));
////            rabbitTemplate.convertAndSend(WSPorxyBroadcastConstant.EXCHANGE_NAME,null,pushProto);
////
////            return ResponseProto.buildResponse(requestProto);
////        }
////        return ResponseProto.buildResponse(requestProto, ErrorEnum.GRAB_ORDER_END);
//        return null;
//    }
//}
