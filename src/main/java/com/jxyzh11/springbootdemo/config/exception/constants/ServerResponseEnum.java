package com.jxyzh11.springbootdemo.config.exception.constants;

import com.jxyzh11.springbootdemo.config.exception.GlobalExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 服务级别异常常数
 */
@Getter
@AllArgsConstructor
public enum ServerResponseEnum implements GlobalExceptionAssert {

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