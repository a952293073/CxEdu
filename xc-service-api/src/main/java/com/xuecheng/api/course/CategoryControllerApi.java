package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "分类管理",description = "分类管理查询修改新增")
public interface CategoryControllerApi {

@ApiOperation("查询分类列表" )
public CategoryNode findCategoryList();
}
