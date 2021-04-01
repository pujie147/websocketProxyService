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
class RoomHeartBeatRedisManger {
    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${app.ws.idle-time:60000}")
    private Long idleTime;

    private long idleTime(){
        return System.currentTimeMillis()+idleTime;
    }

    private String ROOM_EFFECTIVE_REDIS_KEY = "IM_ROOM_EFFECTIVE_TIME";

    public void generateRedisUserEffectiveTime(String roomId){
        redisTemplate.opsForZSet().add(ROOM_EFFECTIVE_REDIS_KEY,roomId,idleTime());
    }

    public boolean refreshRedisRoomEffectiveTime(String roomId){
        if(isUserEffective(roomId)) {
            redisTemplate.opsForZSet().add(ROOM_EFFECTIVE_REDIS_KEY, roomId, idleTime());
            return true;
        }
        return false;
    }

    public boolean isUserEffective(String roomId){
        Long effectiveTime = redisTemplate.opsForZSet().rank(ROOM_EFFECTIVE_REDIS_KEY,roomId);
        if(System.currentTimeMillis()<=effectiveTime){
            return true;
        }
        return false;
    }

    public List<String> findInvalidRoom(Long start,Long end){
        Set set = redisTemplate.opsForZSet().range(ROOM_EFFECTIVE_REDIS_KEY, start, end);
        return (List<String>) set;
    }

    public boolean del(String roomId){
        Long effectiveTime = redisTemplate.opsForZSet().remove(ROOM_EFFECTIVE_REDIS_KEY,roomId);
        if(effectiveTime!=null){
            return true;
        }
        return false;
    }

}
