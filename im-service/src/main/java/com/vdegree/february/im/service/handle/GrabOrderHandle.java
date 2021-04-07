package com.vdegree.february.im.service.handle;

import com.google.common.collect.Lists;
import com.vdegree.february.im.common.routing.IMCMDRouting;
import com.vdegree.february.im.common.routing.IMCMDUp;
import com.vdegree.february.im.api.ws.InternalProto;
import com.vdegree.february.im.api.ws.message.push.EnterRoomPushMsg;
import com.vdegree.february.im.api.ws.message.request.GrabOrderApplicationRequestMsg;
import com.vdegree.february.im.common.cache.GrabOrderRedisManger;
import com.vdegree.february.im.common.cache.UserDataRedisManger;
import com.vdegree.february.im.common.constant.type.ErrorEnum;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.common.utils.agora.RtcTokenBuilderUtil;
import com.vdegree.february.im.service.communication.PushManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 抢入房邀请单
 * 处理
 *
 * @author DELL
 * @version 1.0
 * @date 2021/4/7 10:28
 */
@IMCMDUp
public class GrabOrderHandle {
    @Autowired
    private GrabOrderRedisManger grabOrderRedisManger;

    @Autowired
    private RtcTokenBuilderUtil rtcTokenBuilderUtil;

    @Autowired
    private UserDataRedisManger userDataRedisManger;

    @Autowired
    private PushManager pushManager;

    /**
     * @Author DELL
     * @Date 10:32 2021/4/7
     * @Description
     * 抢单处理 只有第一个人才能抢单
     * 其他人返回 抢单结束
     * @param: msg
     * @param: internalProto
     * @Return com.vdegree.february.im.common.constant.type.ErrorEnum
     * @Exception
     **/
    @IMCMDRouting(cmd = IMCMD.REQUEST_GRAB_ORDER_APPLICATION)
    public ErrorEnum grabOrderApplication(GrabOrderApplicationRequestMsg msg, InternalProto internalProto){
        Long count = grabOrderRedisManger.incGrabOrderPersonCount(msg.getSendUserId(), msg.getEnterRoomCode());
        if(count!=null && count<=1){
            Long sendUserId = msg.getSendUserId();
            Long invitedUserId = internalProto.getSendUserId();
            String roomId = msg.getRoomType().generate(sendUserId, invitedUserId);
            EnterRoomPushMsg pushMsg = new EnterRoomPushMsg();
            pushMsg.setRoomType(msg.getRoomType());
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

            return ErrorEnum.SUCCESS;
        }
        return ErrorEnum.GRAB_ORDER_END;
    }
}
