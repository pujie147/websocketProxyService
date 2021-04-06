package com.vdegree.february.im.ws.config;

import com.vdegree.february.im.common.constant.WSPorxyBroadcastConstant;
import com.vdegree.february.im.common.utils.IPUtils;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 赋值queuename 保证每个服务的queuename唯一
 *
 * @author dell
 * @version 1.0
 * @date 2021/4/6 16:15
 */
@Configuration
public class MQConfig {
    private static final String QUEUE_NAME= WSPorxyBroadcastConstant.QUEUE_NAME+"."+ IPUtils.getLocalHostIP();

    @Bean
    public Queue queue(){
        return new Queue(QUEUE_NAME,true);
    }
}
