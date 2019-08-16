package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "课程管理",description = "课程管理查询修改新增")
public interface CourseControllerApi {
@ApiOperation("课程计划查询")
public TeachplanNode findTeachplanList(String courseId);
@ApiOperation("增加课程计划")
public ResponseResult addTeachplan(Teachplan teachplan);
@ApiOperation("查询课程列表")
@ApiImplicitParams({
        @ApiImplicitParam(name="page",value = "页码",required=true,paramType="path",dataType="int" ),
        @ApiImplicitParam(name="size",value = "每页记录数",required=true,paramType="path",dataType="int" )
})
    public QueryResponseResult getCourseBaseList(int page,int size);
@ApiOperation("新增课程")
    public AddCourseResult addCourseBase(CourseBase courseBase);
    @ApiOperation("新增课程营销")
    public AddCourseResult addCourseMarket(String id,CourseMarket courseMarket);

    @ApiOperation("修改课程")
    public AddCourseResult updateCourseBase(String id,CourseBase courseBase);
@ApiOperation("查询课程")
 public CourseBase findCourseBaseById(String id);
    @ApiOperation("查询课程营销信息")
    public CourseMarket getCourseMarketById(String id);
}
