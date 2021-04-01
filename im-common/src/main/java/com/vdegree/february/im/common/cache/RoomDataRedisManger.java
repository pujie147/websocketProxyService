package com.vdegree.february.im.common.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

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

    @Autowired
    private RoomHeartBeatRedisManger roomHeartBeatRedisManger;

    private String getKey(String roomId){
        return ROOM_SESSION_DATA_REDIS_KEY+"_"+roomId;
    }

    public void buildNewRedisData(String roomId,Long sendUserId,Long invitedUserId){
        HashOperations opshash = redisTemplate.opsForHash();
        opshash.put(getKey(roomId),FIELD_SEND_USER_ID,sendUserId);
        opshash.put(getKey(roomId),FIELD_INVITED_USER_ID,invitedUserId);
        opshash.put(getKey(roomId),FIELD_START_TIME,System.currentTimeMillis());
        roomHeartBeatRedisManger.generateRedisUserEffectiveTime(roomId);
    }

    private String FIELD_SEND_USER_ID = "sendUserId";
    public Long getSendUserId(String roomId){
        Object obj = redisTemplate.opsForHash().get(getKey(roomId), FIELD_SEND_USER_ID);
        if(obj instanceof Integer){
            return ((Integer) redisTemplate.opsForHash().get(getKey(roomId), FIELD_SEND_USER_ID)).longValue();
        }
        return (Long)redisTemplate.opsForHash().get(getKey(roomId),FIELD_SEND_USER_ID);
    }

    private String FIELD_INVITED_USER_ID = "invitedUserId";
    public Long getInvitedUserId(String roomId){
        Object obj = redisTemplate.opsForHash().get(getKey(roomId),FIELD_INVITED_USER_ID);
        if(obj instanceof Integer){
            return ((Integer) redisTemplate.opsForHash().get(getKey(roomId),FIELD_INVITED_USER_ID)).longValue();
        }
        return (Long)redisTemplate.opsForHash().get(getKey(roomId),FIELD_INVITED_USER_ID);
    }

    private String FIELD_START_TIME = "startTime";
    public Long getStartTime(String roomId){
        return (Long)redisTemplate.opsForHash().get(getKey(roomId),FIELD_START_TIME);
    }

    private String FIELD_CONFIRM_TIME = "confirmTime";
    public void putConfirmTime(String roomId,Long confirmTime){
        redisTemplate.opsForHash().put(getKey(roomId),FIELD_CONFIRM_TIME,confirmTime);
    }
    public Long getConfirmTime(String roomId){
        return (Long)redisTemplate.opsForHash().get(getKey(roomId),FIELD_CONFIRM_TIME);
    }

    private String FIELD_SEND_USER_CONFIRM_COUNT = "confirmSendUserCount";
    public Long incConfirmSendUserCount(String roomId){
        return redisTemplate.opsForHash().increment(getKey(roomId),FIELD_SEND_USER_CONFIRM_COUNT,1);
    }
    public Long getConfirmSendUserCount(String roomId){
        return (Long)redisTemplate.opsForHash().get(getKey(roomId),FIELD_SEND_USER_CONFIRM_COUNT);
    }

    private String FIELD_INVITED_USER_CONFIRM_COUNT = "confirminvitedUserCount";
    public Long incConfirminvitedUserCount(String roomId){
        return redisTemplate.opsForHash().increment(getKey(roomId),FIELD_INVITED_USER_CONFIRM_COUNT,1);
    }
    public Long getConfirminvitedUserCount(String roomId){
        return (Long)redisTemplate.opsForHash().get(getKey(roomId),FIELD_INVITED_USER_CONFIRM_COUNT);
    }

    public boolean refreshRedisRoomEffectiveTime(String roomId){
        if(redisTemplate.hasKey(getKey(roomId))) {
            return roomHeartBeatRedisManger.refreshRedisRoomEffectiveTime(roomId);
        }
        return false;
    }

    public List<String> findInvalidRoom(Long startTime, Long endTime){
        return roomHeartBeatRedisManger.findInvalidRoom(startTime,endTime);
    }

    public void delete(String roomId){
        roomHeartBeatRedisManger.del(roomId);
        redisTemplate.delete(getKey(roomId));
    }

}
