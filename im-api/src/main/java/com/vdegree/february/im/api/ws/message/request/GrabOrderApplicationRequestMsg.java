package com.vdegree.february.im.api.ws.message.request;

import com.vdegree.february.im.common.constant.type.RoomType;
import lombok.Data;

/**
 * 抢单申请 请求
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/30 17:25
 */
@Data
public class GrabOrderApplicationRequestMsg {
    private Long sendUserId;
    private RoomType roomType;
    private Integer enterRoomCode;

}
