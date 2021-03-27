package com.vdegree.february.im.common.cache;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/25 17:07
 */
@Component
public class RoomHeartBeatRedisManger {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RoomDataRedisManger roomDataRedisManger;

    @Value("${app.ws.idle-time:60000}")
    private Long idleTime;

    private long idleTime(){
        return System.currentTimeMillis()+idleTime;
    }

    private String USER_EFFECTIVE_REDIS_KEY = "IM_ROOM_EFFECTIVE_TIME";

    public void generateRedisUserEffectiveTime(String roomId,Long sendUserId,Long invitedUserId){
        redisTemplate.opsForZSet().add(USER_EFFECTIVE_REDIS_KEY,roomId,idleTime());
        roomDataRedisManger.buildNewRedisData(roomId,sendUserId,invitedUserId,System.currentTimeMillis());
    }

    public void refreshRedisUserAndRoomEffectiveTime(String roomId){
        redisTemplate.opsForZSet().add(USER_EFFECTIVE_REDIS_KEY,roomId,idleTime());
//        redisTemplate.opsForHash().get(USER_SESSION_DATA_REDIS_KEY,userId); // 如果用户信息类有房间id该用户在视频
        //在视频的用户刷新 房间有效时间
    }


}
