package com.vdegree.february.im.api.ws.base.request;

import com.vdegree.february.im.api.WSCMD;

import java.util.HashMap;
import java.util.UUID;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/15 16:26
 */

public class RequestProto extends HashMap {

    private static final String SEND_USER_ID = "sendUserId";
    public long getSendUserId() {
        return ((Double)this.getOrDefault(SEND_USER_ID,0D)).longValue();
    }
    public void setSendUserId(Long userId) {
        this.put(SEND_USER_ID,userId);
    }


    private static final String REQEUST_TIME = "reqeustTime";
    public long getReqeustTime() {
        return ((Double)this.getOrDefault(REQEUST_TIME,0D)).longValue();
    }
    public void setReqeustTime(Long reqeustTime) {
        this.put(REQEUST_TIME,reqeustTime);
    }

    private static final String REPONSE_TIME = "reponseTime";
    public long getReponseTime() {
        return ((Double)this.getOrDefault(REPONSE_TIME,0D)).longValue();
    }
    public void setReponseTime(Long reponseTime) {
        this.put(REPONSE_TIME,reponseTime);
    }


    private static final String WS_PROXY_START_TIME = "wsProxyStartTime";
    public long getWSProxyStartTime() {
        return ((Double)this.getOrDefault(WS_PROXY_START_TIME,0D)).longValue();
    }
    public void setWSProxyStartTime(long startTime) {
        this.put(WS_PROXY_START_TIME,startTime);
    }

    private static final String REQEUST_ID = "reqeustId";
    public String getReqeustId() {
        return (String) this.getOrDefault(REQEUST_ID,"");
    }
    public void setReqeustId(String reqeustId) {
        this.put(REQEUST_ID,reqeustId);
    }

    /**
     * 消息随机数，后台用于同一秒内的消息去重。请确保该字段填的是随机数
     */
    private static final String MSG_RANDOM = "msgRandom";
    public long getMsgRandom() {
        return ((Double)this.getOrDefault(MSG_RANDOM,0D)).longValue();
    }


    private static final String CMD = "cmd";
    public WSCMD getCmd() {
        if(this.get(CMD)!=null){
            if(this.get(CMD) instanceof String){
                return WSCMD.valueOf((String) this.get(CMD));
            }else if(this.get(CMD) instanceof Double){
                return WSCMD.get(((Double)this.get(CMD)).intValue()).orElse(null);
            }
        }
        return null;
    }
    public void setCmd(WSCMD wscmd){
        this.put(CMD,wscmd);
    }


    public static RequestProto buildRequest(WSCMD cmd){
        RequestProto requestProto = new RequestProto();
        requestProto.setReqeustId(UUID.randomUUID().toString());
        requestProto.setCmd(cmd);
        requestProto.setReqeustTime(System.currentTimeMillis());
        return requestProto;
    }


}
