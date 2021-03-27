package com.vdegree.february.im.api.ws.message.request;

import com.vdegree.february.im.common.constant.ReplyType;
import com.vdegree.february.im.common.constant.RoomType;
import lombok.Data;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/27 16:13
 */
@Data
public class ConfirmInvitationRequestMsg {
    private Long sendUserId;
    private ReplyType replyType;
    private RoomType roomType;

}
