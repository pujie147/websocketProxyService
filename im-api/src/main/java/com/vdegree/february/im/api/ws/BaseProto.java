package com.vdegree.february.im.api.ws;

import com.vdegree.february.im.common.constant.IMCMD;

import java.util.HashMap;


/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/15 16:47
 */
public class BaseProto extends HashMap {


    private static final String SEND_USER_ID = "sendUserId";
    public long getSendUserId() {
        Object obj = this.get(SEND_USER_ID);
        if(obj instanceof Long){
            return ((Long) obj).longValue();
        }
        return 0;
    }
    public void setSendUserId(Long userId) {
        this.put(SEND_USER_ID,userId);
    }


    private static final String REQEUST_TIME = "reqeustTime";
    public long getReqeustTime() {
        return (long) this.getOrDefault(REQEUST_TIME,0L);
    }
    public void setReqeustTime(Long reqeustTime) {
        this.put(REQEUST_TIME,reqeustTime);
    }


    private static final String REPONSE_TIME = "reponseTime";
    public long getReponseTime() {
        return (long) this.getOrDefault(REPONSE_TIME,0L);
    }
    public void setReponseTime(long reponseTime) {
        this.put(REPONSE_TIME,reponseTime);
    }


    private static final String WS_PROXY_START_TIME = "wsProxyStartTime";
    public void setWSProxyStartTime(long startTime) {
        this.put(WS_PROXY_START_TIME,startTime);
    }
    public void getWSProxyStartTime(long startTime) {
        this.getOrDefault(WS_PROXY_START_TIME,0);
    }


    private static final String REQEUST_ID = "reqeustId";
    public String getReqeustId() {
        return (String) this.getOrDefault(REQEUST_ID,"");
    }
    public void setReqeustId(String requestId) {
        this.put(REQEUST_ID,requestId);
    }

    private static final String MSG = "message";
    public String getMsg(){return (String) this.getOrDefault(MSG,"");}
    public void setMsg(String msg){this.put(MSG,msg);}

    private static final String CMD = "cmd";
    public IMCMD getCmd() {
        if(this.get(CMD)!=null){
            if(this.get(CMD) instanceof String){
                return IMCMD.valueOf((String) this.get(CMD));
            }else if(this.get(CMD) instanceof Double){
                return IMCMD.get(((Double)this.get(CMD)).intValue()).orElse(null);
            }else if(this.get(CMD) instanceof Integer){
                return IMCMD.get(((Integer)this.get(CMD)).intValue()).orElse(null);
            }
        }
        return null;
    }
    public void setCmd(IMCMD imcmd){
        this.put(CMD,imcmd.getType());
    }

}
