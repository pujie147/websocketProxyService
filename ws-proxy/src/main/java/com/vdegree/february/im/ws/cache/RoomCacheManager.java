package com.vdegree.february.im.ws.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import lombok.extern.log4j.Log4j2;

import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/19 11:21
 */
@Log4j2
public class RoomCacheManager {
    /** 缓存项最大数量 */
    private final long GUAVA_CACHE_SIZE;

    /** 缓存时间：秒 */
    private final long GUAVA_CACHE_SECONDS;

    private LoadingCache<Long, String> USER_ID_ROOM_ID = null;

    /**
     * 全局缓存设置
     *
     * 缓存项最大数量：100000
     * 缓存有效时间（天）：10
     *
     *
     * @param
     * @return
     * @throws Exception
     */
    public RoomCacheManager(RemovalListener<Long, String> removalListener, Integer connectionMax, Long idelTime) throws Exception {
        this.GUAVA_CACHE_SECONDS = idelTime;
        this.GUAVA_CACHE_SIZE = connectionMax;
        LoadingCache<Long, String> cache = CacheBuilder.newBuilder()
                // 缓存池大小，在缓存项接近该大小时， Guava开始回收旧的缓存项
                .maximumSize(GUAVA_CACHE_SIZE)
                // 设置时间对象没有被读/写访问则对象从内存中删除(在另外的线程里面不定期维护)
                .expireAfterAccess(GUAVA_CACHE_SECONDS, TimeUnit.SECONDS)
                // 设置缓存在写入之后 设定时间 后失效
                .expireAfterWrite(GUAVA_CACHE_SECONDS, TimeUnit.SECONDS)
                // 移除监听器,缓存项被移除时会触发
                .removalListener(removalListener)
                // 开启Guava Cache的统计功能
                .recordStats()
                .build(new CacheLoader <Long, String>() {
                    @Override
                    public String load(Long key) throws Exception {
                        // 处理缓存键不存在缓存值时的处理逻辑
                        return null;
                    }
                });
        USER_ID_ROOM_ID = cache;
    }

    /**
     * 设置缓存值
     * 注: 若已有该key值，则会先移除(会触发removalListener移除监听器)，再添加
     *
     * @param key
     * @param value
     */
    public void put(Long key, String value) {
        try {
            USER_ID_ROOM_ID.put(key, value);
        } catch (Exception e) {
            log.error("设置缓存值出错", e);
        }
    }

    /**
     * @Author DELL
     * @Date 15:32 2021/3/25
     * @Description
        获取缓存值 通过userId查询 RoomId
     * @param: key
     * @Return java.util.Optional<io.netty.channel.Channel>
     * @Exception
     **/
    public String getRoomIdByUserId(Long userId) {
        String roomId = null;
        try {
            roomId = USER_ID_ROOM_ID.get(userId);
        } catch (Exception e) {
            log.error("获取缓存值出错", e);
        }
        return roomId;
    }


    public boolean containsUserId(Long userId) {
        return USER_ID_ROOM_ID.asMap().containsKey(userId);
    }

    /**
     * @Author DELL
     * @Date 17:51 2021/3/25
     * @Description 刷新本地缓存等待时间
     * @param: userId
     * @Return void
     * @Exception
     **/
    public boolean refreshLocalCacheIdelTime(Long userId) {
        try {
            return USER_ID_ROOM_ID.get(userId) != null ? true : false;
        }catch (Exception e){
            return false;
        }
    }


    /**
     * @Author DELL
     * @Date 15:40 2021/3/25
     * @Description
        移除缓存
     * @param: userId
     * @Return void
     * @Exception
     **/
    public void remove(Long userId) {
        try {
            USER_ID_ROOM_ID.invalidate(userId);
        } catch (Exception e) {
            log.error("移除缓存出错", e);
        }
    }

    public Set<Long> userIds(){
        return USER_ID_ROOM_ID.asMap().keySet();
    }

    /**
     * 获取缓存项数量
     *
     * @return
     */
    public int size() {
        int size = 0;
        try {
            size = Long.valueOf(USER_ID_ROOM_ID.size()).intValue();
        } catch (Exception e) {
            log.error("获取缓存项数量出错", e);
        }
        return size;
    }
}