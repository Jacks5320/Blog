package com.jk.blog.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
* @description: 异常控制
* @author: Jacks丶
* @date:
* @version: 1.0
*/
@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler//异常处理
    public ModelAndView exceptionHandler(HttpServletRequest request,Exception e) throws Exception {

        logger.error("Request URL: {},Exception:{}",request.getRequestURL(),e);
        /*
        捕获异常类和异常状态码，不为空则抛出异常，跳转到异常页。
        若为空，跳转到错误页
         */
        if(AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null){
            throw e;
        }
        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("exception",e);
        mv.setViewName("error/error");//error目录下的error页面

        return mv;
    }
}
