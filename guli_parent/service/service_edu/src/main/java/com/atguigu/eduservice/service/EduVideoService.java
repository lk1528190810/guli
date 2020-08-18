package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author lk
 * @since 2020-08-14
 */
public interface EduVideoService extends IService<EduVideo> {

    //1. 删除全部的小节根据课程id
    void removeVideoByCourseId(String courseId);
}
