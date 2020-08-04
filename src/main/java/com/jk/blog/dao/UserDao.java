package com.jk.blog.dao;

import com.jk.blog.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User,Long> {

    User findByUserNameAndPassword(String userName,String password);

}
