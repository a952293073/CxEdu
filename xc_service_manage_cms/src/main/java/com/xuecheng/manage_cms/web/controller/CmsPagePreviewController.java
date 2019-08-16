package com.xuecheng.manage_cms.web.controller;

import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class CmsPagePreviewController {
    @Autowired
    private CmsPageService cmsPageService;

    @GetMapping("/cms/preview/{pageId}")
    public void preview(@PathVariable("pageId")String pageId, HttpServletResponse response){
        String pageHtml = cmsPageService.getPageHtml(pageId);
        System.out.println(pageHtml);
        if (StringUtils.isEmpty(pageHtml)){
            ExceptionCast.cast(CmsCode.CMS_COURSE_PERVIEWISNULL);
        }
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(pageHtml.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
