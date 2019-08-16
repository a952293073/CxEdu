package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {
    @Autowired
    private CourseService courseService;

    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        TeachplanNode teachplanNode=courseService.findTeachplanList(courseId);
        System.out.println(teachplanNode);
        return teachplanNode;
    }

    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
        return courseService.addTeachplan(teachplan);
    }

    @Override
    @GetMapping("/coursebase/list/{page}/{size}")
    public QueryResponseResult getCourseBaseList(@PathVariable("page") int page, @PathVariable("size") int size) {
        return courseService.getCourseBaseList(page,size);
    }

    @Override
    @PostMapping("/coursebase/add")
    public AddCourseResult addCourseBase(@RequestBody CourseBase courseBase) {
        return courseService.addCourseBase(courseBase);
    }

    @Override
    @PostMapping("/coursemarket/add/{id}")
    public AddCourseResult addCourseMarket(@PathVariable("id")String id, @RequestBody CourseMarket courseMarket) {
        return courseService.saveCourseMarket(id,courseMarket);
    }

    @Override
    @PostMapping("/coursebase/update/{id}")
    public AddCourseResult updateCourseBase(@PathVariable("id")String id, @RequestBody CourseBase courseBase) {

        return courseService.updateCourseBase(id,courseBase);
    }

    @Override
    @GetMapping("/coursebase/get/{id}")
    public CourseBase findCourseBaseById(@PathVariable("id") String id) {
        return courseService.findCourseBaseById(id);
    }

    @Override
    @GetMapping("/courseMarket/get/{id}")
    public CourseMarket getCourseMarketById(@PathVariable("id") String id) {
        return courseService.getCourseMarketByid(id);
    }

}
