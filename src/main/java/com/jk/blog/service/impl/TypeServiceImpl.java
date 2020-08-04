package com.jk.blog.service.impl;

import com.jk.blog.dao.TypeDao;
import com.jk.blog.exception.NotFoundException;
import com.jk.blog.pojo.Type;
import com.jk.blog.service.TypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeDao typeDao;

    @Transactional//事务
    @Override
    public Type saveType(Type type) {
        return typeDao.save(type);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        typeDao.deleteById(id);
    }

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeDao.getOne(id);
        if (t == null) {
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(type, t);

        return typeDao.save(t);
    }

    @Transactional
    @Override
    public Type getType(Long id) {
        return typeDao.getOne(id);
    }

    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeDao.findAll(pageable);
    }

    @Override
    public List<Type> listType() {
        return typeDao.findAll();
    }

    @Override
    public List<Type> listTypeTop(Integer size) {
        //排序后的pageable
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "blogs.size"));
        return typeDao.findTop(pageable);
    }

    @Override
    public Type findByName(String name) {
        return typeDao.findByName(name);
    }
}
