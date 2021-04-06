package com.vdegree.february.im.service.job;

import com.vdegree.february.im.api.rpc.PublicAppServiceApi;
import com.vdegree.february.im.common.cache.RoomDataRedisManger;
import com.vdegree.february.im.common.cache.UserDataRedisManger;
import com.vdegree.february.im.common.constant.type.RoomType;
import com.vdpub.crontab.constant.VdJobHandler;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;


/**
 * 心跳维持
 *
 * @author DELL
 * @version 1.0
 * @date 2021/4/1 15:38
 */
@Component
public class HeartBeatJob {
    @Autowired
    private UserDataRedisManger userDataRedisManger;

    @Autowired
    private RoomDataRedisManger roomDataRedisManger;

    @Autowired
    private PublicAppServiceApi publicAppServiceApi;

    @Value("${ws.service.heartBeat.effective-time}")
    private Long effectiveTime;

    /**
     * @Author DELL
     * @Date 15:42 2021/4/1
     * @Description 用户心跳维持
     * @param: param
     * @Return com.xxl.job.core.biz.model.ReturnT<java.lang.String> 
     * @Exception
     *
     **/
    @VdJobHandler("IMUserHeartBeat")
    public ReturnT<String> userHeartBeat(String param) throws Exception {
        Set<Long> userIdList = userDataRedisManger.findInvalidUser();
        XxlJobLogger.log("find user Heartbeat time out count:{}",userIdList.size());
        XxlJobLogger.log("------------------------------handler start----------------------------");
        userIdList.forEach(userId -> {
            XxlJobLogger.log("handler user Heartbeat outTime userId:{}",userId);
            publicAppServiceApi.pushDisConnected(userId);
        });
        XxlJobLogger.log("------------------------------handler start----------------------------");
        return ReturnT.SUCCESS;
    }

    /**
     * @Author DELL
     * @Date 15:41 2021/4/1
     * @Description 房间心跳维持
     * @param: param
     * @Return com.xxl.job.core.biz.model.ReturnT<java.lang.String> 
     * @Exception 
     **/
    @VdJobHandler("IMRoomHeartBeat")
    public ReturnT<String> roomHeartBeat(String param) throws Exception {
        Set<String> roomIdList = roomDataRedisManger.findInvalidRoom();
        XxlJobLogger.log("find room Heartbeat time out count:{} ",roomIdList.size());
        roomIdList.forEach(roomId -> {
            RoomType type = RoomType.getRoomPrefixByRoomId(roomId);
            XxlJobLogger.log("handler room Heartbeat time out roomId:{}",roomId);
            if(type!=null){
                publicAppServiceApi.pushQuitRoomApi(roomId,type);
            }
        });
        return ReturnT.SUCCESS;
    }
}
