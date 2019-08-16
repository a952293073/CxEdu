package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.dao.CategoryBaseRepository;
import com.xuecheng.manage_course.dao.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {


    @Autowired
    private CategoryBaseRepository categoryBaseRepository;
    @Autowired
    private CategoryMapper categoryMapper;


    public CategoryNode findCategoryList() {
        List<CategoryNode> categoryNode = categoryMapper.findList();
        CategoryNode list = new CategoryNode();
        list.setChildren(categoryNode);
        return list;
    }

}
