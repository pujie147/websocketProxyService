package com.vdegree.february.im.service.handle;

import com.vdegree.february.im.api.ws.ProtoContext;
import com.vdegree.february.im.api.ws.message.push.InvitedUserEnterRoomPushMsg;

/**
 * ImServcie handler的接口
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/27 11:20
 */
public interface BaseImServiceHandle {
    ProtoContext execute(ProtoContext protoContext);
}
