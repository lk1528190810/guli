package com.atguigu.eduvod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.*;
import com.atguigu.commonutils.R;
import com.atguigu.eduvod.service.VodService;
import com.atguigu.eduvod.utils.AliyunVodSDKUtils;
import com.atguigu.eduvod.utils.ConstantVodUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(description="阿里云视频点播微服务")
@RequestMapping("/eduvod/video")
@RestController
@CrossOrigin
public class VodController {

    @Autowired
    private VodService vodService;

    //这是文件上传视频的方法
    @ApiOperation(value = "这是文件上传视频的方法")
    @PostMapping("/uploadAliyunVideo")
    public R uploadAliyunVideo(MultipartFile file){
        String videoId = vodService.uploadAliyunVideo(file);
        return R.ok().data("videoId",videoId);
    }

    @DeleteMapping("/deleteAliyunVideo/{videoSourceId}")
    public R deleteAliyunVideo(@PathVariable String videoSourceId){
        try {
            //1.初始化客户端对象
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //2. 创建request和response对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //3.设置请求的id
            request.setVideoIds(videoSourceId);
            //4.调用client方法
            DeleteVideoResponse response = client.getAcsResponse(request);

            return R.ok();
        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"删除视频失败");
        }
    }

    //删除多个视频
    @DeleteMapping("/deleteAliyunVideoBatch")
    public R deleteAliyunVideoBatch(@RequestParam("videoList") List<String> videoList) {
        vodService.deleteAliyunVideoBatch(videoList);
        return R.ok();
    }

    //获取凭证的接口
    @GetMapping("/getAuth/{vid}")
    public R getAuth(@PathVariable String vid){
        try {
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(vid);
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            return R.ok().data("playAuth",playAuth);
        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001,"获取凭证失败");
        }
    }
}
