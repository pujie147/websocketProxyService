package com.vdegree.february.im.api.mq;

/**
 * 服务通讯类型协议
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 17:01
 */
public enum ServiceProtoType {
    INTERNAL_WS_PROXY_IM_SERVICE(1,"wsProxy和ImService"),
    ;

    ServiceProtoType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    private int type;
    private String name;

}
