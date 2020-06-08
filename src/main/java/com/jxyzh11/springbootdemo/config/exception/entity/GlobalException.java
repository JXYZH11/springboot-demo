package com.jxyzh11.springbootdemo.config.exception.entity;

import com.jxyzh11.springbootdemo.config.exception.IOpenapiResponseEnum;
import com.jxyzh11.springbootdemo.config.exception.IResponseEnum;

/**
 * 基础异常实体类
 *
 * @ClassName: BaseException
 * @Description: TODO
 * @Author: JXYZH11
 * @Date: 2020/4/28 15:22
 * @Version: 1.0
 */
public class GlobalException extends Exception {

    public IResponseEnum responseEnum;
    public IOpenapiResponseEnum responseV3Enum;
    public Object[] args;
    public String message;
    public Throwable cause;

    public GlobalException(IResponseEnum responseEnum, Object[] args, String message) {
        this.responseEnum = responseEnum;
        this.args = args;
        this.message = message;
    }

    public GlobalException(IResponseEnum responseEnum, Object[] args, String message, Throwable cause) {
        this.responseEnum = responseEnum;
        this.args = args;
        this.message = message;
        this.cause = cause;
    }

    public GlobalException(IOpenapiResponseEnum responseV3Enum, Object[] args, String message) {
        this.responseV3Enum = responseV3Enum;
        this.args = args;
        this.message = message;
    }

    public GlobalException(IOpenapiResponseEnum responseV3Enum, Object[] args, String message, Throwable cause) {
        this.responseV3Enum = responseV3Enum;
        this.args = args;
        this.message = message;
        this.cause = cause;
    }

    public IResponseEnum getResponseEnum() {
        return responseEnum;
    }

    public void setResponseEnum(IResponseEnum responseEnum) {
        this.responseEnum = responseEnum;
    }

    public IOpenapiResponseEnum getResponseV3Enum() {
        return responseV3Enum;
    }

    public void setResponseV3Enum(IOpenapiResponseEnum responseV3Enum) {
        this.responseV3Enum = responseV3Enum;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }
}
