package com.vdegree.february.im.common.routing;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.List;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/4/6 18:55
 */

@Data
@AllArgsConstructor
public class HandlerInfo {
    private Object clazz;
    private Method method;
    private List<Class> params;

}
