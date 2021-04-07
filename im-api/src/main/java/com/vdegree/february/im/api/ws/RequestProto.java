package com.vdegree.february.im.api.ws;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;


/**
 * client -> imservice 要处理的请求和参数
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/15 16:26
 */
@Data
public class RequestProto<T> implements Serializable {
    private Integer msgRandom;
    private Map message;

}
