package com.vdegree.february.im.common.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 用户心跳的 redis 记录
 * 使用 zset 记录用户心跳
 * Score 最后一次心跳时间戳
 * value 中存放UserId
 * 不能直接访问 可以通过 ${@link UserDataRedisManger} 访问
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

    /**
     * 有效时间
     */
    @Value("${app.ws.idle-time:60000}")
    private Long idleTime;

    /**
     * 最后有效时间
     * @return long
     */
    private long idleTime(){
        return System.currentTimeMillis()+idleTime;
    }

    /**
     * redis key
     */
    private String USER_EFFECTIVE_REDIS_KEY = "IM_USER_EFFECTIVE_TIME";

    /**
     * @Author DELL
     * @Date 14:32 2021/4/6
     * @Description 生成redis的心跳初始化
     * @param: userId
     * @Return void
     * @Exception
     **/
    public void generateRedisUserEffectiveTime(long userId){
        redisTemplate.opsForZSet().add(USER_EFFECTIVE_REDIS_KEY,userId,idleTime());
    }

    /**
     * @Author DELL
     * @Date 14:32 2021/4/6
     * @Description 刷新redis的心跳有效时间
     * @param: userId
     * @Return boolean 
     * @Exception 
     **/
    public boolean refreshRedisUserEffectiveTime(long userId){
        if (isUserEffective(userId)) {
            redisTemplate.opsForZSet().add(USER_EFFECTIVE_REDIS_KEY, userId, idleTime());
            return true;
        }
        return false;
    }

    /**
     * @Author DELL
     * @Date 14:33 2021/4/6
     * @Description 判断用户是否失效
     * @param: userid
     * @Return boolean 
     * @Exception 
     **/
    public boolean isUserEffective(long userid){
        Long effectiveTime = redisTemplate.opsForZSet().score(USER_EFFECTIVE_REDIS_KEY, userid).longValue();
        if(System.currentTimeMillis()<=effectiveTime){
            return true;
        }
        return false;
    }

    /**
     * @Author DELL
     * @Date 14:34 2021/4/6
     * @Description 删除用户redis心跳记录
     * @param: userId
     * @Return boolean
     * @Exception
     **/
    public boolean del(Long userId){
        Long effectiveTime = redisTemplate.opsForZSet().remove(USER_EFFECTIVE_REDIS_KEY,userId);
        if(effectiveTime!=null){
            return true;
        }
        return false;
    }

    /**
     * @Author DELL
     * @Date 14:34 2021/4/6
     * @Description 查询所有失效的用户
     * 后续扩展
     *      可以添加 offer 和 count 分页处理
     * @param: 
     * @Return java.util.Set<java.lang.Long> 
     * @Exception 
     **/
    public Set<Long> findInvalidUser() {
        Set set = redisTemplate.opsForZSet().rangeByScore(USER_EFFECTIVE_REDIS_KEY, 0, System.currentTimeMillis());
        return set;
    }
}
