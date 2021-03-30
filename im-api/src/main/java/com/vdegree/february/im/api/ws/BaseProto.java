package com.vdegree.february.im.api.ws;

import com.vdegree.february.im.common.constant.type.IMCMD;
import lombok.Data;

import java.io.Serializable;


/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/15 16:47
 */
@Data
public class BaseProto<T> implements Serializable {
    private IMCMD cmd;
    private Long requestTime;
    private Long responseTime;
    private String requestId;
    private T message;
}
