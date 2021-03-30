package com.vdegree.february.im.service.service;

import com.vdegree.february.im.api.rpc.PublicAppServiceApi;
import com.vdegree.february.im.api.ws.PushProto;
import com.vdegree.february.im.api.ws.message.push.GrabOrderInvitationPushMsg;
import com.vdegree.february.im.common.constant.WSPorxyBroadcastConstant;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.common.constant.type.PushType;
import com.vdegree.february.im.common.constant.type.RoomType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/30 11:53
 */
@Service
public class PublicAppServiceApiImpl implements PublicAppServiceApi {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendGrabOrderApi(Long sendUserId, List<Long> invitationUserIds, RoomType roomType) {
        GrabOrderInvitationPushMsg grabOrderInvitationPushMsg = new GrabOrderInvitationPushMsg();
        grabOrderInvitationPushMsg.setRoomType(roomType);
        grabOrderInvitationPushMsg.setSendUserId(sendUserId);
        PushProto pushProto = PushProto.buildPush(IMCMD.PUSH_GRAB_ORDER_INVITATION, grabOrderInvitationPushMsg, PushType.PUSH_CONTAIN_USER, invitationUserIds);
        rabbitTemplate.convertAndSend(WSPorxyBroadcastConstant.EXCHANGE_NAME,null,pushProto);
    }
}
