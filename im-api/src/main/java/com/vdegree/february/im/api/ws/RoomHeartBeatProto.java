package com.vdegree.february.im.api.ws;

import com.vdegree.february.im.common.constant.type.ErrorEnum;
import org.springframework.beans.BeanUtils;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/30 18:47
 */
public class RoomHeartBeatProto extends BaseProto{

    public static RoomHeartBeatProto build(BaseProto baseProto){
        RoomHeartBeatProto proto = new RoomHeartBeatProto();
        BeanUtils.copyProperties(baseProto,proto);
        return proto;
    }

    public ResponseProto buildResponseProto(ErrorEnum errorEnum){
        ResponseProto responseProto = ResponseProto.buildResponse(this);
        responseProto.setError(errorEnum);
        responseProto.setResponseTime(System.currentTimeMillis());
        return responseProto;
    }

    public ResponseProto buildResponseProto(){
        ResponseProto responseProto = ResponseProto.buildResponse(this);
        responseProto.setResponseTime(System.currentTimeMillis());
        return responseProto;
    }
}
