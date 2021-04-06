package com.vdegree.february.im.api.im2ws.message;

import com.vdegree.february.im.api.im2ws.IM2WSProto;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * imservice 通知 wsproxy 断开用户的连接释放本地缓存
 *
 * @author DELL
 * @version 1.0
 * @date 2021/4/2 11:51
 */
@Data
@AllArgsConstructor
public class IM2WSDisConnectedProto extends IM2WSProto {
    private Long userId;
}
