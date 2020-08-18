package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(name = "service-vod",fallback = VodFeginClientImpl.class)
public interface VodFeginClient {

    @DeleteMapping("/eduvod/video/deleteAliyunVideo/{videoSourceId}")
    public R deleteAliyunVideo(@PathVariable("videoSourceId") String videoSourceId);

    //删除多个视频
    @DeleteMapping("/eduvod/video/deleteAliyunVideoBatch")
    public R deleteAliyunVideoBatch(@RequestParam("videoList") List<String> videoList) ;
}
