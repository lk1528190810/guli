package com.atguigu.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    //这是文件上传到Oss的方法
    String uploadFileAvatar(MultipartFile file);
}
