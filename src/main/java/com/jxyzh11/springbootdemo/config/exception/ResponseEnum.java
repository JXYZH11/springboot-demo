package com.jxyzh11.springbootdemo.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseEnum implements BusinessExceptionAssert {

    /**
     * Bad licence type
     */
    ID_IS_NULL(10001, "id不能为空"),
    /**
     * Licence not found
     */
    SERVER_ERROR(10002, "服务器异常");

    /**
     * 返回码
     */
    private int code;
    /**
     * 返回消息
     */
    private String message;
}