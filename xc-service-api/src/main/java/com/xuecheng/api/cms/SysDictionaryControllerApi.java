package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "字典查询",description = "字典查询")
public interface SysDictionaryControllerApi {
    @ApiOperation("课程等级查询")
    public SysDictionary getSysDictionary(String dType);
}
