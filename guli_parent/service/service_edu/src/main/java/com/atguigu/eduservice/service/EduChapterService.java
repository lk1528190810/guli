package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author lk
 * @since 2020-08-14
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getAllChapterVideo(String courseId);

    boolean deleteChapterById(String chapterId);

    //2. 删除全部的章节根据课程id
    void removeChapterByCourseId(String courseId);
}
