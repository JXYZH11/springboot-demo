package com.jxyzh11.springbootdemo.config.exception.constants;

import com.jxyzh11.springbootdemo.config.exception.GlobalExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseEnum implements GlobalExceptionAssert {

    ID_IS_NULL(10001, "id不能为空")
    ;

    /**
     * 返回码
     */
    private int code;
    /**
     * 返回消息
     */
    private String message;
}