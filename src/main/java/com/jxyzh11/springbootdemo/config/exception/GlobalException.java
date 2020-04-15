package com.jxyzh11.springbootdemo.config.exception;

/**
 * 统一错误码异常
 *
 * @ClassName: GlobalException
 * @Description: TODO
 * @Author: JXYZH11
 * @Date: 2020/4/15 10:53
 * @Version: 1.0
 */
public class GlobalException extends Exception {

    private ErrorCodeInterface errorCode;

    public GlobalException(ErrorCodeInterface errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCodeInterface getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCodeInterface errorCode) {
        this.errorCode = errorCode;
    }
}