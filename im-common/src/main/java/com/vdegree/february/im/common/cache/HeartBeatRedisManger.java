package com.vdegree.february.im.common.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/25 17:07
 */
@Component
class HeartBeatRedisManger {
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
    }

    public boolean refreshRedisUserEffectiveTime(long userId){
        if (isUserEffective(userId)) {
            redisTemplate.opsForZSet().add(USER_EFFECTIVE_REDIS_KEY, userId, idleTime());
            return true;
        }
        return false;
    }

    public boolean isUserEffective(long userid){
        Long effectiveTime = redisTemplate.opsForZSet().rank(USER_EFFECTIVE_REDIS_KEY,userid);
        if(System.currentTimeMillis()<=effectiveTime){
            return true;
        }
        return false;
    }
    public boolean del(Long userId){
        Long effectiveTime = redisTemplate.opsForZSet().remove(USER_EFFECTIVE_REDIS_KEY,userId);
        if(effectiveTime!=null){
            return true;
        }
        return false;
    }


    public List<Long> findInvalidUser(Long startTime, Long endTime) {
        Set set = redisTemplate.opsForZSet().range(USER_EFFECTIVE_REDIS_KEY, startTime, endTime);
        return (List<Long>) set;
    }
}
