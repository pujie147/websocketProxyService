package com.vdegree.february.im.api.ws.message.request;

import com.vdegree.february.im.common.constant.type.IsInRoomEnum;
import lombok.Data;

/**
 * 心跳参数
 *
 * @author DELL
 * @version 1.0
 * @date 2021/4/2 9:18
 */
@Data
public class HeartBestRequestMsg {
    /**
     * 0 不在房间内 1：房间内
     */
    IsInRoomEnum isInRoom;

    String roomId;

}
