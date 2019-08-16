package com.xuecheng.manage_cms_client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

@Service
public class PageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    CmsSiteRepository cmsSiteRepository;
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    GridFSBucket gridFSBucket;
    //将页面html保存到页面物理路径
    public void savePageToServerPath(String pageId){
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()){
                ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
            }
        CmsPage cmsPage = optional.get();
        String siteId = cmsPage.getSiteId();
        Optional<CmsSite> optionalSite = cmsSiteRepository.findById(siteId);
        if (!optionalSite.isPresent()){
                ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        CmsSite cmsSite = optionalSite.get();
       String pagePath=cmsPage.getPagePhysicalPath()+cmsPage.getPageName();
       //获取页面文件
        String htmlFileId = cmsPage.getHtmlFileId();
        InputStream inputStream=getHtml(htmlFileId);
        if (inputStream==null){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        FileOutputStream fileOutputStream=null;
        try {
            File file1 = new File(cmsPage.getPagePhysicalPath());
            file1.mkdirs();
            File file = new File(pagePath);

            fileOutputStream=new FileOutputStream(file);
            IOUtils.copy(inputStream,fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private InputStream getHtml(String htmlFileId) {

        try {
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(new Criteria("_id").is(htmlFileId)));
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
           return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
