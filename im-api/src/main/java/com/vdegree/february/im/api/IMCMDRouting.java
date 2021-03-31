package com.vdegree.february.im.api;

import com.vdegree.february.im.common.constant.type.IMCMD;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * IMCMD 路由标识注解
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/27 10:12
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface IMCMDRouting {
    IMCMD cmd();
    @AliasFor(annotation = Component.class, attribute = "value")
    String value() default "";
}
