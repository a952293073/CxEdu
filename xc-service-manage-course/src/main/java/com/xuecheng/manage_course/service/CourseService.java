package com.xuecheng.manage_course.service;


import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.*;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private TeachplanMapper teachplanMapper;
    @Autowired
    private TeachplanBaseRepository teachplanBaseRepository;
    @Autowired
    private CourseBaseRepository courseBaseRepository;
    @Autowired
    private CategoryBaseRepository categoryBaseRepository;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CourseMarketRepository courseMarketRepository;
    public TeachplanNode findTeachplanList(String courseId) {

        TeachplanNode teachplanNode=teachplanMapper.findListByCourseId(courseId);
        return teachplanNode;
    }

    public ResponseResult addTeachplan(Teachplan teachplan) {
        if (teachplan==null || StringUtils.isEmpty(teachplan.getCourseid()) || StringUtils.isEmpty(teachplan.getPname()) ){
           ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        String courseid = teachplan.getCourseid();
        String parentid = teachplan.getParentid();

        if (StringUtils.isEmpty(parentid)){
            parentid = getTeachplanRoot(courseid);
            teachplan.setParentid(parentid);
        }

        Optional<Teachplan> teachplanParent = teachplanBaseRepository.findById(parentid);
        if (!teachplanParent.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        if (teachplanParent.get().getGrade().equals("1")){
            teachplan.setGrade("2");
        }else {
            teachplan.setGrade("3");
        }

           teachplan = teachplanBaseRepository.save(teachplan);


        return ResponseResult.SUCCESS();
    }

    //获取课程根结点，如果没有则添加根结点
    public String getTeachplanRoot(String courseId) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()){
            return null;
        }
        CourseBase courseBase = optional.get();
        List<Teachplan> teachplans= teachplanBaseRepository.findByCourseidAndParentid(courseId,"0");
        if (teachplans == null || teachplans.size()==0){
            Teachplan teachplanRoot = new Teachplan();
            teachplanRoot.setParentid("0");
            teachplanRoot.setGrade("1");
            teachplanRoot.setCourseid(courseId);
            teachplanRoot.setPname(courseBase.getName());
            teachplanRoot.setStatus("0");
            teachplanBaseRepository.save(teachplanRoot);
            return teachplanRoot.getId();
        }
        Teachplan teachplan = teachplans.get(0);
        return teachplan.getId();
    }


    public QueryResponseResult getCourseBaseList(int page, int size) {
        Pageable pageable = new PageRequest(page-1, size);
        Page<CourseBase> all = courseBaseRepository.findAll(pageable);
        List<CourseBase> courseBases = all.getContent();
        if (courseBases==null || courseBases.size()==0){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        QueryResult<CourseBase> queryResult = new QueryResult<>();
        queryResult.setList(courseBases);
        queryResult.setTotal(all.getTotalElements());

        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }

    public AddCourseResult addCourseBase(CourseBase courseBase) {
       courseBase = courseBaseRepository.save(courseBase);
       if (courseBase==null){
           ExceptionCast.cast(CommonCode.FAIL);
       }
        return new AddCourseResult(CommonCode.SUCCESS,courseBase.getId());
    }

    public CourseBase findCourseBaseById(String id) {
        Optional<CourseBase> optional = courseBaseRepository.findById(id);
        if (!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        return optional.get();
    }

    public AddCourseResult updateCourseBase(String id, CourseBase courseBase) {
        Optional<CourseBase> optional = courseBaseRepository.findById(id);
        if (!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        CourseBase courseBaseOne = optional.get();
          courseBaseOne.setDescription(courseBase.getDescription());
          courseBaseOne.setGrade(courseBase.getGrade());
          courseBaseOne.setMt(courseBase.getMt());
          courseBaseOne.setName(courseBase.getName());
          courseBaseOne.setSt(courseBase.getSt());
          courseBaseOne.setStatus(courseBase.getStatus());
          courseBaseOne.setStudymodel(courseBase.getStudymodel());
          courseBaseOne.setUsers(courseBase.getUsers());

        return addCourseBase(courseBaseOne);
    }

    public AddCourseResult saveCourseMarket(String id, CourseMarket courseMarket) {
        Optional<CourseMarket> optional = courseMarketRepository.findById(id);
        if (!optional.isPresent()){
            CourseMarket courseMarketOne = courseMarketRepository.save(courseMarket);
            return new AddCourseResult(CommonCode.SUCCESS,courseMarketOne.getId());
        }
//        rge: "203002"
//        endTime: ""
//        expiration: []
//        expires: ""
//        id: "297e7c7c62b888f00162b8a7dec20000"
//        price: "100"
//        qq: "12345"
//        startTime: ""
//        users: ""
//        valid: "204001"
        CourseMarket courseMarketOne = optional.get();
        courseMarketOne.setCharge(courseMarket.getCharge());
        courseMarketOne.setEndTime(courseMarket.getEndTime());
        courseMarketOne.setStartTime(courseMarket.getStartTime());
        courseMarketOne.setQq(courseMarket.getQq());
        courseMarketOne.setPrice_old(courseMarketOne.getPrice());
        courseMarketOne.setPrice(courseMarket.getPrice());
        courseMarketOne.setValid(courseMarket.getValid());
        courseMarketOne = courseMarketRepository.save(courseMarket);
        return new AddCourseResult(CommonCode.SUCCESS,courseMarketOne.getId());

    }

    public CourseMarket getCourseMarketByid(String id) {
        Optional<CourseMarket> optional = courseMarketRepository.findById(id);
        if (!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        return optional.get();
    }
}
