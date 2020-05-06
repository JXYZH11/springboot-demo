package com.jxyzh11.springbootdemo.config.exception;

import java.text.MessageFormat;

public interface GlobalExceptionAssert extends IResponseEnum, Assert {

    @Override
    default GlobalException newException(Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);

        return new GlobalException(this, args, msg);
    }

    @Override
    default GlobalException newException(Throwable t, Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);

        return new GlobalException(this, args, msg, t);
    }

}