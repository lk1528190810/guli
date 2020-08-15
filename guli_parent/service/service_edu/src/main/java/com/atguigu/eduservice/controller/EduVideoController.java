package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author lk
 * @since 2020-08-14
 */
@RestController
@RequestMapping("/eduservice/video")
@Api(description = "这是关于小节的controller")
@CrossOrigin
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;

    //添加小节
    @ApiOperation(value = "添加小节的方法")
    @PostMapping("/addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        videoService.save(eduVideo);
        return R.ok();
    }

    //删除小节
    //TODO 这个还需要完善 删除小节时视频也应该删除
    @ApiOperation(value = "删除小节的方法")
    @DeleteMapping("/deleteVideo/{videoId}")
    public R deleteVideo(@PathVariable String videoId){
        videoService.removeById(videoId);
        return R.ok();
    }

    //修改小节
    @ApiOperation(value = "修改小节的方法")
    @PostMapping("/updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){
        videoService.updateById(eduVideo);
        return R.ok();
    }

    //根据video的id查询出对应的小节
    @ApiOperation(value = "根据video的id查询出对应的小节")
    @GetMapping("/getVideoById/{videoId}")
    public R getVideoById(@PathVariable String videoId){
        EduVideo eduVideo = videoService.getById(videoId);
        System.out.println("eduVideo = " + eduVideo);
        return R.ok().data("video",eduVideo);
    }

}

