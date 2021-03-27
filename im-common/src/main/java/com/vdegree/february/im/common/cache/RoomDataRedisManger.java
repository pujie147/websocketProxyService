package com.vdegree.february.im.common.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
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
public class RoomDataRedisManger {
    @Autowired
    private RedisTemplate redisTemplate;

    private String ROOM_SESSION_DATA_REDIS_KEY = "IM_ROOM_SESSION_DATA";

    private String FIELD_SEND_USER_ID = "sendUserId";
    private String FIELD_INVITED_USER_ID = "invitedUserId";
    private String FIELD_START_TIME = "startTime";
    private String FIELD_CONFIRM_TIME = "confirmTime";
    private String FIELD_CONFIRM_COUNT = "confirmCount";


    private String getKey(String roomId){
        return ROOM_SESSION_DATA_REDIS_KEY+"_"+roomId;
    }

    public void buildNewRedisData(String roomId,Long sendUserId,Long invitedUserId,Long startTime){
        HashOperations opshash = redisTemplate.opsForHash();
        opshash.put(getKey(roomId),FIELD_SEND_USER_ID,sendUserId);
        opshash.put(getKey(roomId),FIELD_INVITED_USER_ID,invitedUserId);
        opshash.put(getKey(roomId),FIELD_START_TIME,startTime);
    }

    public String getSendUserId(String roomId){
        return (String)redisTemplate.opsForHash().get(getKey(roomId),FIELD_SEND_USER_ID);
    }

    public String getInvitedUserId(String roomId){
        return (String)redisTemplate.opsForHash().get(getKey(roomId),FIELD_INVITED_USER_ID);
    }

    public String getStartTime(String roomId){
        return (String)redisTemplate.opsForHash().get(getKey(roomId),FIELD_START_TIME);
    }

    public void putConfirmTime(String roomId,Long confirmTime){
        redisTemplate.opsForHash().put(getKey(roomId),FIELD_CONFIRM_TIME,confirmTime);
    }
    public Long getConfirmTime(String roomId){
        return (Long)redisTemplate.opsForHash().get(getKey(roomId),FIELD_CONFIRM_TIME);
    }

    public void putConfirmCount(String roomId,Long confirmCount){
        redisTemplate.opsForHash().put(getKey(roomId),FIELD_CONFIRM_COUNT,confirmCount);
    }
    public Long getConfirmCount(String roomId){
        return (Long)redisTemplate.opsForHash().get(getKey(roomId),FIELD_CONFIRM_COUNT);
    }

    public void delete(String roomId){
        redisTemplate.opsForHash().delete(getKey(roomId));
    }

}
