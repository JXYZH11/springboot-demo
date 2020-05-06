package com.jxyzh11.springbootdemo.config.exception.entity;

/**
 * 异常返回实体类型
 *
 * @ClassName: ResultBody
 * @Description: TODO
 * @Author: JXYZH11
 * @Date: 2020/4/15 14:59
 * @Version: 1.0
 */
public class ExceptionResponse {

    public Integer code;
    private String message;

    public ExceptionResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
