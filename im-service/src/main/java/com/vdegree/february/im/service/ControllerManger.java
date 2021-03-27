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
public class ControllerManger extends HashMap<Integer, BaseImServiceHandle> {
    public ControllerManger(int initialCapacity) {
        Maps.newHashMapWithExpectedSize(initialCapacity);
    }

    public BaseImServiceHandle getHandler(Integer key) {
        return super.get(key);
    }

    public BaseImServiceHandle putHandler(Integer key, BaseImServiceHandle value) {
        return super.put(key, value);
    }


}
