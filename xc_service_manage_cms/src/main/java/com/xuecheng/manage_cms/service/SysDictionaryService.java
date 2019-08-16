package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_cms.dao.SysDictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysDictionaryService {
    @Autowired
    private SysDictionaryRepository sysDictionaryRepository;

    public SysDictionary findSysDictionary(String dType) {
//
        SysDictionary sysDictionary = sysDictionaryRepository.findByDType(dType);
         if (sysDictionary==null){
             ExceptionCast.cast(CommonCode.SERVER_ERROR);
         }
        return sysDictionary;
    }
}
