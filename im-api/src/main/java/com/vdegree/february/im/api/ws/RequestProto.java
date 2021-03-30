package com.vdegree.february.im.api.ws;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vdegree.february.im.common.constant.type.IMCMD;
import lombok.Data;

import java.util.UUID;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/15 16:26
 */
@Data
public class RequestProto<T> extends BaseProto<T> {
    private Integer msgRandom;

    public static RequestProto buildRequest(IMCMD cmd){
        RequestProto requestProto = new RequestProto();
        requestProto.setRequestId(UUID.randomUUID().toString());
        requestProto.setCmd(cmd);
        requestProto.setRequestTime(System.currentTimeMillis());
        return requestProto;
    }

}
