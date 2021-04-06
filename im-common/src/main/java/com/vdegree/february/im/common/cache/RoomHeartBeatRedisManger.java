package com.vdegree.february.im.common.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 房间redis 心跳管理
 * 使用 zset 记录用户心跳
 * Score 最后一次心跳时间戳
 * value 中存放RoomId
 * 不能直接访问 可以通过 ${@link RoomDataRedisManger} 访问
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

    public Set<String> findInvalidRoom(){
        Set<String> set = redisTemplate.opsForZSet().rangeByScore(ROOM_EFFECTIVE_REDIS_KEY,0,System.currentTimeMillis());
        return set;
    }

    public boolean del(String roomId){
        Long effectiveTime = redisTemplate.opsForZSet().remove(ROOM_EFFECTIVE_REDIS_KEY,roomId);
        if(effectiveTime!=null){
            return true;
        }
        return false;
    }

}
