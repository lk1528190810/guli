package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.service.EduChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author lk
 * @since 2020-08-14
 */
@RestController
@RequestMapping("/eduservice/chapter")
@CrossOrigin
@Api(description = "查询章节和小节的controller")
public class EduChapterController {

    @Autowired
    private EduChapterService chapterService;

    @ApiOperation(value = "这是查询章节和小节的方法")
    @GetMapping("/getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable String  courseId){
        List<ChapterVo> list = chapterService.getAllChapterVideo(courseId);
        return R.ok().data("allChapterVideo",list);
    }

    //新增chapter的方法
    @ApiOperation(value = "新增chapter的方法")
    @PostMapping("/addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter){
        chapterService.save(eduChapter);
        return R.ok();
    }

    //根据chapterId查询章节
    @ApiOperation(value = "根据chapterId查询章节")
    @GetMapping("/getChapterById/{chapterId}")
    public R getChapterById(@PathVariable String chapterId){
        EduChapter eduChapter = chapterService.getById(chapterId);
        System.out.println("eduChapter = " + eduChapter);
        return R.ok().data("chapter",eduChapter);
    }

    //修改chapter的方法
    @ApiOperation(value = "修改chapter的方法")
    @PostMapping("/updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter){
        chapterService.updateById(eduChapter);
        return R.ok();
    }

    //删除chapter的方法
    @ApiOperation(value = "删除chapter的方法")
    @DeleteMapping("/deleteChapter/{chapterId}")
    public R deleteChapter(@PathVariable String chapterId){
        boolean flag = chapterService.deleteChapterById(chapterId);
        if(flag){
            return R.ok();
        }else {
            return R.error();
        }
    }
}

