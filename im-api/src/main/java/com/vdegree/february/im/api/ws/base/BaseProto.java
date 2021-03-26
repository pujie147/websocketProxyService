package com.vdegree.february.im.api.ws.base;

import com.vdegree.february.im.api.WSCMD;

import java.util.HashMap;


/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/15 16:47
 */
public class BaseProto extends HashMap {
    private static final String REQEUST_TIME = "reqeustTime";
    private static final String REPONSE_TIME = "reponseTime";
    private static final String WS_PROXY_START_TIME = "wsProxyStartTime";
    private static final String REQEUST_ID = "reqeustId";
    private static final String CMD = "cmd";

    public long getReqeustTime() {
        return (long) this.getOrDefault(REQEUST_TIME,0L);
    }

    public long getReponseTime() {
        return (long) this.getOrDefault(REQEUST_TIME,0L);
    }

    public void setReponseTime(long reponseTime) {
        this.put(REPONSE_TIME,reponseTime);
    }

    public void setWSProxyStartTime(long startTime) {
        this.put(WS_PROXY_START_TIME,startTime);
    }

    public void getWSProxyStartTime(long startTime) {
        this.getOrDefault(WS_PROXY_START_TIME,0);
    }

    public String getReqeustId() {
        return (String) this.getOrDefault(REQEUST_ID,"");
    }

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



}
