package com.vdegree.february.im.api.ws.message.push;

import com.vdegree.february.im.common.constant.type.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 抢单邀请推送
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/30 11:55
 */
@Data
@AllArgsConstructor
public class GrabOrderInvitationPushMsg implements Serializable {
    private Long sendUserId;
    private RoomType roomType;
    private Integer enterRoomCode;
}
