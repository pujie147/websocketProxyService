package com.vdegree.february.im.api.rpc;

import com.vdegree.february.im.common.constant.type.RoomType;

import java.util.List;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/30 11:43
 */

public interface PublicAppServiceApi {
    void sendGrabOrderApi(Long sendUserId, List<Long> invitationUserIds, RoomType roomType);
}
