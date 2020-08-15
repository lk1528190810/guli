package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author lk
 * @since 2020-08-14
 */
@Api(description="课程管理")
@CrossOrigin //跨域
@RestController
@RequestMapping("/eduservice/course")
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    @ApiOperation(value = "新增课程")
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        String id = courseService.addCourseInfo(courseInfoVo);
        return R.ok().data("courseId",id);
    }


    @ApiOperation(value = "获取课程信息用于回显")
    @GetMapping("/getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfoVo = courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfo",courseInfoVo);
    }

    @ApiOperation(value = "更新课程信息回数据库")
    @PostMapping("/updateCourseInfo")
    public R getCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    //最终发布
    @ApiOperation(value = "最终发布")
    @GetMapping("/getCoursePublishVoById/{courseId}")
    public R getCoursePublishVoById(@PathVariable String courseId){
        CoursePublishVo coursePublishVoById = courseService.getCoursePublishVoById(courseId);
        return R.ok().data("coursePublishVo",coursePublishVoById);
    }
}

