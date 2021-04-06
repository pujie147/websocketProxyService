package com.vdegree.february.im.ws.config;

import com.vdegree.february.im.api.IMCMDRouting;
import com.vdegree.february.im.ws.handler.BaseWsProxyHandle;
import com.vdegree.february.im.ws.handler.RoutingManger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * wsproxy handler 路由bean
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/27 10:19
 */
@Configuration
public class RoutingConfig {
    @Autowired
    private ApplicationContext context;
    /**
     * @Author DELL
     * @Date 15:52 2021/4/6
     * @Description 初始化路由规则
     * @param:
     * @Return com.vdegree.february.im.ws.handler.RoutingManger
     * @Exception
     **/
    @Bean
    public RoutingManger routingManager(){
        Map<String, Object> beanMap = context.getBeansWithAnnotation(IMCMDRouting.class);
        RoutingManger routingManger = new RoutingManger(beanMap.size());
        beanMap.keySet().forEach(beanName -> {
            Object bean = beanMap.get(beanName);
            Class clazz = bean.getClass();
            if (bean instanceof BaseWsProxyHandle && clazz.getAnnotation(IMCMDRouting.class) != null) {
                IMCMDRouting IMCMDRouting = (IMCMDRouting) clazz.getAnnotation(IMCMDRouting.class);
                routingManger.putHandler(IMCMDRouting.cmd().getType(), (BaseWsProxyHandle) bean);
            }
        });
        return routingManger;
    }
}
