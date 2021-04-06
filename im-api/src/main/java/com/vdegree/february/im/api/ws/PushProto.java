package com.vdegree.february.im.api.ws;

import com.vdegree.february.im.common.constant.type.IMCMD;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;

/**
 * imService 主动推送给client 的协议
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/15 16:48
 */
@Data
@Log4j2
public class PushProto<T> extends BaseProto {
    private T message;
    private PushProto(){}

    public static <T>PushProto buildPush(IMCMD imcmd,T message){
        PushProto pushProto = new PushProto();
        pushProto.setRequestId(UUID.randomUUID().toString());
        pushProto.setCmd(imcmd);
        pushProto.setMessage(message);
        pushProto.setRequestTime(System.currentTimeMillis());
        return pushProto;
    }

}
