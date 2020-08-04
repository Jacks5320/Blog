package com.jk.blog.service;

import com.jk.blog.pojo.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {

    Type saveType(Type type);//增

    void deleteType(Long id);//删

    Type updateType(Long id, Type type);//改

    Type getType(Long id);//查

    Page<Type> listType(Pageable pageable);//分页

    List<Type> listType();

    List<Type> listTypeTop(Integer size);

    Type findByName(String name);

}
