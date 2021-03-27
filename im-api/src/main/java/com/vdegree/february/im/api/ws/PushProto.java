package com.vdegree.february.im.api.ws;

import com.vdegree.february.im.api.IMCMD;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

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

    private PushProto(){}

    public static PushProto buildPush(IMCMD imcmd,Object message){
        PushProto pushProto = new PushProto();
        pushProto.setReqeustId(UUID.randomUUID().toString());
        pushProto.setCmd(imcmd);
        return pushProto;
    }


}
