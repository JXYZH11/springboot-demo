package com.jxyzh11.springbootdemo.config.exception.entity;

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

    public IResponseEnum getResponseEnum() {
        return responseEnum;
    }

    public void setResponseEnum(IResponseEnum responseEnum) {
        this.responseEnum = responseEnum;
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
