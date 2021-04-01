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

    @Value("${ss:180000}")
    private Long effectiveTime;

    /**
     * @Author DELL
     * @Date 15:42 2021/4/1
     * @Description 用户心跳维持
     * @param: param
     * @Return com.xxl.job.core.biz.model.ReturnT<java.lang.String> 
     * @Exception 
     **/
    @VdJobHandler("IMUserHeartBeat")
    public ReturnT<String> userHeartBeat(String param) throws Exception {
        long endTime = System.currentTimeMillis() - effectiveTime;
        long startTime = endTime - effectiveTime*2;
        List<Long> userIdList = userDataRedisManger.findInvalidUser(startTime, endTime);
        userIdList.forEach(userId -> {
            publicAppServiceApi.pushDisConnected(userId);

        });
//        XxlJobLogger.log("XXL-JOB, Hello World.");
//
//        for (int i = 0; i < 5; i++) {
//            XxlJobLogger.log("beat at:" + i); // 日志输出 xxl-job-server
//            TimeUnit.SECONDS.sleep(2);
//        }
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
        long endTime = System.currentTimeMillis() - effectiveTime;
        long startTime = endTime - effectiveTime*2;
        List<String> roomIdList = roomDataRedisManger.findInvalidRoom(startTime, endTime);
        roomIdList.forEach(roomId -> {
            for (RoomType value : RoomType.values()) {
                if(value.getRoomPrefix().startsWith(roomId)) {
                    publicAppServiceApi.pushQuitRoomApi(roomId, value);
                }
            }
        });
        return ReturnT.SUCCESS;
    }
}
