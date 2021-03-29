package com.vdegree.february.im.common.constant.type;

import com.google.gson.annotations.SerializedName;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/27 15:20
 */
public enum RoomType {
    @SerializedName("1")
    Chat(1,"聊天","C"),
    @SerializedName("2")
    Video(2,"视频","V"),
    @SerializedName("3")
    Audio(3,"音频","A"),
    ;

    RoomType(int type, String name,String roomPrefix) {
        this.type = type;
        this.name = name;
        this.roomPrefix = roomPrefix;
    }

    private int type;
    private String name;
    private String roomPrefix;
//    1:聊天 2：视频 3：音频


    public String getRoomPrefix() {
        return roomPrefix;
    }}
