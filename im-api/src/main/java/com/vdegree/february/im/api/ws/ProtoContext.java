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
 * wsproxy 和 imservice 交互对象 保存请求和响应的类型和需要构造的基础数据
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

    public ProtoContext buildFailResponseProto(ErrorEnum errorEnum,Gson gson) {
        return buildResponseProto(null,errorEnum,gson);
    }

    public ProtoContext buildSuccessResponseProto(Gson gson) {
        return buildSuccessResponseProto(null,gson);
    }

    public ProtoContext buildSuccessResponseProto(Object msg,Gson gson) {
        return buildResponseProto(msg,ErrorEnum.SUCCESS,gson);
    }

    public ProtoContext buildResponseProto(Object msg,ErrorEnum errorEnum,Gson gson) {
        ResponseProto response = ResponseProto.buildResponse(this.baseProto, errorEnum);
        response.setError(errorEnum);
        this.internalProto.setImCMDType(this.getBaseProto().getCmd().getType());
        this.responseProto = gson.toJson(response);
//        this.responseProto.setMessage(msg);
        return this;
    }
}
