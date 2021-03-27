package com.vdegree.february.im.api.ws;

import com.vdegree.february.im.common.constant.ErrorEnum;
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
public class ReponseProto extends BaseProto {

    public static ReponseProto buildReponse(RequestProto requestProto){
        ReponseProto reponseProto = new ReponseProto();
        try {
            reponseProto.putAll(requestProto);
            reponseProto.setError(ErrorEnum.SUCCESS);
        } catch (Exception e) {
            log.error(e);
        }
        reponseProto.setReponseTime(System.currentTimeMillis());
        return reponseProto;
    }

    public void setError(ErrorEnum errorEnum) {
        this.put("errorInfo",errorEnum.getErrorInfo());
        this.put("errorCode",errorEnum.getErrorCode());
    }

    public static ReponseProto buildReponse(RequestProto requestProto, ErrorEnum errorEnum){
        ReponseProto reponseProto = buildReponse(requestProto);
        reponseProto.setError(errorEnum);
        return reponseProto;
    }

}
