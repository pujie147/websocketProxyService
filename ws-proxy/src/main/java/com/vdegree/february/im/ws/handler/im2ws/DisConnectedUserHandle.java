//package com.vdegree.february.im.ws.handler.im2ws;
//
//import com.vdegree.february.im.api.IMCMDRouting;
//import com.vdegree.february.im.api.im2ws.message.IM2WSDisConnectedProto;
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
// * 断开用户连接
// *
// * @author DELL
// * @version 1.0
// * @date 2021/4/2 11:54
// */
//@IMCMDRouting(cmd= IMCMD.IM_WP_DIS_CONNECTED_USER)
//@Log4j2
//public class DisConnectedUserHandle implements BaseWsProxyHandle {
//    @Autowired
//    private CacheChannelGroupManager cacheChannelGroupManager;
//
//    @Override
//    public void execute(ProtoContext protoContext) {
//        if(protoContext.getInternalProto().getIm2WSProto() instanceof IM2WSDisConnectedProto){
//            IM2WSDisConnectedProto im2WSProto = (IM2WSDisConnectedProto) protoContext.getInternalProto().getIm2WSProto();
//            if(cacheChannelGroupManager.containsUserId(im2WSProto.getUserId())) {
//                cacheChannelGroupManager.deleteUser(im2WSProto.getUserId());
//            }
//        }else{
//            log.error("Proto fail!!");
//        }
//    }
//}
