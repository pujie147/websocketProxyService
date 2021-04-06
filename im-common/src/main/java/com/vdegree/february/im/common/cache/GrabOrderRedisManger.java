package com.vdegree.february.im.common.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 抢单缓存
 * 保存 进入房间码
 * 保证一个用户同时只能有一个邀请
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

    private String getKey(Long userId ){
        return GRAB_ORDER_SESSION_DATA_REDIS_KEY+"_"+userId;
    }

    private String getHashKey(Integer enterRoomCode){
        return FIELD_GRAB_ORDER_PERSON_COUNT +"_"+enterRoomCode;
    }

    public void buildNewRedisData(Long userId ,Integer enterRoomCode,Long timeout){
        redisTemplate.delete(getKey(userId));
        redisTemplate.opsForHash().increment(getKey(userId),getHashKey(enterRoomCode),0);
        redisTemplate.expire(getKey(userId),timeout, TimeUnit.SECONDS);
    }

    public Long incGrabOrderPersonCount(Long userId ,Integer enterRoomCode){
        if(redisTemplate.opsForHash().hasKey(getKey(userId),getHashKey(enterRoomCode))){
            return redisTemplate.opsForHash().increment(getKey(userId),getHashKey(enterRoomCode),1);
        }
        return null;
    }

}
