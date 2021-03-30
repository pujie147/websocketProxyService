package com.vdegree.february.im.api.ws;

import com.vdegree.february.im.common.constant.type.ErrorEnum;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/15 16:48
 */
@Data
@Log4j2
public class ResponseProto<T> extends BaseProto {

    private String errorInfo = ErrorEnum.SUCCESS_INFO;
    private Integer errorCode = ErrorEnum.SUCCESS_CODE;
    private T message;

    public static ResponseProto buildResponse(RequestProto requestProto){
        ResponseProto responseProto = new ResponseProto();
        responseProto.setCmd(requestProto.getCmd());
        responseProto.setSendUserId(requestProto.getSendUserId());
        responseProto.setRequestTime(requestProto.getRequestTime());
        responseProto.setWsProxyStartTime(requestProto.getWsProxyStartTime());
        responseProto.setRequestId(responseProto.getRequestId());
        return responseProto;
    }

    public void setError(ErrorEnum errorEnum) {
        errorInfo = errorEnum.getErrorInfo();
        errorCode = errorEnum.getErrorCode();
    }

    public static ResponseProto buildResponse(RequestProto requestProto, ErrorEnum errorEnum){
        ResponseProto responseProto = buildResponse(requestProto);
        responseProto.setError(errorEnum);
        return responseProto;
    }

}
