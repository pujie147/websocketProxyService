package com.vdegree.february.im.common.constant.type;

import com.google.gson.annotations.SerializedName;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/4/2 9:33
 */
public enum IsInRoomEnum {
    @SerializedName("0")
    NotInRoom(0,"不在房间内"),
    @SerializedName("1")
    InRoom(1,"房间内")
    ;

    IsInRoomEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    private int type;
    private String name;
}
