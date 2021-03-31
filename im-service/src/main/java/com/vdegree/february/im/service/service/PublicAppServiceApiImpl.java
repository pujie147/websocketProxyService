package com.vdegree.february.im.service.service;

import com.google.common.collect.Lists;
import com.vdegree.february.im.api.rpc.PublicAppServiceApi;
import com.vdegree.february.im.api.ws.message.push.GrabOrderInvitationPushMsg;
import com.vdegree.february.im.api.ws.message.push.QuitRoomPushMsg;
import com.vdegree.february.im.common.cache.GrabOrderRedisManger;
import com.vdegree.february.im.common.cache.RoomDataRedisManger;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.common.constant.type.RoomType;
import com.vdegree.february.im.common.utils.RoomIdGenerateUtil;
import com.vdegree.february.im.service.PushManager;
import io.netty.util.internal.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Random;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/30 11:53
 */
@Service
@Log4j2
public class PublicAppServiceApiImpl implements PublicAppServiceApi {
    @Autowired
    private GrabOrderRedisManger grabOrderRedisManger;

    @Value("${ws.service.grab-order.effective-time}")
    private Long grabOrderEffectiveTime;

    @Autowired
    private PushManager pushManager;
    @Autowired
    private RoomDataRedisManger roomDataRedisManger;

    @Override
    public boolean sendGrabOrderApi(Long sendUserId, List<Long> invitationUserIds, RoomType roomType) {
        try {
            int enterRoomCode = new Random().nextInt(6000);
            GrabOrderInvitationPushMsg grabOrderInvitationPushMsg = new GrabOrderInvitationPushMsg();
            grabOrderInvitationPushMsg.setRoomType(roomType);
            grabOrderInvitationPushMsg.setSendUserId(sendUserId);
            grabOrderInvitationPushMsg.setEnterRoomCode(enterRoomCode);
            grabOrderRedisManger.buildNewRedisData(sendUserId, enterRoomCode, grabOrderEffectiveTime);
            pushManager.pushProto(IMCMD.PUSH_GRAB_ORDER_INVITATION, grabOrderInvitationPushMsg, invitationUserIds);
        }catch (Exception e){
            log.error(e);
            return false;
        }
        return true;
    }

    @Override
    public boolean pushQuitRoomApi(String roomId, RoomType roomType){
        if(!StringUtil.isNullOrEmpty(roomId)){
            if(roomId.startsWith(roomType.getRoomPrefix())) {
                try {
                    Long sendUserId = roomDataRedisManger.getSendUserId(roomId);
                    Long invitedUserId = roomDataRedisManger.getInvitedUserId(roomId);
                    pushManager.pushProto(IMCMD.PUSH_QUIT_ROOM, new QuitRoomPushMsg(roomId, roomType), Lists.newArrayList(sendUserId, invitedUserId));
                }catch (Exception e){
                    return false;
                }
            }
        }
        return false;
    }


}
