package com.vdegree.february.im.service.controller;

import com.vdegree.february.im.common.constant.type.RoomType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * TODO 用于模拟调试 rpc调用
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/30 15:03
 */
@Data
public class SendGrabOrderApiVo implements Serializable {
    private Long sendUserId;
    private List<Long> invitationUserIds;
    private RoomType roomType;
}