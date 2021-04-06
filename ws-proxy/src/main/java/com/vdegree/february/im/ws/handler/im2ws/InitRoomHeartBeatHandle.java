//package com.vdegree.february.im.ws.handler.im2ws;
//
//import com.vdegree.february.im.api.IMCMDRouting;
//import com.vdegree.february.im.api.im2ws.message.IM2WSInitRoomHeartbeatProto;
//import com.vdegree.february.im.api.ws.ProtoContext;
//import com.vdegree.february.im.common.constant.type.IMCMD;
//import com.vdegree.february.im.ws.cache.CacheChannelGroupManager;
//import com.vdegree.february.im.ws.handler.BaseWsProxyHandle;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//
//
///**
// * 处理房间 初始化之后 imservice 通知 ws proxy 建立心跳缓存
// *
// * @author DELL
// * @version 1.0
// * @date 2021/4/2 11:54
// */
//@IMCMDRouting(cmd= IMCMD.IM_WP_INIT_ROOM_HEARBEAT)
//@Log4j2
//public class InitRoomHeartBeatHandle implements BaseWsProxyHandle {
//    @Autowired
//    private CacheChannelGroupManager cacheChannelGroupManager;
//
//    @Override
//    public void execute(ProtoContext protoContext) {
//        if(protoContext.getInternalProto().getIm2WSProto() instanceof IM2WSInitRoomHeartbeatProto){
//            IM2WSInitRoomHeartbeatProto im2WSInitRoomHeartbeatProto = (IM2WSInitRoomHeartbeatProto) protoContext.getInternalProto().getIm2WSProto();
//            // 判断心跳用户在一台服务器
//            if(cacheChannelGroupManager.containsUserId(im2WSInitRoomHeartbeatProto.getUserId())) {
//                cacheChannelGroupManager.addRoom(im2WSInitRoomHeartbeatProto.getUserId(), im2WSInitRoomHeartbeatProto.getRoomId());
//            }
//        }else{
//            log.error("Proto fail!!");
//        }
//    }
//}
