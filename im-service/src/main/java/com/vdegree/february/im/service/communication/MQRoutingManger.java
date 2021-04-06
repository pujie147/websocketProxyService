package com.vdegree.february.im.service.communication;

import com.google.common.collect.Maps;
import com.vdegree.february.im.service.handle.BaseImServiceHandle;

import java.util.HashMap;

/**
 * handler的路由管理
 * 保存路由映射关系
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/27 11:51
 */
public class MQRoutingManger extends HashMap<Integer, HandlerInfo> {
    public MQRoutingManger(int initialCapacity) {
        Maps.newHashMapWithExpectedSize(initialCapacity);
    }
}
