package com.vdegree.february.im.common.constant.type;

import com.google.gson.annotations.SerializedName;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/27 16:14
 */
public enum ReplyType {
    @SerializedName("0")
    ACCEPT(0,"接受"),
    @SerializedName("1")
    REFUSE(1,"拒绝"),
;
    ReplyType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    private int type;
    private String name;
}
