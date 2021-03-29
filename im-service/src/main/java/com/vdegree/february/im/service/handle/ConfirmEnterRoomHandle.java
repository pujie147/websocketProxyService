package com.vdegree.february.im.service.handle;

import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.api.IMController;
import com.vdegree.february.im.api.ws.ReponseProto;
import com.vdegree.february.im.api.ws.RequestProto;

/**
 * 1.接收客户端发起的聊天邀请
 * 2.回调appservice
 * 4.return
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 19:56
 */
@IMController(cmd = IMCMD.REQUEST_CONFIRM_ENTER_ROOM)
public class ConfirmEnterRoomHandle implements BaseImServiceHandle {


    @Override
    public ReponseProto execute(RequestProto requestProto) {
        return ReponseProto.buildReponse(requestProto);
    }
}
