package com.vdegree.february.im.api.ws;

import com.google.gson.Gson;
import com.vdegree.february.im.api.ws.message.request.HeartBestRequestMsg;
import com.vdegree.february.im.common.constant.type.ErrorEnum;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.common.constant.type.PushType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/30 18:05
 */
@Data
public class ProtoContext implements Serializable {
    private BaseProto baseProto = new BaseProto();
    private InternalProto internalProto = new InternalProto();
    private String json;
    // 可能是push 和 response
    private String responseProto ;

    public static ProtoContext buildContext(String json){
        ProtoContext protoContext = new ProtoContext();
        protoContext.baseProto = new Gson().fromJson(json,BaseProto.class);
        protoContext.json = json;
        return protoContext;
    }

    public HeartBeatProto buildHeartBeatProto(HeartBestRequestMsg msg){
        HeartBeatProto proto = HeartBeatProto.build(baseProto);
        proto.setMessage(msg);
        return proto;
    }

    public RoomHeartBeatProto buildRoomHeartBeatProto(){
        return RoomHeartBeatProto.build(baseProto);
    }

    public ProtoContext buildSuccessResponseProto() {
        this.responseProto = new Gson().toJson(ResponseProto.buildResponse(this.baseProto));
        this.internalProto.setImCMDType(this.getBaseProto().getCmd().getType());
        return this;
    }

    public ProtoContext buildFailResponseProto(ErrorEnum errorEnum) {
        this.responseProto = new Gson().toJson(ResponseProto.buildResponse(this.baseProto,errorEnum));
        this.internalProto.setImCMDType(this.getBaseProto().getCmd().getType());
        return this;
    }
}
