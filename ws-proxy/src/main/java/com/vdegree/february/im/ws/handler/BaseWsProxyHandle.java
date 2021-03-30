package com.vdegree.february.im.ws.handler;

import com.vdegree.february.im.api.ws.BaseProto;
import com.vdegree.february.im.api.ws.WSRequestProtoContext;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/27 11:00
 */
public interface BaseWsProxyHandle {
    void execute(WSRequestProtoContext wsRequestProtoContext);
}
