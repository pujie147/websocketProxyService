package com.vdegree.february.im.api.ws;

import lombok.Data;
import org.springframework.beans.BeanUtils;


/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/30 18:47
 */

@Data
public class HeartBeatProto extends BaseProto{

    public static HeartBeatProto build(BaseProto baseProto){
        HeartBeatProto heartBeatProto = new HeartBeatProto();
        BeanUtils.copyProperties(baseProto,heartBeatProto);
        return heartBeatProto;
    }

    public ResponseProto buildResponseProto(){
        return ResponseProto.buildResponse(this);
    }
}
