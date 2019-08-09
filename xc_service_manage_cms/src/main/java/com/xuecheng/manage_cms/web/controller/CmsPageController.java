package com.xuecheng.manage_cms.web.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {
    @Autowired
    private CmsPageService cmsPageService;
    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {

        QueryResponseResult queryResponseResult = cmsPageService.findList(page, size, queryPageRequest);
        return queryResponseResult;
    }

    @Override
    @PostMapping("/add")
    public CmsPageResult addCmsPage(@RequestBody CmsPage cmsPage) {
        return cmsPageService.addCmsPage(cmsPage);
    }

    @Override
    @GetMapping("/get/{pageId}")
    public CmsPage getCmsPageByPageId(@PathVariable("pageId") String pageId) {
        return cmsPageService.getCmsPageByPageId(pageId);
    }

    @Override
    @PutMapping("/edit/{pageId}")
    public CmsPageResult updateCmsPage(@PathVariable("pageId") String pageId,@RequestBody CmsPage cmsPage) {
        return cmsPageService.updateCmsPage(pageId,cmsPage);
    }

    @Override
    @DeleteMapping("/delete/{pageId}")
    public CmsPageResult deleteCmsPage(@PathVariable("pageId") String pageId) {

        return cmsPageService.deleteCmsPage(pageId);
    }
}
