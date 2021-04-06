package com.vdegree.february.im.common.constant.type;

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

/**
 * 房间类型
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/27 15:20
 */
public enum RoomType {
    @SerializedName("1")
    Chat(1, "聊天", "C"),
    @SerializedName("2")
    Video(2, "视频", "V"),
    @SerializedName("3")
    Audio(3, "音频", "A"),
    ;

    RoomType(int type, String name, String roomPrefix) {
        this.type = type;
        this.name = name;
        this.roomPrefix = roomPrefix;
    }

    private int type;
    private String name;
    /**
     * 构建房间id的首字母
     */
    private String roomPrefix;


    public String getRoomPrefix() {
        return roomPrefix;
    }

    /**
     * @Author DELL
     * @Date 15:09 2021/4/6
     * @Description 解析房间类型
     * @param: roomId
     * @Return com.vdegree.february.im.common.constant.type.RoomType
     * @Exception
     **/
    public static RoomType getRoomPrefixByRoomId(String roomId) {
        if (!StringUtils.isEmpty(roomId)) {
            for (RoomType value : values()) {
                if (roomId.startsWith(value.getRoomPrefix())) {
                    return value;
                }
            }
        }
        return null;
    }

    /**
     * @Author DELL
     * @Date 15:09 2021/4/6
     * @Description 生成房间id
     * @param: userId1
     * @param: userId2
     * @Return java.lang.String 
     * @Exception 
     **/
    public String generate(Long userId1, Long userId2){
        String roomId = Lists.newArrayList(userId1.toString(), userId2.toString()).stream().sorted().collect(Collectors.joining("-"));
        return this.roomPrefix + roomId;
    }

}