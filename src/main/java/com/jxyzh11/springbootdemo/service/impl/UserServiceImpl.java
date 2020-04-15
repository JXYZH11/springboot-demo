package com.jxyzh11.springbootdemo.service.impl;

import com.jxyzh11.springbootdemo.dao.UserDao;
import com.jxyzh11.springbootdemo.entity.User;
import com.jxyzh11.springbootdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: UserService
 * @Description: TODO
 * @Author: JXYZH11
 * @Date: 2020/4/14 16:27
 * @Version: 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao dao;

    public User get(User user) {
        int num = 1 / 0;
        return dao.get(user);
    }
}
