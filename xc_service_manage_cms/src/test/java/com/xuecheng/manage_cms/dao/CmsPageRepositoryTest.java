package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {
    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    private CmsPageService cmsPageService;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    GridFsTemplate gridFsTemplate;
    //分页测试
    @Test
    public void testFindPage() {
//        int page = 0;//从0开始
//        int size = 10;//每页记录数
//        Pageable pageable = PageRequest.of(page,size);
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName("测试页面");
//        cmsPageRepository.insert(cmsPage);
        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println(all);
    }
@Test
    public void restTemplate(){
    ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f", Map.class);
    Map body = forEntity.getBody();
    System.out.println(body);
}
@Test
    public void getPageHtml(){
    String pageHtml = cmsPageService.getPageHtml("5d50e4ee9505241d38467481");
    System.out.println(pageHtml);
}
@Test
    public void gridFStest() throws FileNotFoundException {
    File file = new File("d:/index_banner.ftl");
    FileInputStream fileInputStream = new FileInputStream(file);
    ObjectId id = gridFsTemplate.store(fileInputStream, "index_banner.ftl");
    System.out.println(id);
}
}