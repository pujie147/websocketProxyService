package com.vdegree.february.im.api.ws.message.push;

import com.vdegree.february.im.common.constant.type.ErrorEnum;
import com.vdegree.february.im.common.constant.type.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 服务器事件推送
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/31 18:25
 */
@Data
public class ServiceEventPushMsg {
    private Integer errorCode;
    private String errorMsg;

    public ServiceEventPushMsg(ErrorEnum errorCode) {
        this.errorCode = errorCode.getErrorCode();
        this.errorMsg = errorCode.getErrorInfo();
    }
}
