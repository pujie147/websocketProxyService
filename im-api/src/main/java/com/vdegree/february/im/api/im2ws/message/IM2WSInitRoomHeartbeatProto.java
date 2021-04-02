package com.vdegree.february.im.api.im2ws.message;

import com.vdegree.february.im.api.im2ws.IM2WSProto;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/4/2 11:51
 */
@Data
@AllArgsConstructor
public class IM2WSInitRoomHeartbeatProto extends IM2WSProto {
    private Long userId;
    private String roomId;
}
