package com.vdegree.february.im.common.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/25 17:07
 */
@Component
public class GrabOrderRedisManger {
    @Autowired
    private RedisTemplate redisTemplate;

    private String GRAB_ORDER_SESSION_DATA_REDIS_KEY = "IM_GRAB_ORDER_SESSION_DATA";

    private String FIELD_GRAB_ORDER_PERSON_COUNT = "GRAB_ORDER_PERSON_COUNT";


    private String getKey(Long userId ,Integer enterRoomCode){
        return GRAB_ORDER_SESSION_DATA_REDIS_KEY+"_"+userId+"_"+enterRoomCode;
    }

    public void buildNewRedisData(Long userId ,Integer enterRoomCode,Long timeout){
        redisTemplate.opsForHash().increment(getKey(userId,enterRoomCode),FIELD_GRAB_ORDER_PERSON_COUNT,0);
        redisTemplate.expire(getKey(userId,enterRoomCode),timeout, TimeUnit.SECONDS);
    }

    public Long incGrabOrderPersonCount(Long userId ,Integer enterRoomCode){
        if(redisTemplate.opsForHash().hasKey(getKey(userId,enterRoomCode),FIELD_GRAB_ORDER_PERSON_COUNT)){
            return redisTemplate.opsForHash().increment(getKey(userId,enterRoomCode),FIELD_GRAB_ORDER_PERSON_COUNT,0);
        }
            return null;
    }

//    public Long getConnectStartTime(Long userId){
//        return (Long)redisTemplate.opsForHash().get(getKey(userId),FIELD_CONNECT_START_TIME);
//    }
//
//    public void putRoomId(Long userId,String roomId){
//        redisTemplate.opsForHash().put(getKey(userId),FIELD_ROOM_ID,roomId);
//    }
//    public String getRoomId(Long userId){
//        return (String)redisTemplate.opsForHash().get(getKey(userId),FIELD_ROOM_ID);
//    }
//
//    public void delRoomId(Long userId){
//        redisTemplate.opsForHash().delete(getKey(userId),FIELD_ROOM_ID);
//    }

}
