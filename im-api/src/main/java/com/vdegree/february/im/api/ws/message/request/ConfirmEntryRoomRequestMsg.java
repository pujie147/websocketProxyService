package com.vdegree.february.im.api.ws.message.request;

import com.vdegree.february.im.common.constant.type.RoomType;
import lombok.Data;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/27 16:13
 */
@Data
public class ConfirmEntryRoomRequestMsg {
    private String roomId;
    private RoomType roomType;

}
