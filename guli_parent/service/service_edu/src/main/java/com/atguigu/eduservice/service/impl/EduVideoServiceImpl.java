package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.VodFeginClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author lk
 * @since 2020-08-14
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    //将删除多个视频的接口注入进来
    @Autowired
    private VodFeginClient feginClient;


    //1. 删除全部的小节根据课程id
    @Override
    public void removeVideoByCourseId(String courseId) {
        //删除小节之前需要先删除所有的视频
        //因为每个课程id会有多个视频
        //1.首先根据courseId查询出所有的视频
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        wrapperVideo.select("video_source_id");
        //这里面获取的是所有的VideoSourceId
        List<EduVideo> eduVideos = baseMapper.selectList(wrapperVideo);

        //将 List<EduVideo> => List<String>
        List<String> videoIds = new ArrayList<>();
        for (int i = 0; i < eduVideos.size(); i++) {
            EduVideo eduVideo = eduVideos.get(i);
            String videoSourceId = eduVideo.getVideoSourceId();
            if(!StringUtils.isEmpty(videoSourceId)) {
                videoIds.add(videoSourceId);
            }
        }

        if(videoIds.size() > 0) {
            //删除多个视频
            feginClient.deleteAliyunVideoBatch(videoIds);
        }

        //删除小节
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
