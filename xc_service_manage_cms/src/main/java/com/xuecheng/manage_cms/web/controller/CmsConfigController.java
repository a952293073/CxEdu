package com.xuecheng.manage_cms.web.controller;

import com.xuecheng.api.cms.CmsConfigControllerApi;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.service.CmsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/cms/config")
public class CmsConfigController implements CmsConfigControllerApi {
    @Autowired
    private CmsConfigService cmsConfigService;


    @Override
    @GetMapping("/getmodel/{id}")
    @ResponseBody
    public CmsConfig getModelById(@PathVariable("id") String id) {
        return  cmsConfigService.getModelById(id);

    }

    @RequestMapping("/getmodel1/{id}")
    public String getModel(@PathVariable("id") String id, Map<String,Object> map) {
       map.putAll(cmsConfigService.getModel(id));

        return "index_banner";


    }
}
