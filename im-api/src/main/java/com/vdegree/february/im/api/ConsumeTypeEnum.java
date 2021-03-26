package com.vdegree.february.im.api;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 20:39
 */
public enum ConsumeTypeEnum {
    /**
     *
     * 起始位置{@link WSCMD.REQUEST_HEARTBEAT}
     */
    REQUEST(0,"请求", 10001L,19999L),
    /**
     *
     * 起始位置{@link WSCMD.PUSH_INVITED_USER_ENTER_ROOM}
     */
    PUSH(1,"推送",20000L,29999L),

    ;
    ConsumeTypeEnum(Integer type, String name,Long startPoint,Long endPoint) {
        this.type = type;
        this.name = name;
    }

    private Integer type;
    private String name;
    private Long startPoint;
    private Long endPoint;


}
