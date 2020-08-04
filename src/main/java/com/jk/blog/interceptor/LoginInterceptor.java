package com.jk.blog.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @description: 拦截器，未登录不能进入后台操作页面
* @author: Jacks丶
* @date:
* @version: 1.0
*/
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        if (request.getSession().getAttribute("user") == null){
            response.sendRedirect("/admin");//重定向
            return false;
        }
        return true;
    }
}
