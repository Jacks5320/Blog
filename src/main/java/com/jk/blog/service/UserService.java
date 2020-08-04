package com.jk.blog.service;

import com.jk.blog.pojo.User;

public interface UserService {
    User checkUser(String userName,String password);
}
