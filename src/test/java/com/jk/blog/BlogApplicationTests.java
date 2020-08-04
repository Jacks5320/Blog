package com.jk.blog;

import com.jk.blog.pojo.Comment;
import com.jk.blog.service.CommentService;
import com.jk.blog.utils.MD5Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class BlogApplicationTests {


    @Test
    void contextLoads() {
        String psw = MD5Utils.code("123456");
        System.out.println(psw);
    }
}
