package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CmsPageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;

    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 20;
        }
        Pageable pageable = new PageRequest(page, size);
        CmsPage cmsPage = new CmsPage();

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("pageName",ExampleMatcher.GenericPropertyMatchers.contains());
        if (!StringUtils.isBlank(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if (!StringUtils.isBlank(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        if (!StringUtils.isBlank(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        if (!StringUtils.isBlank(queryPageRequest.getPageName())) {
            cmsPage.setPageName(queryPageRequest.getPageName());
        }
        if (!StringUtils.isBlank(queryPageRequest.getPageType())) {
            cmsPage.setPageType(queryPageRequest.getPageType());
        }
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        QueryResult<CmsPage> queryResult = new QueryResult<>();
        queryResult.setList(all.getContent());
        queryResult.setTotal(all.getTotalElements());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    public CmsPageResult addCmsPage(CmsPage cmsPage) {

        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());

        if (cmsPage1 != null) {

            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        cmsPage.setPageId(null);

        cmsPage = cmsPageRepository.save(cmsPage);

        return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
    }

    public CmsPage getCmsPageByPageId(String pageId) {
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (optional.isPresent()){
            return optional.get();
        }
        return null;

    }

    public CmsPageResult updateCmsPage(String pageId, CmsPage cmsPage) {
        CmsPage one = this.getCmsPageByPageId(pageId);
        if (one != null) {
                one.setTemplateId(cmsPage.getTemplateId());
//更新所属站点
                one.setSiteId(cmsPage.getSiteId());
//更新页面别名
                one.setPageAliase(cmsPage.getPageAliase());
//更新页面名称
                one.setPageName(cmsPage.getPageName());
//更新访问路径
                one.setPageWebPath(cmsPage.getPageWebPath());
//更新物理路径
                one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
               cmsPage = cmsPageRepository.save(one);
               if (cmsPage!=null){
                   return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
               }

        }

        return new CmsPageResult(CommonCode.FAIL, null);

    }

    public CmsPageResult deleteCmsPage(String pageId) {
        CmsPage cmsPage = this.getCmsPageByPageId(pageId);
        if (cmsPage!=null){
            cmsPageRepository.delete(cmsPage);
            return new CmsPageResult(CommonCode.SUCCESS,null);
        }
        return new CmsPageResult(CommonCode.FAIL,null);

    }
}


