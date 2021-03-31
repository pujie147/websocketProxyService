package com.vdegree.february.im.common.constant.type;


import com.google.common.collect.Maps;
import com.google.gson.annotations.SerializedName;

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
    @SerializedName("0")
    REQUEST(0,"请求", 10001,19999),
    /**
     *
     * 起始位置{@link PUSH_INVITED_USER_ENTER_ROOM}
     */
    @SerializedName("1")
    PUSH(1,"推送",20000,29999),



    /**
     *  request 请求是 client 发给 service的协议 10000 - 19999
     */
    @SerializedName("10001")
    REQUEST_HEARTBEAT(10001,"用户心跳","用户心跳"),
    @SerializedName("10002")
    REQUEST_INVITED_USER_ENTER_ROOM(10002,"发起1对1房间邀请","房间只能有2个人"),
    @SerializedName("10003")
    REQUEST_CONFIRM_INVITATION_ROOM(10003,"确认邀请","在服务器发起 邀请用户进入房间 之后客户端发起的 确认邀请"),
    @SerializedName("10004")
    REQUEST_CONFIRM_ENTER_ROOM(10004,"确认进入房间","在服务器发起 进入房间 之后客户端发起的 确认进入房间"),
    @SerializedName("10005")
    REQUEST_QUIT_ROOM(10005,"退出房间","退出房间"),
    @SerializedName("10006")
    REQUEST_GRAB_ORDER_APPLICATION(10006,"抢单申请","抢单申请"),
    @SerializedName("10011")
    REQUEST_ROOM_HEARTBEAT(10011,"房间心跳","房间心跳，保证房间的有效，由房间发起人维持"),


    /**
     *
     *  push 请求是 service主动推送给 client的协议 20000 - 29999
      */
    @SerializedName("20001")
    PUSH_INVITED_USER_ENTER_ROOM(20001,"邀请用户进入房间","邀请指定用户(用户只能为2人)进入指定房间"),
    @SerializedName("20002")
    PUSH_REFUSE_INVITATION(20002,"拒绝邀请","拒绝邀请"),
    @SerializedName("20003")
    PUSH_ENTER_ROOM(20003,"进入房间","进入房间"),
    @SerializedName("20004")
    PUSH_QUIT_ROOM(20004,"退出房间","退出房间"),
    @SerializedName("20005")
    PUSH_CONNECTION_CLOSE(20005,"链接关闭","链接关闭"),
    @SerializedName("20006")
    PUSH_GRAB_ORDER_INVITATION(20006,"抢单邀请","链接关闭"),
    @SerializedName("20007")
    PUSH_GRAB_ORDER_END(20007,"抢单结束","抢单结束"),


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
    public static IMCMD getConsumeType(Integer imCMDType){
        if(REQUEST.startPoint<= imCMDType && imCMDType<=REQUEST.endPoint){
            return REQUEST;
        }else if(PUSH.startPoint<= imCMDType && imCMDType<=PUSH.endPoint){
            return PUSH;
        }
        return null;
    }
}
