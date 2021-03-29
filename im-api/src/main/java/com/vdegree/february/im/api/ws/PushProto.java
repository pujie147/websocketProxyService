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
@JsonIgnoreProperties(value = {"userIds","pushType"})
public class PushProto extends BaseProto {


    private static final String USER_IDS = "userIds";
    public void setPushUserIds(List<Long> userIds){
        this.put(USER_IDS,userIds);
    }
    public List<Long> getPushUserIds(){
        return (List<Long>)this.get(USER_IDS);
    }

    private static final String pushType = "pushType";
    public void setPushType(PushType pushType){
        this.put(pushType,pushType);
    }
    public PushType getPushType(){
        return (PushType)this.get(USER_IDS);
    }

    private PushProto(){}

    public static PushProto buildPush(IMCMD imcmd,String message,PushType pushType,List<Long> userIds){
        PushProto pushProto = new PushProto();
        pushProto.setReqeustId(UUID.randomUUID().toString());
        pushProto.setCmd(imcmd);
        pushProto.setMsg(message);
        pushProto.setPushType(pushType);
        pushProto.setPushUserIds(userIds);
        return pushProto;
    }

    public static PushProto buildPush(BaseProto baseProto){
        PushProto pushProto = new PushProto();
        pushProto.putAll(baseProto);
        return pushProto;
    }


}
