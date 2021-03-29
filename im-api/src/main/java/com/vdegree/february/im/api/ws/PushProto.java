package com.vdegree.february.im.api.ws;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.common.constant.type.PushType;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.UUID;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/15 16:48
 */
@Data
@Log4j2
public class PushProto<T> extends BaseProto {

    private List<Long> pushUserIds;
    private PushType pushType;
    private T message;

    private PushProto(){}

    public static <T>PushProto buildPush(IMCMD imcmd,T message,PushType pushType,List<Long> userIds){
        PushProto pushProto = new PushProto();
        pushProto.setRequestId(UUID.randomUUID().toString());
        pushProto.setCmd(imcmd);
        pushProto.setMessage(message);
        pushProto.setPushType(pushType);
        pushProto.setPushUserIds(userIds);
        pushProto.setRequestTime(System.currentTimeMillis());
        return pushProto;
    }

    public static PushProto buildPush(BaseProto baseProto){
        if(baseProto instanceof PushProto){
            return (PushProto) baseProto;
        }
        return null;
    }


}
