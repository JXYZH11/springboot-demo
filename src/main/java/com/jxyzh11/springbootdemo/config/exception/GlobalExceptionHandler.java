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

    @ExceptionHandler(value = BaseException.class)
    public ExceptionResponse handleException(HttpServletRequest request, BaseException e) {
        log.error(e.getMessage(), e);
        return new ExceptionResponse(e.getResponseEnum().getCode(), e.getMessage());
    }

    /**
     * 未定义异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = Exception.class)
    public ExceptionResponse handleException(Exception e) {
        log.error(e.getMessage(), e);
        return new ExceptionResponse(ResponseEnum.SERVER_ERROR.getCode(), ResponseEnum.SERVER_ERROR.getMessage());
    }
}
