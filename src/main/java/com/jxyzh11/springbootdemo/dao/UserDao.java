package com.jxyzh11.springbootdemo.dao;

import com.jxyzh11.springbootdemo.entity.User;
import org.springframework.stereotype.Component;

/**
 * @ClassName: UserDao
 * @Description: TODO
 * @Author: JXYZH11
 * @Date: 2020/4/14 16:24
 * @Version: 1.0
 */
public interface UserDao {

    public User get(User user);
}
