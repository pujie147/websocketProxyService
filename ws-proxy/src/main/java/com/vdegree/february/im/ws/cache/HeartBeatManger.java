package com.vdegree.february.im.ws.cache;

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
public class HeartBeatManger {
    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${app.ws.idle-time:60000}")
    private Long idleTime;

    private long idleTime(){
        return System.currentTimeMillis()+idleTime;
    }

    private String USER_EFFECTIVE_REDIS_KEY = "IM_USER_EFFECTIVE_TIME";

    private String USER_SESSION_DATA_REDIS_KEY = "IM_USER_SESSION_DATA";

    public void generateRedisUserEffectiveTime(long userId){
        redisTemplate.opsForZSet().add(USER_EFFECTIVE_REDIS_KEY,userId,idleTime());
    }

    public void refreshRedisUserAndRoomEffectiveTime(long userId){
        redisTemplate.opsForZSet().add(USER_EFFECTIVE_REDIS_KEY,userId,idleTime());
//        redisTemplate.opsForHash().get(USER_SESSION_DATA_REDIS_KEY,userId); // 如果用户信息类有房间id该用户在视频
        //在视频的用户刷新 房间有效时间
    }


}
