package com.vdegree.february.im.ws.handler;

import com.google.common.collect.Maps;

import java.util.HashMap;

/**
 * wsproxy handler 映射关系保存类
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/27 11:51
 */
public class RoutingManger extends HashMap<Integer, BaseWsProxyHandle> {
    public RoutingManger(int initialCapacity) {
        Maps.newHashMapWithExpectedSize(initialCapacity);
    }

    public BaseWsProxyHandle getHandler(Integer key) {
        return super.get(key);
    }

    public BaseWsProxyHandle putHandler(Integer key, BaseWsProxyHandle value) {
        return super.put(key, value);
    }


}
