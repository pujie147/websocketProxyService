package com.vdegree.february.im.api.ws;

import com.vdegree.february.im.common.constant.type.IMCMD;
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
public class PushProto extends BaseProto {

    private static final String USER_IDS = "userIds";
    public void setPushUserIds(List<Long> userIds){
        this.put(USER_IDS,userIds);
    }

    private PushProto(){}

    public static PushProto buildPush(IMCMD imcmd,String message){
        PushProto pushProto = new PushProto();
        pushProto.setReqeustId(UUID.randomUUID().toString());
        pushProto.setCmd(imcmd);
        return pushProto;
    }


}
