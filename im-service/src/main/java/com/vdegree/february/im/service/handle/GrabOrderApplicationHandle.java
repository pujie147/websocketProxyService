package com.vdegree.february.im.service.handle;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.vdegree.february.im.api.IMCMDRouting;
import com.vdegree.february.im.api.ws.*;
import com.vdegree.february.im.api.ws.message.push.EnterRoomPushMsg;
import com.vdegree.february.im.api.ws.message.request.GrabOrderApplicationRequestMsg;
import com.vdegree.february.im.common.cache.GrabOrderRedisManger;
import com.vdegree.february.im.common.cache.UserDataRedisManger;
import com.vdegree.february.im.common.constant.type.ErrorEnum;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.common.utils.RoomIdGenerateUtil;
import com.vdegree.february.im.common.utils.agora.RtcTokenBuilderUtil;
import com.vdegree.february.im.service.communication.PushManager;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 19:56
 */
@IMCMDRouting(cmd = IMCMD.REQUEST_GRAB_ORDER_APPLICATION)
public class GrabOrderApplicationHandle implements BaseImServiceHandle {

    @Autowired
    private GrabOrderRedisManger grabOrderRedisManger;

    @Autowired
    private Gson gson;

    @Autowired
    private RtcTokenBuilderUtil rtcTokenBuilderUtil;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UserDataRedisManger userDataRedisManger;

    @Autowired
    private PushManager pushManager;


    @Override
    public ProtoContext execute(ProtoContext protoContext) {
        RequestProto<GrabOrderApplicationRequestMsg> msg = gson.fromJson(protoContext.getJson(),new TypeToken<RequestProto<GrabOrderApplicationRequestMsg>>(){}.getType());
        Long count = grabOrderRedisManger.incGrabOrderPersonCount(msg.getMessage().getSendUserId(), msg.getMessage().getEnterRoomCode());
        if(count!=null && count<=1){
            Long sendUserId = msg.getMessage().getSendUserId();
            Long invitedUserId = protoContext.getInternalProto().getSendUserId();
            String roomId = RoomIdGenerateUtil.generate(sendUserId, invitedUserId, msg.getMessage().getRoomType());
            EnterRoomPushMsg pushMsg = new EnterRoomPushMsg();
            pushMsg.setRoomType(msg.getMessage().getRoomType());
            pushMsg.setRoomId(roomId);

            // 邀请人
            String token = rtcTokenBuilderUtil.build(sendUserId.intValue(), roomId);
            pushMsg.setToken(token);
            pushManager.pushProto(IMCMD.PUSH_ENTER_ROOM, pushMsg, Lists.newArrayList(sendUserId));
            userDataRedisManger.putRoomId(sendUserId,roomId);

            // 被邀请人
            token = rtcTokenBuilderUtil.build(invitedUserId.intValue(), roomId);
            pushMsg.setToken(token);
            pushManager.pushProto(IMCMD.PUSH_ENTER_ROOM, pushMsg, Lists.newArrayList(invitedUserId));

            return protoContext.buildSuccessResponseProto();
        }
        return protoContext.buildFailResponseProto(ErrorEnum.GRAB_ORDER_END);
    }
}
