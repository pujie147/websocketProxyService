package com.vdegree.february.im.common.utils.agora;


import com.vdegree.february.im.common.utils.agora.media.RtcTokenBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * TODO 缺少配置 声网配置实现
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/16 16:55
 */
@Component
public class RtcTokenBuilderUtil {
    @Value("${appId:9f9caa27687e4bc6b9441190874384b4}")
    private String appId;
    @Value("${appCertificate:3cc79e41f82048858821aa7de4fec432}")
    private String appCertificate;
    @Value("${expirationTimeInSeconds:86400}")
    private int expirationTimeInSeconds;

    public String build(Integer userId,String roomId){
        RtcTokenBuilder token = new RtcTokenBuilder();
        int timestamp = (int)(System.currentTimeMillis() / 1000 + expirationTimeInSeconds);
        String result = token.buildTokenWithUid(appId, appCertificate,
                roomId, userId, RtcTokenBuilder.Role.Role_Publisher, timestamp);
        return result;
    }

}
