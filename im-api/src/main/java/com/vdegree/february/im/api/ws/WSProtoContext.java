package com.vdegree.february.im.api.ws;

import com.google.gson.Gson;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/30 18:05
 */
@Component
@Data
public class WSProtoContext {
    @Autowired
    private Gson gson;
    private BaseProto baseProto = new BaseProto();
    private RequestProto requestProto = new RequestProto();
    private InternalProto internalProto = new InternalProto();
    private String json;

    public WSProtoContext buildContext(String json){
        WSProtoContext wsProtoContext = new WSProtoContext();
        wsProtoContext.baseProto = gson.fromJson(json,BaseProto.class);
        this.json = json;
        return wsProtoContext;
    }

    public HeartBeatProto buildHeartBeatProto(){
        return HeartBeatProto.build(baseProto);
    }

    public RoomHeartBeatProto buildRoomHeartBeatProto(){
        return RoomHeartBeatProto.build(baseProto);
    }


}
