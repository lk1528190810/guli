package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author lk
 * @since 2020-08-14
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService videoService;

    @Override
    public List<ChapterVo> getAllChapterVideo(String courseId) {
        //1. 根据课程id查询出所有的章节
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);

        //2. 根据课程的id查询出所有的小节
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        List<EduVideo> eduVideoList = videoService.list(wrapperVideo);

        //用于储存最后返回的结果
        List<ChapterVo> finalList = new ArrayList<>();

        //3.遍历所有的章节
        for (int i = 0; i < eduChapterList.size(); i++) {
            //每一个章节
            EduChapter eduChapter = eduChapterList.get(i);
            //将每一个章节对象转换成ChapterVo对象
            ChapterVo chapterVo = new ChapterVo();
            //将chapterVo => chapterVo
            BeanUtils.copyProperties(eduChapter,chapterVo);
            //再将chapterVo加入到finalList中
            finalList.add(chapterVo);

            //用来存储小节
            List<VideoVo> videoVoList = new ArrayList<>();

            //4. 遍历所有的小节
            for (int j = 0; j < eduVideoList.size(); j++) {
                //每一个小节
                EduVideo eduVideo = eduVideoList.get(j);
                //判断每一个小节的章节id和每一个章节的id是否相等如果相等就将该小节设置到该章节中
                if(eduVideo.getChapterId().equals(eduChapter.getId())){
                    VideoVo videoVo = new VideoVo();
                    //把 eduVideo => VideoVo
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    //将videoVo添加到videoVoList中
                    videoVoList.add(videoVo);
                }
            }

            //将 videoVoList 放到每一个章节对象中去
            chapterVo.setChildren(videoVoList);
        }
        return finalList;
    }

    //删除章节
    @Override
    public boolean deleteChapterById(String chapterId) {
        //删除章节的时候需要先查询是否有小节
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        int count = videoService.count(wrapper);
        if(count > 0){
            throw  new GuliException(20001,"该章节中有小节还无法删除");
        }else {
            int result = baseMapper.deleteById(chapterId);
            return  result > 0;
        }
    }

    //2. 删除全部的章节根据课程id
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
