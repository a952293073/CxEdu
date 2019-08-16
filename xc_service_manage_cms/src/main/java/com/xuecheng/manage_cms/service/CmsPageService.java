package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class CmsPageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;
    @Autowired
    private RabbitTemplate rabbitTemplate;
//    @Value("${xuecheng.mq.routingkey}")
//    private String routingkey;
    /**
     * 查询页面列表
     *
     * @param page
     * @param size
     * @param queryPageRequest
     * @return
     */
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
                .withMatcher("pageName", ExampleMatcher.GenericPropertyMatchers.contains());
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

    /**
     * 新增页面
     *
     * @param cmsPage
     * @return
     */
    public CmsPageResult addCmsPage(CmsPage cmsPage) {

        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());

        if (cmsPage1 != null) {

            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        cmsPage.setPageId(null);

        cmsPage = cmsPageRepository.save(cmsPage);

        return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
    }

    /**
     * 获取页面信息
     *
     * @param pageId
     * @return
     */
    public CmsPage getCmsPageByPageId(String pageId) {
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;

    }

    /**
     * 更新页面
     *
     * @param pageId
     * @param cmsPage
     * @return
     */
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
            one.setDataUrl(cmsPage.getDataUrl());
            cmsPage = cmsPageRepository.save(one);
            if (cmsPage != null) {
                return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
            }

        }

        return new CmsPageResult(CommonCode.FAIL, null);

    }

    /**
     * 删除页面
     *
     * @param pageId
     * @return
     */
    public CmsPageResult deleteCmsPage(String pageId) {
        CmsPage cmsPage = this.getCmsPageByPageId(pageId);
        if (cmsPage != null) {
            cmsPageRepository.delete(cmsPage);
            return new CmsPageResult(CommonCode.SUCCESS, null);
        }
        return new CmsPageResult(CommonCode.FAIL, null);

    }

    /**
     * 发布页面
     * @param pageId
     * @return
     */
    public ResponseResult postPage(String pageId) {
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String siteId = optional.get().getSiteId();

        String pageHtml = getPageHtml(pageId);
        savePageHtml(pageId, pageHtml);
        sendPostPage(pageId,siteId);
      return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 发送页面消息
     * @param pageId
     * @param siteId
     */
    private void sendPostPage(String pageId,String siteId) {


        Map<String, String> map = new HashMap<>();
        map.put("pageId",pageId);
        String json = JSON.toJSONString(map);

        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE,siteId,json);
        System.out.println("已发送消息");
    }

    /**
     * 保存静态页面
     * @param pageId
     * @param pageHtml
     * @return
     */
    private CmsPage savePageHtml(String pageId, String pageHtml) {
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        CmsPage cmsPage = optional.get();
        if (cmsPage.getHtmlFileId()!=null){
            gridFsTemplate.delete(Query.query(new Criteria("_id").is(cmsPage.getHtmlFileId())));
        }

            InputStream inputStream = IOUtils.toInputStream(pageHtml);
            ObjectId objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());
            cmsPage.setHtmlFileId(objectId.toString());
             cmsPageRepository.save(cmsPage);
        return cmsPage;
    }

    /**
     * 页面静态化
     *
     * @param pageId
     * @return
     */
    public String getPageHtml(String pageId) {

        //1.获取页面模型
        Map model = this.getModelByPageId(pageId);
        if (model == null) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        //3、获取模板
        String template = this.getTemplateIdByPageId(pageId);
        if (StringUtils.isEmpty(template)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //4、页面静态化
        String html = genericHtml(template,model);
        if (StringUtils.isEmpty(html)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        return html;
    }

    private String genericHtml(String template, Map model) {

        try {
            Configuration configuration = new Configuration(Configuration.getVersion());
            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            stringTemplateLoader.putTemplate("template",template);
            configuration.setTemplateLoader(stringTemplateLoader);
            Template template1 = configuration.getTemplate("template");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template1, model);
            return html;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取页面模板
     *
     * @param pageId
     * @return
     */
    private String getTemplateIdByPageId(String pageId) {
        CmsPage cmsPage = this.getCmsPageByPageId(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String templateId = cmsPage.getTemplateId();

        if (StringUtils.isEmpty(templateId)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if (optional.isPresent()) {
            CmsTemplate cmsTemplate = optional.get();
            String templateFileId = cmsTemplate.getTemplateFileId();

            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));

            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
            try {
                String context = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
                return context;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取页面模型
     *
     * @param pageId
     * @return
     */
    private Map getModelByPageId(String pageId) {
        CmsPage cmsPage = this.getCmsPageByPageId(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String dataUrl = cmsPage.getDataUrl();
        ResponseEntity<Map> entity = restTemplate.getForEntity(dataUrl, Map.class);
        System.out.println("map:"+entity.getBody());
        return entity.getBody();
    }
}


