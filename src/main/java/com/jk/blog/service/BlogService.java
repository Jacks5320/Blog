package com.jk.blog.service;

import com.jk.blog.pojo.Blog;
import com.jk.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface BlogService {
    //根据id查询一条博客
    Blog getBlog(Long id);
    //查询出博客，并将正文部分的markdown格式转换成html格式进行页面渲染
    Blog getAndConvert(Long id);
    //根据标签查询博客列表
    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);
    //根据
    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(String query, Pageable pageable);

    Page<Blog> listBlog(Long tagId, Pageable pageable);

    List<Blog> listRecommendBlogTop(Integer size);

    Map<String,List<Blog>> archiveBlog();

    Long countBlog();

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id, Blog blog);

    void deleteBlog(Long id);
}
