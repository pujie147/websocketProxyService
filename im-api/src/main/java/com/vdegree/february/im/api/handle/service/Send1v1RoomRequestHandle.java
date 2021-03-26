package com.vdegree.february.im.api.handle.service;


import com.vdegree.february.im.api.BaseHandle;
import com.vdegree.february.im.api.ws.base.reponse.ReponseProto;
import com.vdegree.february.im.api.ws.base.request.RequestProto;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 19:56
 */
@Component
public class Send1v1RoomRequestHandle implements BaseHandle {
    @Override
    public ReponseProto exector(RequestProto requestProto) {
        return ReponseProto.buildReponse(requestProto);
    }
}
