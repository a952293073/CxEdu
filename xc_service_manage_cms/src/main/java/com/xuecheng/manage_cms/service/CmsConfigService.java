package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
public class CmsConfigService {
    @Autowired
    private CmsConfigRepository cmsConfigRepository;
    @Autowired
    private RestTemplate restTemplate;
    public CmsConfig getModelById(String id) {
        Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
        if (optional.isPresent()){

            return optional.get();
        }
        return null ;
    }

    public Map getModel(String id) {
        String url="http://localhost:31001/cms/config/getmodel/";
        ResponseEntity<Map> entity = restTemplate.getForEntity(url + id, Map.class);
        if (entity.getBody()!=null){
            System.out.println(entity.getBody());
            return entity.getBody();
        }
        return null ;
    }
}
