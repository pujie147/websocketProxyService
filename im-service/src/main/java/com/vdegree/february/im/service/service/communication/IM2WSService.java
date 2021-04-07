package com.vdegree.february.im.service.service.communication;

import com.google.common.collect.Lists;
import com.vdegree.february.im.api.im2ws.message.IM2WSDisConnectedProto;
import com.vdegree.february.im.api.im2ws.message.IM2WSInitRoomHeartbeatProto;
import com.vdegree.february.im.common.constant.type.IMCMD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/4/7 14:31
 */
@Component
public class IM2WSService {
    @Autowired
    private IM2WSManager im2WSManager;

    public void initRoomHeartbeat(Long sendUserId,String roomId){
        im2WSManager.sendProto(IMCMD.IM_WP_INIT_ROOM_HEARBEAT,new IM2WSInitRoomHeartbeatProto(sendUserId,roomId));
    }

    public void disConnectedUser(Long userId){
        im2WSManager.sendProto(IMCMD.IM_WP_DIS_CONNECTED_USER,new IM2WSDisConnectedProto(userId));
    }
}
