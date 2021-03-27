package com.vdegree.february.im.common.utils;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 19:51
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    // 通过name获取 Bean.
    public Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    // 通过class获取Bean.
    public <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    // 通过name,以及Clazz返回指定的Bean
    public <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    // 通过name,以及Clazz返回指定的Bean
    public <T> T getBean(Class<T> clazz, Class<T> clazzTarget) {
        return applicationContext.getBean(clazz,clazzTarget);
    }

    // 添加bean
    public void setBean(Object object) {
        applicationContext.getAutowireCapableBeanFactory().autowireBean(object);
    }

}