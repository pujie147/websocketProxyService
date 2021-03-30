package com.vdegree.february.im.api.ws;

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

    public ResponseProto buildResponseProto(){
        return ResponseProto.buildResponse(this);
    }

}
