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
    private String FIELD_SEND_USER_CONFIRM_COUNT = "confirmSendUserCount";
    private String FIELD_INVITED_USER_CONFIRM_COUNT = "confirminvitedUserCount";


    private String getKey(String roomId){
        return ROOM_SESSION_DATA_REDIS_KEY+"_"+roomId;
    }

    public void buildNewRedisData(String roomId,Long sendUserId,Long invitedUserId,Long startTime){
        HashOperations opshash = redisTemplate.opsForHash();
        opshash.put(getKey(roomId),FIELD_SEND_USER_ID,sendUserId);
        opshash.put(getKey(roomId),FIELD_INVITED_USER_ID,invitedUserId);
        opshash.put(getKey(roomId),FIELD_START_TIME,startTime);
    }

    public Long getSendUserId(String roomId){
        return (Long)redisTemplate.opsForHash().get(getKey(roomId),FIELD_SEND_USER_ID);
    }

    public Long getInvitedUserId(String roomId){
        return (Long)redisTemplate.opsForHash().get(getKey(roomId),FIELD_INVITED_USER_ID);
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

    public Long incConfirmSendUserCount(String roomId){
        return redisTemplate.opsForHash().increment(getKey(roomId),FIELD_SEND_USER_CONFIRM_COUNT,1);
    }
    public Long getConfirmSendUserCount(String roomId){
        return (Long)redisTemplate.opsForHash().get(getKey(roomId),FIELD_SEND_USER_CONFIRM_COUNT);
    }

    public Long incConfirminvitedUserCount(String roomId){
        return redisTemplate.opsForHash().increment(getKey(roomId),FIELD_INVITED_USER_CONFIRM_COUNT,1);
    }
    public Long getConfirminvitedUserCount(String roomId){
        return (Long)redisTemplate.opsForHash().get(getKey(roomId),FIELD_INVITED_USER_CONFIRM_COUNT);
    }

    public void delete(String roomId){
        redisTemplate.opsForHash().delete(getKey(roomId));
    }

}
