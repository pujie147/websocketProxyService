package com.vdegree.february.im.common.cache;

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
public class UserDataRedisManger {
    @Autowired
    private RedisTemplate redisTemplate;

    private String USER_SESSION_DATA_REDIS_KEY = "IM_USER_SESSION_DATA";

    private String FIELD_CONNECT_START_TIME = "connectStartTime";
    private String FIELD_ROOM_ID = "roomId";

    private String getKey(Long userId){
        return USER_SESSION_DATA_REDIS_KEY+"_"+userId;
    }

    public void buildNewRedisData(Long userId,Long connectStartTime){
        redisTemplate.opsForHash().put(getKey(userId),FIELD_CONNECT_START_TIME,connectStartTime);
    }

    public Long getConnectStartTime(Long userId){
        return (Long)redisTemplate.opsForHash().get(getKey(userId),FIELD_CONNECT_START_TIME);
    }

    public void putRoomId(Long userId,String roomId){
        redisTemplate.opsForHash().put(getKey(userId),FIELD_ROOM_ID,roomId);
    }
    public String getRoomId(Long userId){
        return (String)redisTemplate.opsForHash().get(getKey(userId),FIELD_ROOM_ID);
    }

    public void delRoomId(Long userId){
        redisTemplate.opsForHash().delete(getKey(userId),FIELD_ROOM_ID);
    }

}
