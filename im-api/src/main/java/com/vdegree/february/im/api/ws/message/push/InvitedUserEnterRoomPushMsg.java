package com.vdegree.february.im.api.ws.message.push;

import com.vdegree.february.im.common.constant.type.RoomType;
import lombok.Data;

/**
 * 邀请用户进入房间推送
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/27 15:19
 */
@Data
public class InvitedUserEnterRoomPushMsg {
    private Long sendUserId;
    private RoomType roomType;
}
