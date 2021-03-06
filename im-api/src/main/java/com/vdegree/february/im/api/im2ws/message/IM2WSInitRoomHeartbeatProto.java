package com.vdegree.february.im.api.im2ws.message;

import com.vdegree.february.im.api.im2ws.IM2WSProto;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * imservice 通知 wsproxy 初始化 房间心跳缓存
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
