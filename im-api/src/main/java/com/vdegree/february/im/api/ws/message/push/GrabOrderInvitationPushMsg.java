package com.vdegree.february.im.api.ws.message.push;

import com.vdegree.february.im.common.constant.type.RoomType;
import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/30 11:55
 */
@Data
public class GrabOrderInvitationPushMsg implements Serializable {
    private Long sendUserId;
    private RoomType roomType;
}
