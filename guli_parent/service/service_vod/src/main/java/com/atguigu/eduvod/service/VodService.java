package com.atguigu.eduvod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
    //这是文件上传视频的方法
    String uploadAliyunVideo(MultipartFile file);

    //删除多个视频
    void deleteAliyunVideoBatch(List<String> videoList);
}
