package com.jxyzh11.springbootdemo.entity;

import lombok.Data;

/**
 * @ClassName: User
 * @Description: TODO
 * @Author: JXYZH11
 * @Date: 2020/4/14 16:24
 * @Version: 1.0
 */
public class User {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
