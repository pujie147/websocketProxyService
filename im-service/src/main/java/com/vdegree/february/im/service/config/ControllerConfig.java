package com.vdegree.february.im.service.config;

import com.vdegree.february.im.api.IMController;
import com.vdegree.february.im.service.ControllerManger;
import com.vdegree.february.im.service.handle.BaseImServiceHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/27 10:19
 */
@Configuration
public class ControllerConfig {
    @Autowired
    private ApplicationContext context;

    @Bean(name="controllerManager")
    public ControllerManger controllerManager(){
        Map<String, Object> beanMap = context.getBeansWithAnnotation(IMController.class);
        ControllerManger controllerManager = new ControllerManger(beanMap.size());
        for(String beanName:beanMap.keySet()){
            Object bean = beanMap.get(beanName);
            Class clazz = bean.getClass();
            if (bean instanceof BaseImServiceHandle && clazz.getAnnotation(IMController.class) != null) {
                IMController imController = (IMController) clazz.getAnnotation(IMController.class);
                controllerManager.put(imController.cmd().getType(), (BaseImServiceHandle) bean);

            }
        }
        return controllerManager;
    }
}
