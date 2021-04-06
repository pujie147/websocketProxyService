package com.vdegree.february.im.api.ws;

import com.vdegree.february.im.common.constant.type.IMCMD;
import lombok.Data;

import java.io.Serializable;


/**
 * client -> imservice 要处理的请求和参数
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/15 16:26
 */
@Data
public class RequestProto<T> implements Serializable {
    private Integer msgRandom;
    private T message;

    public static RequestProto buildRequest(IMCMD cmd){
        RequestProto requestProto = new RequestProto();
//        requestProto.setRequestId(UUID.randomUUID().toString());
//        requestProto.setCmd(cmd);
//        requestProto.setRequestTime(System.currentTimeMillis());
        return requestProto;
    }

}
