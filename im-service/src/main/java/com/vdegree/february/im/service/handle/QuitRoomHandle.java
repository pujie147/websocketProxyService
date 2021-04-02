package com.vdegree.february.im.service.handle;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.vdegree.february.im.api.IMCMDRouting;
import com.vdegree.february.im.api.ws.ProtoContext;
import com.vdegree.february.im.api.ws.RequestProto;
import com.vdegree.february.im.api.ws.message.push.QuitRoomPushMsg;
import com.vdegree.february.im.api.ws.message.request.QuitRoomRequestMsg;
import com.vdegree.february.im.common.cache.RoomDataRedisManger;
import com.vdegree.february.im.common.cache.UserDataRedisManger;
import com.vdegree.february.im.common.constant.type.ErrorEnum;
import com.vdegree.february.im.common.constant.type.IMCMD;
import com.vdegree.february.im.common.constant.type.RoomType;
import com.vdegree.february.im.service.communication.PushManager;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * 1.接收客户端发起的聊天邀请
 * 2.回调appservice判断是否有可以邀请
 * 3.push 被邀请用户客户端
 * 4.return
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 19:56
 */
@IMCMDRouting(cmd = IMCMD.REQUEST_QUIT_ROOM)
public class QuitRoomHandle implements BaseImServiceHandle {
    @Autowired
    private PushManager pushManager;
    @Autowired
    private Gson gson;
    @Autowired
    private RoomDataRedisManger roomDataRedisManger;
    @Autowired
    private UserDataRedisManger userDataRedisManger;

    @Override
    public ProtoContext execute(ProtoContext protoContext) {
        RequestProto<QuitRoomRequestMsg> msg = gson.fromJson(protoContext.getJson(),new TypeToken<RequestProto<QuitRoomRequestMsg>>(){}.getType());
        String roomId = msg.getMessage().getRoomId();
        RoomType roomType = msg.getMessage().getRoomType();
        if(!StringUtil.isNullOrEmpty(roomId)){
            if(roomId.startsWith(roomType.getRoomPrefix())) {
                try {
                    Long sendUserId = roomDataRedisManger.getSendUserId(roomId);
                    Long invitedUserId = roomDataRedisManger.getInvitedUserId(roomId);
                    ArrayList<Long> pushUserids = Lists.newArrayList(sendUserId, invitedUserId);
                    pushUserids.remove(protoContext.getInternalProto().getSendUserId());
                    pushManager.pushProto(IMCMD.PUSH_QUIT_ROOM, new QuitRoomPushMsg(roomId, roomType), pushUserids);
                    userDataRedisManger.delRoomId(sendUserId);
                    userDataRedisManger.delRoomId(invitedUserId);
                    // 删除房间session信息
                    roomDataRedisManger.delete(roomId);
                    //TODO 回调appService 退出房间
                    return protoContext.buildSuccessResponseProto();
                }catch (Exception e){
                    protoContext.buildFailResponseProto(ErrorEnum.PARAM_ERROR);
                }
            }
        }
        return protoContext.buildFailResponseProto(ErrorEnum.PARAM_ERROR);
    }

}
