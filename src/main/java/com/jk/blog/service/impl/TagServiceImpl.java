package com.jk.blog.service.impl;

import com.jk.blog.dao.TagDao;
import com.jk.blog.exception.NotFoundException;
import com.jk.blog.pojo.Tag;
import com.jk.blog.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagDao tagDao;

    @Transactional//事务
    @Override
    public Tag saveTag(Tag tag) {
        return tagDao.save(tag);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagDao.deleteById(id);
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = tagDao.getOne(id);
        if (t == null){
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(tag,t);

        return tagDao.save(t);
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagDao.getOne(id);
    }

    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagDao.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return tagDao.findAll();
    }

    @Override
    public List<Tag> listTag(String ids) {

        return tagDao.findAllById(convertToList(ids));
    }

    @Override
    public List<Tag> listTagTop(Integer size) {

        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "blogs.size"));
        return tagDao.findTop(pageable);
    }

    public List<Long> convertToList(String ids){
        List<Long> list = new ArrayList<>();

        if (!"".equals(ids) && ids != null){
            String[] idarray = ids.split(",");
            for (int i = 0; i< idarray.length;i++){
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }

    @Override
    public Tag getTagByName(String name) {
        return tagDao.findByName(name);
    }
}
