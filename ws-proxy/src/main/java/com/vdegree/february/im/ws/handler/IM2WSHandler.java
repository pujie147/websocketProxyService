package com.vdegree.february.im.ws.handler;

import com.vdegree.february.im.api.im2ws.message.IM2WSDisConnectedProto;
import com.vdegree.february.im.api.im2ws.message.IM2WSInitRoomHeartbeatProto;
import com.vdegree.february.im.api.ws.ProtoContext;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.common.routing.IMCMDRouting;
import com.vdegree.february.im.common.routing.IMCMDUp;
import com.vdegree.february.im.ws.cache.CacheChannelGroupManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/4/7 14:02
 */
@IMCMDUp
@Log4j2
public class IM2WSHandler {
    @Autowired
    private CacheChannelGroupManager cacheChannelGroupManager;

    @IMCMDRouting(cmd= IMCMD.IM_WP_DIS_CONNECTED_USER)
    public void disConnectedUserHandle(ProtoContext protoContext){
        if(protoContext.getInternalProto().getIm2WSProto() instanceof IM2WSDisConnectedProto){
            IM2WSDisConnectedProto im2WSProto = (IM2WSDisConnectedProto) protoContext.getInternalProto().getIm2WSProto();
            if(cacheChannelGroupManager.containsUserId(im2WSProto.getUserId())) {
                cacheChannelGroupManager.deleteUser(im2WSProto.getUserId());
            }
        }else{
            log.error("Proto fail!!");
        }
    }

    @IMCMDRouting(cmd= IMCMD.IM_WP_INIT_ROOM_HEARBEAT)
    public void initRoomHeartBeatHandle(ProtoContext protoContext){
        if(protoContext.getInternalProto().getIm2WSProto() instanceof IM2WSInitRoomHeartbeatProto){
            IM2WSInitRoomHeartbeatProto im2WSInitRoomHeartbeatProto = (IM2WSInitRoomHeartbeatProto) protoContext.getInternalProto().getIm2WSProto();
            // 判断心跳用户在一台服务器
            if(cacheChannelGroupManager.containsUserId(im2WSInitRoomHeartbeatProto.getUserId())) {
                cacheChannelGroupManager.addRoom(im2WSInitRoomHeartbeatProto.getUserId(), im2WSInitRoomHeartbeatProto.getRoomId());
            }
        }else{
            log.error("Proto fail!!");
        }
    }
}
