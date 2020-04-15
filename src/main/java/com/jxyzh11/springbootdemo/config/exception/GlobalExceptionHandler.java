package com.jxyzh11.springbootdemo.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一异常处理
 *
 * @ClassName: GlobalExceptionHandler
 * @Description: TODO
 * @Author: JXYZH11
 * @Date: 2020/4/15 10:44
 * @Version: 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = GlobalException.class)
    public ExceptionResponse handleException(HttpServletRequest request, GlobalException e) {
        ErrorCodeInterface errorCode = e.getErrorCode();
        return new ExceptionResponse(errorCode.getCode(), errorCode.getMessage());
    }
}
