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
public class HeartBeatRedisManger {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserDataRedisManger userDataRedisManger;

    @Value("${app.ws.idle-time:60000}")
    private Long idleTime;

    private long idleTime(){
        return System.currentTimeMillis()+idleTime;
    }

    private String USER_EFFECTIVE_REDIS_KEY = "IM_USER_EFFECTIVE_TIME";

    public void generateRedisUserEffectiveTime(long userId){
        redisTemplate.opsForZSet().add(USER_EFFECTIVE_REDIS_KEY,userId,idleTime());
        userDataRedisManger.buildNewRedisData(userId,System.currentTimeMillis());
    }

    public void refreshRedisUserAndRoomEffectiveTime(long userId){
        redisTemplate.opsForZSet().add(USER_EFFECTIVE_REDIS_KEY,userId,idleTime());
        //在视频的用户刷新 房间有效时间
    }


}
