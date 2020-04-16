package com.jxyzh11.springbootdemo.config.redis.entity;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * @author sunny
 * @date 2018/3/19
 */
public class RedisObject implements Serializable {

    private static final long serialVersionUID = 6125787238288049947L;
    private Object value;
    private Double score;

    public RedisObject() {}

    public RedisObject(Object value, Double score) {
        this.value = value;
        this.score = score;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RedisObject that = (RedisObject) o;
        return Objects.equal(value, that.value) &&
                Objects.equal(score, that.score);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value, score);
    }
}
