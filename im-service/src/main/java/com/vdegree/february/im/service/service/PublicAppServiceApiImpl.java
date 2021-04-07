package com.vdegree.february.im.service.service;

import com.vdegree.february.im.api.rpc.PublicAppServiceApi;
import com.vdegree.february.im.common.cache.GrabOrderRedisManger;
import com.vdegree.february.im.common.cache.RoomDataRedisManger;
import com.vdegree.february.im.common.cache.UserDataRedisManger;
import com.vdegree.february.im.common.constant.type.ErrorEnum;
import com.vdegree.february.im.common.constant.type.RoomType;
import com.vdegree.february.im.service.service.communication.IM2WSService;
import com.vdegree.february.im.service.service.communication.PushService;
import io.netty.util.internal.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * 暴露给 appservice 服务调用的rpc服务
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
    private PushService pushService;
    @Autowired
    private RoomDataRedisManger roomDataRedisManger;
    @Autowired
    private UserDataRedisManger userDataRedisManger;

    @Autowired
    private IM2WSService im2WSService;

    @Override
    public boolean sendGrabOrderApi(Long sendUserId, List<Long> invitationUserIds, RoomType roomType) {
        try {
            int enterRoomCode = new Random().nextInt(6000);
            pushService.grabOrderInvitation(sendUserId,enterRoomCode,roomType,invitationUserIds);
        }catch (Exception e){
            log.error(e);
            return false;
        }
        return true;
    }

    /**
     * @Author DELL
     * @Date 11:56 2021/4/6
     * @Description 退出房间处理操作
     * 1、会删除session 中的房间号缓存
     * 2、发布广播告知房间用户房间关闭
     * 3、删除房间 session信息
     * @param: roomId
     * @param: roomType
     * @Return boolean 
     * @Exception 
     **/
    @Override
    public boolean pushQuitRoomApi(String roomId, RoomType roomType){
        if(!StringUtil.isNullOrEmpty(roomId)){
            if(roomId.startsWith(roomType.getRoomPrefix())) {
                try {
                    Long sendUserId = roomDataRedisManger.getSendUserId(roomId);
                    Long invitedUserId = roomDataRedisManger.getInvitedUserId(roomId);
//              TODO 缺少关闭房间的push请求      pushService.quitRoom(roomId,roomType,sendUserId,invitedUserId);
                    userDataRedisManger.delRoomId(sendUserId);
                    userDataRedisManger.delRoomId(invitedUserId);
                    roomDataRedisManger.delete(roomId);
                    //TODO 回调 appservice 退出房间
                    return true;
                }catch (Exception e){
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * @Author DELL
     * @Date 11:55 2021/4/6
     * @Description 断开连接会处理操作
     * 1、查询是否有存活房间，有存活房间
     * 存活的房间
     * @param: userId
     * @Return boolean
     * @Exception
     **/
    @Override
    public boolean pushDisConnected(Long userId){
        String roomId = userDataRedisManger.getRoomId(userId);
        if(roomId!=null) {
            Long invitedUserId = roomDataRedisManger.getInvitedUserId(roomId);
            Long sendUserId = roomDataRedisManger.getSendUserId(roomId);
            if (userId.compareTo(invitedUserId) == 0) {
                pushService.serviceEvent(ErrorEnum.ROOM_FAILURE,sendUserId);
                pushService.serviceEvent(ErrorEnum.DIS_CONNECTED_REQUEST_ERROR,invitedUserId);
                userDataRedisManger.delRoomId(sendUserId);
                roomDataRedisManger.delete(roomId);
            } else if (userId.compareTo(sendUserId) == 0) {
                pushService.serviceEvent(ErrorEnum.ROOM_FAILURE,invitedUserId);
                pushService.serviceEvent(ErrorEnum.DIS_CONNECTED_REQUEST_ERROR,sendUserId);
                userDataRedisManger.delRoomId(invitedUserId);
                roomDataRedisManger.delete(roomId);
            }
        }
        userDataRedisManger.del(userId);
        im2WSService.disConnectedUser(userId);
        //TODO 回调 appservice 离线用户
        return true;
    }


}
