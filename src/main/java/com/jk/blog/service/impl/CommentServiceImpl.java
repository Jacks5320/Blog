package com.jk.blog.service.impl;

import com.jk.blog.dao.CommentDao;
import com.jk.blog.pojo.Comment;
import com.jk.blog.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = Sort.by("createTime");
        //取到根评论列表
        List<Comment> comments = commentDao.findByBlogIdAndParentCommentNull(blogId, sort);
        return eachComment(comments);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId != -1) {
            comment.setParentComment(commentDao.getOne(parentCommentId));
        } else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentDao.save(comment);
    }



    /**
     * 循环每个顶级的评论节点
     *
     * @param comments 父节点（根节点）
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments) {
        //中转父节点列表
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment, c);//将comment复制到c
            commentsView.add(c);
        }
        // 合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    // 存放所有子代的集合
    private List<Comment> tempReplies = new ArrayList<>();
    /**
     * @param comments root根节点，blog不为空的对象集合
     * @return
     */
    private void combineChildren(List<Comment> comments) {
        for (Comment comment : comments) {
            //获取回复子集
            List<Comment> replies = comment.getReplyComments();
            for (Comment reply : replies) {
                recursively(reply);//以二级评论作为根，递归查找
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplies);
            //清除临时存放区
            tempReplies = new ArrayList<>();
        }
    }

    /**
     * 递归迭代出所有的子评论
     *
     * @param comment 被迭代的二级评论对象
     * @return
     */
    private void recursively(Comment comment) {
        tempReplies.add(comment);    //顶节点添加的临时存放集合
        if (comment.getReplyComments().size() > 0) {
            List<Comment> replies = comment.getReplyComments();
            for (Comment reply : replies) {
                recursively(reply);
            }
        }
    }
}
