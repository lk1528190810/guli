package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    //获取到最终发布课程的所有信息
    @ApiOperation(value = "获取到最终发布课程的所有信息")
    @GetMapping("/getCoursePublishVoById/{courseId}")
    public R getCoursePublishVoById(@PathVariable String courseId){
        CoursePublishVo coursePublishVoById = courseService.getCoursePublishVoById(courseId);
        return R.ok().data("coursePublishVo",coursePublishVoById);
    }

    //最终发布
    @ApiOperation(value = "最终发布")
    @PostMapping("/publicCourse/{courseId}")
    public R publicCourse(@PathVariable String courseId){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(courseId);
        eduCourse.setStatus("Normal");
        courseService.updateById(eduCourse);
        return R.ok();
    }

    //查询所有已经发布的课程
    //查询所有已经发布的课程带分页
    @ApiOperation(value = "查询所有已经发布的课程带分页")
    @PostMapping("/getListCourse/{current}/{limit}")
    public R getListCourse(@PathVariable("current") long current,
                           @PathVariable("limit") long limit,
                           @RequestBody(required = false) CourseQuery courseQuery){

        //创建一个Page对象
        Page<EduCourse> page = new Page<>(current,limit);

        //条件判断 ， 动态sql需要进行拼接
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();

        String title = courseQuery.getTitle();
        String status = courseQuery.getStatus();

        if(!StringUtils.isEmpty(title)){
            wrapper.eq("title",title);
        }
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("status",status);
        }

        courseService.page(page,wrapper);

        //根据创建时间降序排序
        wrapper.orderByDesc("gmt_modified");
        List<EduCourse> records = page.getRecords();// 每页的数据信息
        System.out.println("records = " + records);
        long total = page.getTotal(); //总记录数
        //总记录数
        return R.ok().data("total",total).data("rows",records);

    }

    @ApiOperation(value = "删除课程")
    @DeleteMapping("/deleteCourse/{courseId}")
    public R deleteCourse(@PathVariable String courseId){
        courseService.removeCourse(courseId);
        return R.ok();
    }

}

