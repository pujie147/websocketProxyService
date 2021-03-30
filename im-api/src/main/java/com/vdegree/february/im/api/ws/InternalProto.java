package com.vdegree.february.im.api.ws;

import com.vdegree.february.im.common.constant.type.PushType;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/30 18:11
 */
@Data
public class InternalProto {
    private Long sendUserId;
    private List<Long> pustUserIds;
    private PushType pushType;

    private Long wsProxyStartTime;
    private Long wsProxyEndTime;
    private Long imServiceStartTime;
    private Long imServiceEndTime;

}
