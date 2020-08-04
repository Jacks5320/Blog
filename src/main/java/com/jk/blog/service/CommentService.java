package com.jk.blog.service;

import com.jk.blog.pojo.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> listCommentByBlogId(Long id);

    Comment saveComment(Comment comment);
}
