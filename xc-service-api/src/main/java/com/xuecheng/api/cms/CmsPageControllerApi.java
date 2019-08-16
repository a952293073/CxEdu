package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value="cms页面管理接口",description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {
    //页面查询
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value = "页码",required=true,paramType="path",dataType="int"),
            @ApiImplicitParam(name="size",value = "每页记录数",required=true,paramType="path",dataType="int")
    })
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
    @ApiOperation("新增页面")
    public CmsPageResult addCmsPage(CmsPage cmsPage);
    @ApiOperation("发布页面")
    public ResponseResult postCmsPageHtml(String pageId);
    @ApiOperation("根据pageId获取页面")
    public CmsPage getCmsPageByPageId(String pageId);
    @ApiOperation("根据pageId修改页面")
    public CmsPageResult updateCmsPage(String pageId,CmsPage cmsPage);
    @ApiOperation("根据pageId删除页面")
    public CmsPageResult deleteCmsPage(String pageId);
}
