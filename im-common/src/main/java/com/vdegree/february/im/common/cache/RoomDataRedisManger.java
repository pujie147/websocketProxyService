package com.vdegree.february.im.common.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 房间的基础信息
 * redis 操作类
 * 确认邀请成功时创建 SWConfirmInvitationHandle
 * 房间失效时销毁
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

    /**
     * @Author DELL
     * @Date 14:38 2021/4/6
     * @Description 初始化 房间在redis中的缓存信息
     * @param: roomId
     * @param: sendUserId
     * @param: invitedUserId
     * @Return void
     * @Exception
     **/
    public void buildNewRedisData(String roomId,Long sendUserId,Long invitedUserId){
        HashOperations opshash = redisTemplate.opsForHash();
        opshash.put(getKey(roomId),FIELD_SEND_USER_ID,sendUserId);
        opshash.put(getKey(roomId),FIELD_INVITED_USER_ID,invitedUserId);
        opshash.put(getKey(roomId),FIELD_START_TIME,System.currentTimeMillis());
        roomHeartBeatRedisManger.generateRedisUserEffectiveTime(roomId);
    }

    /**
     * 房间缓存 发起用户Id保存
     */
    private String FIELD_SEND_USER_ID = "sendUserId";
    public Long getSendUserId(String roomId){
        Object obj = redisTemplate.opsForHash().get(getKey(roomId), FIELD_SEND_USER_ID);
        if(obj instanceof Integer){
            return ((Integer) redisTemplate.opsForHash().get(getKey(roomId), FIELD_SEND_USER_ID)).longValue();
        }
        return (Long)redisTemplate.opsForHash().get(getKey(roomId),FIELD_SEND_USER_ID);
    }

    /**
     * 房间缓存 邀请用户Id保存
     */
    private String FIELD_INVITED_USER_ID = "invitedUserId";
    public Long getInvitedUserId(String roomId){
        Object obj = redisTemplate.opsForHash().get(getKey(roomId),FIELD_INVITED_USER_ID);
        if(obj instanceof Integer){
            return ((Integer) redisTemplate.opsForHash().get(getKey(roomId),FIELD_INVITED_USER_ID)).longValue();
        }
        return (Long)redisTemplate.opsForHash().get(getKey(roomId),FIELD_INVITED_USER_ID);
    }

    /**
     * 房间缓存 房间开始时间
     */
    private String FIELD_START_TIME = "startTime";
    public Long getStartTime(String roomId){
        return (Long)redisTemplate.opsForHash().get(getKey(roomId),FIELD_START_TIME);
    }

    /**
     * 房间缓存 确认进入房间 发起用户记录
     */
    private String FIELD_SEND_USER_CONFIRM_COUNT = "confirmSendUserCount";
    public Long incConfirmSendUserCount(String roomId){
        return redisTemplate.opsForHash().increment(getKey(roomId),FIELD_SEND_USER_CONFIRM_COUNT,1);
    }
    public Long getConfirmSendUserCount(String roomId){
        return (Long)redisTemplate.opsForHash().get(getKey(roomId),FIELD_SEND_USER_CONFIRM_COUNT);
    }

    /**
     * 房间缓存 确认进入房间 邀请用户记录记录
     */
    private String FIELD_INVITED_USER_CONFIRM_COUNT = "confirminvitedUserCount";
    public Long incConfirminvitedUserCount(String roomId){
        return redisTemplate.opsForHash().increment(getKey(roomId),FIELD_INVITED_USER_CONFIRM_COUNT,1);
    }
    public Long getConfirminvitedUserCount(String roomId){
        return (Long)redisTemplate.opsForHash().get(getKey(roomId),FIELD_INVITED_USER_CONFIRM_COUNT);
    }

    /**
     * @Author DELL
     * @Date 14:41 2021/4/6
     * @Description 刷新房间的redis 心跳
     * @param: roomId
     * @Return boolean 
     * @Exception 
     **/
    public boolean refreshRedisRoomEffectiveTime(String roomId){
        if(redisTemplate.hasKey(getKey(roomId))) {
            return roomHeartBeatRedisManger.refreshRedisRoomEffectiveTime(roomId);
        }
        return false;
    }

    /**
     * @Author DELL
     * @Date 14:45 2021/4/6
     * @Description 查询失效的房间
     * @param:
     * @Return java.util.Set<java.lang.String>
     * @Exception
     **/
    public Set<String> findInvalidRoom(){
        return roomHeartBeatRedisManger.findInvalidRoom();
    }

    /**
     * @Author DELL
     * @Date 14:42 2021/4/6
     * @Description 删除房间的缓存
     * @param: roomId
     * @Return void 
     * @Exception 
     **/
    public void delete(String roomId){
        roomHeartBeatRedisManger.del(roomId);
        redisTemplate.delete(getKey(roomId));
    }

}
