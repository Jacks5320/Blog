package com.jk.blog.dao;

import com.jk.blog.pojo.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentDao extends JpaRepository<Comment,Long> {

    List<Comment> findByBlogId(Long blogId,Sort sort);

    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);

    List<Comment> findByBlogIdAndParentCommentIsNull(Long blogId, Sort sort);
}
