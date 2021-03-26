package com.vdegree.february.im.common.utils;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 17:23
 */

import org.apache.commons.beanutils.BeanUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author czx
 * @Date 2019-07-18 11:12
 * @Description:
 **/
public class MapUtil {
    /**
     * 使用BeanUtils工具类 将Map转为Bean
     * jar包地址
     * <dependency>
     * <groupId>commons-io</groupId>
     * <artifactId>commons-io</artifactId>
     * <version>2.6</version>
     * </dependency>
     *
     * @param classz
     * @param map
     * @param <T>
     * @return
     */
    public static <T> T toBean(Class<T> classz, Map map) {
        T bean = null;
        try {
            bean = classz.newInstance();
            BeanUtils.populate(bean, map);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return bean;
    }

    /**
     * 将Map转为Bean
     *
     * @param type
     * @param map
     * @param <T>
     * @return
     * @throws IntrospectionException    获取类属性异常
     * @throws IllegalAccessException    创建Bean对象异常
     * @throws InstantiationException    创建Bean对象异常
     * @throws InvocationTargetException 对象转换异常
     */
    public static <T> T toBean2(Class<T> type, Map map) throws IntrospectionException, IllegalAccessException, InstantiationException, InvocationTargetException {
        BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
        T bean = type.newInstance(); // 创建 JavaBean 对象
        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (map.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                Object value = map.get(propertyName);
                Object[] args = new Object[1];
                args[0] = value;
                descriptor.getWriteMethod().invoke(bean, args);
            }
        }
        return bean;
    }

    /**
     * 将Bean对象转为Map
     *
     * @param bean
     * @return
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Map convertBean(Object bean)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }
}