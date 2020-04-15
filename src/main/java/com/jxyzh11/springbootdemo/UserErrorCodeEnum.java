package com.jxyzh11.springbootdemo;

import com.jxyzh11.springbootdemo.config.exception.ErrorCodeInterface;

public enum UserErrorCodeEnum implements ErrorCodeInterface {

    id_not_be_null(1,"id不能为空")
    ;

    private Integer code;
    private String message;

    UserErrorCodeEnum(Integer code, String message) {
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
