package com.vdegree.february.im.api.handle.proxy;

import com.vdegree.february.im.api.BaseHandle;
import com.vdegree.february.im.api.ws.base.reponse.ReponseProto;
import com.vdegree.february.im.api.ws.base.request.RequestProto;
import io.netty.channel.group.DefaultChannelGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 21:02
 */
@Component
public class PushHandler implements BaseHandle {

    @Override
    public ReponseProto exector(RequestProto requestProto) {
        return null;
    }
}
