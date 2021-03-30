package com.vdegree.february.im.service;

import com.google.common.collect.Maps;
import com.vdegree.february.im.service.handle.BaseImServiceHandle;

import java.util.HashMap;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/27 11:51
 */
public class MQRoutingManger extends HashMap<Integer, BaseImServiceHandle> {
    public MQRoutingManger(int initialCapacity) {
        Maps.newHashMapWithExpectedSize(initialCapacity);
    }
}
