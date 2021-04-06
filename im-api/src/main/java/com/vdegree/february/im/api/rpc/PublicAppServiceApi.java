package com.vdegree.february.im.api.rpc;

import com.vdegree.february.im.common.constant.type.RoomType;

import java.util.List;

/**
 * imService 对外提供的service
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/30 11:43
 */

public interface PublicAppServiceApi {
    boolean sendGrabOrderApi(Long sendUserId, List<Long> invitationUserIds, RoomType roomType);

    boolean pushQuitRoomApi(String roomId, RoomType roomType);

    boolean pushDisConnected(Long userId);
}
