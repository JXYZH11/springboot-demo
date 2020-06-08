package com.jxyzh11.springbootdemo.config.exception;

import com.jxyzh11.springbootdemo.config.exception.entity.GlobalException;

import java.text.MessageFormat;

/**
 * 全局异常Assert接口
 */
public interface OpenapiExceptionAssert extends IOpenapiResponseEnum, Assert {

    @Override
    default GlobalException newException(Object... args) {
        String msg = MessageFormat.format(this.getError_desc(), args);

        return new GlobalException(this, args, msg);
    }

    @Override
    default GlobalException newException(Throwable t, Object... args) {
        String msg = MessageFormat.format(this.getError_desc(), args);

        return new GlobalException(this, args, msg, t);
    }

}