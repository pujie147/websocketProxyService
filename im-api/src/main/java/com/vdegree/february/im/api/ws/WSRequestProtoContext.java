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
public class WSRequestProtoContext {
    @Autowired
    private Gson gson;
    private BaseProto baseProto;
    private RequestProto requestProto;
    private InternalProto internalProto;
    private String json;

    public WSRequestProtoContext buildContext(String json){
        WSRequestProtoContext wsRequestProtoContext = new WSRequestProtoContext();
        wsRequestProtoContext.baseProto = gson.fromJson(json,BaseProto.class);
        this.json = json;
        return wsRequestProtoContext;
    }

    public HeartBeatProto buildHeartBeatProto(){
        return HeartBeatProto.build(baseProto);
    }

    public RoomHeartBeatProto buildRoomHeartBeatProto(){
        return RoomHeartBeatProto.build(baseProto);
    }


}
