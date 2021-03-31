package com.vdegree.february.im.api.ws;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
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
public class ProtoContext<T> implements Serializable {
    private BaseProto baseProto = new BaseProto();
    private RequestProto requestProto = new RequestProto();
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

//    public RequestProto<T> getRequestProto(T t){
//        return new Gson().fromJson(json,new TypeToken<RequestProto<>>(){}.getType());
//    }
//    public RequestProto<T> getRequestProto(){
//        return new Gson().fromJson(json,new TypeToken<ResponseProto<T>>(){}.getType());
//    }

    public HeartBeatProto buildHeartBeatProto(){
        return HeartBeatProto.build(baseProto);
    }

    public RoomHeartBeatProto buildRoomHeartBeatProto(){
        return RoomHeartBeatProto.build(baseProto);
    }


//    public void buildPustProto(IMCMD imcmd, Object message,PushType pushType, List<Long> pushUserIds) {
//        PushProto pushProto = PushProto.buildPush(imcmd, message);
//        this.responseProto = pushProto;
//        this.internalProto.setPushType(pushType);
//        this.internalProto.setPustUserIds(pushUserIds);
//    }

    public static ProtoContext buildContext(IMCMD imcmd, Object message,PushType pushType, List<Long> pushUserIds) {
        ProtoContext<Object> protoContext = new ProtoContext<>();
//        protoContext.buildPustProto(imcmd,message,pushType,pushUserIds);
        return protoContext;
    }

    public void buildSuccessResponseProto() {
        this.responseProto = new Gson().toJson(ResponseProto.buildResponse(this.baseProto));
    }

    public void buildFailResponseProto(ErrorEnum errorEnum) {
        this.responseProto = new Gson().toJson(ResponseProto.buildResponse(this.baseProto,errorEnum));
    }
}
