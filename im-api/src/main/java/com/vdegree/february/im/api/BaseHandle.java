package com.vdegree.february.im.api;

import com.vdegree.february.im.api.ws.base.reponse.ReponseProto;
import com.vdegree.february.im.api.ws.base.request.RequestProto;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 19:54
 */
public interface BaseHandle {
    ReponseProto exector(RequestProto requestProto);
}
