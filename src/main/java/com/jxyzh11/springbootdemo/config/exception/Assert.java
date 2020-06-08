package com.jxyzh11.springbootdemo.config.exception;

import com.jxyzh11.springbootdemo.config.exception.entity.GlobalException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 自定义assert接口
 */
public interface Assert {

    /**
     * 创建异常
     *
     * @param args
     * @return
     */
    GlobalException newException(Object... args);

    /**
     * 创建异常
     *
     * @param t
     * @param args
     * @return
     */
    GlobalException newException(Throwable t, Object... args);

    /**
     * 断言对象如果为空，抛出异常
     *
     * @param var 待判断对象
     * @throws Exception
     */
    default void notNull(Object var) throws Exception {
        if (var instanceof String) {
            String str = String.valueOf(var);
            if (StringUtils.isBlank(str) || "undefined".equals(str) || "null".equals(str)) {
                throw newException(var);
            }
        }
        if (var instanceof Integer || var instanceof Long) {
            if (var == null) {
                throw newException(var);
            }
        }
        if (var instanceof Boolean) {
            if (var == null) {
                throw newException(var);
            }
        }
        if (var instanceof List) {
            List list = (List) var;
            if (CollectionUtils.isEmpty(list)) {
                throw newException(var);
            }
        }
        if (var == null) {
            throw newException(var);
        }
    }

    /**
     * 断言对象如果为空，抛出异常
     *
     * @param var  待判断对象
     * @param args message占位符对应的参数列表
     * @throws Exception
     */
    default void notNull(Object var, Object... args) throws Exception {
        if (var instanceof String) {
            String str = String.valueOf(var);
            if (StringUtils.isBlank(str) || "undefined".equals(str) || "null".equals(str)) {
                throw newException(args);
            }
        }
        if (var instanceof Integer || var instanceof Long) {
            if (var == null) {
                throw newException(args);
            }
        }
        if (var instanceof Boolean) {
            if (var == null) {
                throw newException(args);
            }
        }
        if (var instanceof List) {
            List list = (List) var;
            if (CollectionUtils.isEmpty(list)) {
                throw newException(args);
            }
        }
        if (var == null) {
            throw newException(args);
        }
    }
}
