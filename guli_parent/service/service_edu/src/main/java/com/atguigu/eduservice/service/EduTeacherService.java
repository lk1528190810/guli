package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author lk
 * @since 2020-08-10
 */
public interface EduTeacherService extends IService<EduTeacher> {

    //为了存储方便将数据存在map集合里面
    Map<String, Object> getTeacherList(Page<EduTeacher> pageTeacher);
}
