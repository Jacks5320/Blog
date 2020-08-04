package com.jk.blog.service.impl;

import com.jk.blog.dao.UserDao;
import com.jk.blog.pojo.User;
import com.jk.blog.service.UserService;
import com.jk.blog.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User checkUser(String userName, String password) {
        User user = userDao.findByUserNameAndPassword(userName, MD5Utils.code(password));
        return user;
    }
}
