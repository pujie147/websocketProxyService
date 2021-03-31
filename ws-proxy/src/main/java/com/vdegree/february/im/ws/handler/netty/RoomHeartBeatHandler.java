package com.vdegree.february.im.ws.handler.netty;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/15 15:50
 */

import com.vdegree.february.im.api.ws.RoomHeartBeatProto;
import com.vdegree.february.im.common.cache.RoomHeartBeatRedisManger;
import com.vdegree.february.im.common.constant.ChannelAttrConstant;
import com.vdegree.february.im.ws.cache.CacheChannelGroupManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import lombok.extern.log4j.Log4j2;
import org.checkerframework.checker.units.qual.A;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: 处理消息的handler
 * TextWebSocketFrame： 在netty中，是用于为websocket专门处理文本的对象，frame是消息的载体
 *  这里已经指定了类型 如果这里是Object 那么下面还需判断是不是TextWebSocketFrame类型
 */
@Log4j2
@Component
@ChannelHandler.Sharable
public class RoomHeartBeatHandler extends SimpleChannelInboundHandler<RoomHeartBeatProto> {

    @Autowired
    private RoomHeartBeatRedisManger roomHeartBeatRedisManger;

    @Autowired
    private CacheChannelGroupManager cacheChannelGroupManager;

    @Autowired
    RabbitTemplate rabbitTemplate;




    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RoomHeartBeatProto msg)
            throws Exception {
        Long userId = ctx.channel().attr(ChannelAttrConstant.USERID).get();
        cacheChannelGroupManager.refreshRoom(userId);
        //TODO 把Hearbeat redis Manager 和 UserData RedisManger 合并 同一处理 1、生成 2、刷新 3、查询（判断用户是否有效） 4、失效
        //TODO 把RoomHearbeat redis Manager 和 RoomData RedisManger 合并 同一处理 1、生成 2、刷新 3、查询（判断用户是否有效） 4、失效
    }
}