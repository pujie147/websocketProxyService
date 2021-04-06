package com.vdegree.february.im.api.ws.message.push;

import com.vdegree.february.im.common.constant.type.ReplyType;
import com.vdegree.february.im.common.constant.type.RoomType;
import lombok.Data;

/**
 * 拒接邀请推送
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/27 16:25
 */
@Data
public class RefuseInvitationPushMsg {
    private Long invitedUserId;
    private ReplyType replyType;
    private RoomType roomType;
}
