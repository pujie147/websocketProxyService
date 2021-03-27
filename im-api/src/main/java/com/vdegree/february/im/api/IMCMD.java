package com.vdegree.february.im.api;


import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Optional;

/**
 * websocket 命令
 * 作为路由器使用，唯有heartbeat是wsproxy本身处理
 * 其他命令都是转发
 * 分为：
 *      1、lient 发给 service的协议               10000 - 19999
 *      2、service主动推送给 client的协议          20000 - 29999
 *      3、wsproxy -> imService 的协议           30000 - 34999
 *      4、ImService -> wsproxy 的协议           35000 - 39999
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/15 16:04
 */
public enum IMCMD {
    /**
     *
     * 起始位置{@link REQUEST_HEARTBEAT}
     */
    REQUEST(0,"请求", 10001,19999),
    /**
     *
     * 起始位置{@link PUSH_INVITED_USER_ENTER_ROOM}
     */
    PUSH(1,"推送",20000,29999),



    /**
     *  reqeust 请求是 client 发给 service的协议 10000 - 19999
     */
    REQUEST_HEARTBEAT(10001,"心跳","心跳"),
    REQUEST_SEND_1V1_ROOM_REQUEST(10002,"发起1对1房间邀请","房间只能有2个人"),


    /**
     *
     *  push 请求是 service主动推送给 client的协议 20000 - 29999
      */
    PUSH_INVITED_USER_ENTER_ROOM(20001,"邀请用户进入房间","邀请指定用户(用户只能为2人)进入指定房间"),
    PUSH_QUIT_ROOM(20001,"退出房间","指定用户退出房间"),


    // wsproxy -> imService 的协议 30000 - 34999


    // ImService -> wsproxy 的协议 35000 - 39999

    //

    ;
    private static HashMap<Integer, IMCMD> hashMap;

    IMCMD(int type, String name, String desc) {
        this.type = type;
        this.name = name;
        this.desc = desc;
    }

    IMCMD(Integer type, String name, Integer startPoint, Integer endPoint) {
        this.type = type;
        this.name = name;
        this.endPoint = endPoint;
        this.startPoint = startPoint;
    }

    static {
        hashMap= Maps.newHashMapWithExpectedSize(values().length);
        for(IMCMD v:values()){
            hashMap.put(v.type,v);
        }
    }

    private int type;
    private String name;
    private String desc;

    private int startPoint;
    private int endPoint;

    public int getType() {
        return type;
    }


    public static Optional<IMCMD> get(int type){
        return Optional.of(hashMap.get(type));
    }

    /**
     * 获取cmd的类别
     * 可以查看 {@link REQUEST}
     * @param IMCMD
     * @return
     */
    public static IMCMD getConsumeType(IMCMD IMCMD){
        if(REQUEST.startPoint<= IMCMD.getType() && IMCMD.getType()<=REQUEST.endPoint){
            return REQUEST;
        }else if(PUSH.startPoint<= IMCMD.getType() && IMCMD.getType()<=PUSH.endPoint){
            return PUSH;
        }
        return null;
    }
}
