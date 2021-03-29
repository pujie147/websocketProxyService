package com.vdegree.february.im.api.ws;

import com.vdegree.february.im.common.constant.type.IMCMD;

import java.util.UUID;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/15 16:26
 */

public class RequestProto extends BaseProto {

    /**
     * 消息随机数，后台用于同一秒内的消息去重。请确保该字段填的是随机数
     */
    private static final String MSG_RANDOM = "msgRandom";
    public long getMsgRandom() {
        return ((Double)this.getOrDefault(MSG_RANDOM,0D)).longValue();
    }



    public static RequestProto buildRequest(IMCMD cmd){
        RequestProto requestProto = new RequestProto();
        requestProto.setReqeustId(UUID.randomUUID().toString());
        requestProto.setCmd(cmd);
        requestProto.setReqeustTime(System.currentTimeMillis());
        return requestProto;
    }


}
