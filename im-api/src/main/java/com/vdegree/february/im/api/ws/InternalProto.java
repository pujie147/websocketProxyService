package com.vdegree.february.im.api.ws;

import com.vdegree.february.im.api.im2ws.IM2WSProto;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.common.constant.type.PushType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/30 18:11
 */
@Data
public class InternalProto implements Serializable {
    private Long sendUserId;
    private List<Long> pustUserIds;
    private PushType pushType;

    private Long wsProxyStartTime;
    private Long wsProxyEndTime;
    private Long imServiceStartTime;
    private Long imServiceEndTime;

    private IM2WSProto im2WSProto;
    /**
     * {@link IMCMD} 的type为了防止 ws-proxy 没有更新是找不对应
     */
    private Integer imCMDType;

}
