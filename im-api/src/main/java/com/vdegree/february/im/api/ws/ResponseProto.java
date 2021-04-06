package com.vdegree.february.im.api.ws;

import com.vdegree.february.im.common.constant.type.ErrorEnum;
import com.vdegree.february.im.common.constant.type.IMCMD;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.data.repository.config.RepositoryNameSpaceHandler;

/**
 * imService -> client 响应给客户端的协议
 * 于该 ${@link RequestProto} 对象成对出现
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/15 16:48
 */
@Data
@Log4j2
public class ResponseProto<T> extends BaseProto {
    private T message;
    private String errorInfo = ErrorEnum.SUCCESS_INFO;
    private Integer errorCode = ErrorEnum.SUCCESS_CODE;

    public static ResponseProto buildResponse(BaseProto baseProto){
        ResponseProto responseProto = new ResponseProto();
        BeanUtils.copyProperties(baseProto,responseProto);
        return responseProto;
    }

    public void setError(ErrorEnum errorEnum) {
        errorInfo = errorEnum.getErrorInfo();
        errorCode = errorEnum.getErrorCode();
    }

    public static ResponseProto buildResponse(BaseProto baseProto, ErrorEnum errorEnum){
        ResponseProto responseProto = buildResponse(baseProto);
        responseProto.setError(errorEnum);
        return responseProto;
    }

}
