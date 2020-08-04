package com.jk.blog.service.impl;

import com.jk.blog.dao.BlogDao;
import com.jk.blog.exception.NotFoundException;
import com.jk.blog.pojo.Blog;
import com.jk.blog.pojo.Type;
import com.jk.blog.service.BlogService;
import com.jk.blog.utils.MarkdownUtils;
import com.jk.blog.utils.MyBeanUtils;
import com.jk.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogDao blogDao;

    @Transactional
    @Override
    public Blog getBlog(Long id) {
        return blogDao.getOne(id);
    }

    /**
     * markdown格式转http格式
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogDao.getOne(id);
        if (blog == null) {
            throw new NotFoundException("未找到");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(b.getContent()));
        blogDao.updateViews(id);//更新浏览次数
        return b;
    }

    /**
     * 评论
     * @param pageable
     * @param blog
     * @return
     */
    @Transactional
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {

        return blogDao.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(criteriaBuilder.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                if (blog.getTypeId() != null) {
                    predicates.add(criteriaBuilder.equal(root.<Type>get("type"), blog.getTypeId()));
                }
                if (blog.isRecommend()) {
                    predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogDao.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogDao.findByQuery(query, pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {

        return blogDao.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join join = root.join("tags");
                return criteriaBuilder.equal(join.get("id"), tagId);
            }
        }, pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "updateTime"));
        return blogDao.findTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogDao.findGroupYear();
        System.out.println(years.toString());
        Map<String, List<Blog>> map = new LinkedHashMap<>();
        for (String year : years) {
            map.put(year, blogDao.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogDao.count();
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }
        return blogDao.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogDao.getOne(id);
        if (b == null) {
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyNames(blog));//blog的内容保存到b,第三个参数过滤掉null值
        b.setUpdateTime(new Date());
        return blogDao.save(b);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogDao.deleteById(id);
    }
}
