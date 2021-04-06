package com.vdegree.february.im.common.constant.type;

/**
 * 推送类型
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/29 10:36
 */
public enum PushType {
    PUSH_ALL_USER(1,"推送给所有人"),
    PUSH_CONTAIN_USER(2,"推送包含用户"),
    PUSH_NON_CONTAIN_USER(3,"推送非包含用户"),
;
    PushType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    private int type;
    private String name;
}
