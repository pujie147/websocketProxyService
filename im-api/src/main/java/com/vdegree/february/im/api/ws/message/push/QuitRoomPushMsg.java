package com.vdegree.february.im.api.ws.message.push;

import com.vdegree.february.im.common.constant.type.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/31 18:25
 */
@Data
@AllArgsConstructor
public class QuitRoomPushMsg {
    private String roomId;
    private RoomType roomType;

}
