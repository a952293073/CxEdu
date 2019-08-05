package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {
    @Autowired
    CmsPageRepository cmsPageRepository;
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

}