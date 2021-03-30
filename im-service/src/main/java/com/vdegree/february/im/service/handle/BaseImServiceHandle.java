package com.vdegree.february.im.service.handle;

import com.vdegree.february.im.api.ws.ResponseProto;
import com.vdegree.february.im.api.ws.RequestProto;
import com.vdegree.february.im.api.ws.WSRequestProtoContext;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/27 11:20
 */
public interface BaseImServiceHandle {
    ResponseProto execute(WSRequestProtoContext wsRequestProtoContext);
}
