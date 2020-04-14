package com.jxyzh11.springbootdemo.service;

import com.jxyzh11.springbootdemo.dao.UserDao;
import com.jxyzh11.springbootdemo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: UserService
 * @Description: TODO
 * @Author: JXYZH11
 * @Date: 2020/4/14 16:27
 * @Version: 1.0
 */
public interface UserService {

    public User get(User user);
}
