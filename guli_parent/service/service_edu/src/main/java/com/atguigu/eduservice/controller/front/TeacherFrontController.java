package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@Api(description = "这是前台讲师分页")
@RestController
@RequestMapping("/eduservice/teacherfront")
@CrossOrigin
public class TeacherFrontController {

    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;

    //1.前台讲师的分页查询
    @ApiOperation(value = "这是前台讲师分页的方法")
    @GetMapping("/getTeacherList/{currentPage}/{limit}")
    public R getTeacherList(@PathVariable long currentPage,@PathVariable long limit){
        Page<EduTeacher> pageTeacher = new Page<>(currentPage,limit);
        //为了存储方便将数据存在map集合里面
        Map<String,Object> map = teacherService.getTeacherList(pageTeacher);
        return R.ok().data(map);
    }

    //2. 根据teacherId查询出teacher 的信息和他所交的课程
    @ApiOperation(value = "根据teacherId查询出teacher 的信息和他所交的课程")
    @GetMapping("/getTeacherInfo/{teacherId}")
    public R getTeacherInfo(@PathVariable("teacherId") String teacherId){
        System.out.println("teacherId = " + teacherId);
        //教师的详细信息
        EduTeacher teacher = teacherService.getById(teacherId);
        System.out.println("teacher = " + teacher);
        //根据教师的id查询出所交的课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",teacherId);
        List<EduCourse> courseList = courseService.list(wrapper);
        System.out.println("courseList = " + courseList);
        return R.ok().data("teacher",teacher).data("courseList",courseList);
    }
}
