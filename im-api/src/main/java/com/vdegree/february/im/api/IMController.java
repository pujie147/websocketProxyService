package com.vdegree.february.im.api;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/27 10:12
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface IMController {
    IMCMD cmd();
}
