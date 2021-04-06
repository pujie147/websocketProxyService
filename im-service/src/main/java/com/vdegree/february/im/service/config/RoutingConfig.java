package com.vdegree.february.im.service.config;

import com.vdegree.february.im.api.IMCMDRouting;
import com.vdegree.february.im.service.communication.MQRoutingManger;
import com.vdegree.february.im.service.handle.BaseImServiceHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.Map;

/**
 * handler 路由 bean
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
     * @Date 15:18 2021/4/6
     * @Description 初始化 路由映射规则
     * @param: 
     * @Return com.vdegree.february.im.service.communication.MQRoutingManger 
     * @Exception 
     **/
    @Bean(name= "routingManger")
    public MQRoutingManger routingManger(){
        Map<String, Object> beanMap = context.getBeansWithAnnotation(IMCMDRouting.class);
        MQRoutingManger controllerManager = new MQRoutingManger(beanMap.size());
        for(String beanName:beanMap.keySet()){
            Object bean = beanMap.get(beanName);
            Class clazz = bean.getClass();
            if (bean instanceof BaseImServiceHandle && clazz.getAnnotation(IMCMDRouting.class) != null) {
                IMCMDRouting IMCMDRouting = (IMCMDRouting) clazz.getAnnotation(IMCMDRouting.class);
                controllerManager.put(IMCMDRouting.cmd().getType(), (BaseImServiceHandle) bean);
            }
        }
        return controllerManager;
    }

    /**
     * 保留 为切换handler使用
     * 可以在项目启动时热更新路由规则
     * @param routingManger
     * @return
     */
    @Bean(name= "referenceRoutingManger")
    @DependsOn(value={"routingManger"})
    public Long referenceRoutingManger(MQRoutingManger routingManger){
        String newBeanName = "swConfirmInvitationHandle";
        Object newBean = context.getBean(newBeanName);
        if(newBean.getClass().isAnnotationPresent(IMCMDRouting.class)){
            if(newBean instanceof BaseImServiceHandle) {
                routingManger.put(newBean.getClass().getAnnotation(IMCMDRouting.class).cmd().getType(), (BaseImServiceHandle) newBean);
            }
        }
        return null;
    }
}
