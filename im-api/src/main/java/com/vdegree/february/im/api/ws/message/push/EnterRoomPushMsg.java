package com.vdegree.february.im.api.ws.message.push;

import com.vdegree.february.im.common.constant.type.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 进房推送
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/27 16:24
 */
@Data
@AllArgsConstructor
public class EnterRoomPushMsg {
    private String roomId;
    private RoomType roomType;
    private String token;
}
