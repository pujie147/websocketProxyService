package com.vdegree.february.im.api.ws.message.request;

import com.vdegree.february.im.common.constant.type.ReplyType;
import com.vdegree.february.im.common.constant.type.RoomType;
import lombok.Data;

/**
 * 确认邀请 请求
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/27 16:13
 */
@Data
public class ConfirmInvitationRequestMsg implements BaseRequestMsg{
    private Long sendUserId;
    private ReplyType replyType;
    private RoomType roomType;

}
