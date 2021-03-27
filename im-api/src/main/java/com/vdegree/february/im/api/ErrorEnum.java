package com.vdegree.february.im.api;

/**
 * 异常码
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/15 17:25
 */
public enum ErrorEnum {

    /**
     * common 异常 公共使用
     * code 范围 0 - 999
     */
    // 成功无异常
    SUCCESS(ErrorEnum.SUCCESS_CODE,ErrorEnum.SUCCESS_INFO,"没有错误,执行完成"),


    /**
     * websocket 异常 websocket 使用
     * code范围 1001 - 1300;
     */
    // 心跳异常用户失效
    HEART_BEAT_ERROR(1001,"心跳异常","用户失效"),


    ;

    ErrorEnum(int errorCode, String errorInfo, String desc) {
        this.errorCode = errorCode;
        this.errorInfo = errorInfo;
        this.desc = desc;
    }

    /**
     * @Author DELL
     * @Date 14:49 2021/3/26
     * @Description 为成功无异常reponse default 值
     **/
    public static final int SUCCESS_CODE=0;
    public static final String SUCCESS_INFO="success";

    private int errorCode;
    private String errorInfo;
    private String desc;

    @Override
    public String toString() {
        return "ErrorEnum{" +
                "errorCode=" + errorCode +
                ", errorInfo='" + errorInfo + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }}
