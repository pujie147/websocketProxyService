package com.vdegree.february.im.api.ws.message.request;

import com.vdegree.february.im.common.constant.type.RoomType;
import lombok.Data;

/**
 * 退出房间 请求
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/27 15:19
 */
@Data
public class QuitRoomRequestMsg implements BaseRequestMsg{
    private String roomId;
    private RoomType roomType;
}
