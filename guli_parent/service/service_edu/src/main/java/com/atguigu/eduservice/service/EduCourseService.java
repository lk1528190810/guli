package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author lk
 * @since 2020-08-14
 */
public interface EduCourseService extends IService<EduCourse> {

    String addCourseInfo(CourseInfoVo courseInfoVo);

    //更新课程信息回数据库
    void updateCourseInfo(CourseInfoVo courseInfoVo);

    //获取课程信息用于回显
    CourseInfoVo getCourseInfo(String courseId);

    CoursePublishVo getCoursePublishVoById(String courseId);

    //根据课程id删除课程
    void removeCourse(String courseId);
}
