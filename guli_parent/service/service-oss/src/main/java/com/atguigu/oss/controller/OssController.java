package com.atguigu.oss.controller;

import com.atguigu.commonutils.R;
import com.atguigu.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduoss/fileoss")
@CrossOrigin //解决跨域问题
@Api(description = "这是文件上传到Oss的controller")
public class OssController {

    @Autowired
    private OssService ossService;

    //文件上传的方法
    @ApiOperation(value = "文件上传的方法")
    @PostMapping("/upload")
    public R uploadOssFile(MultipartFile file){
        //调用文件上传的方法 返回的应该是oss的存储地址
        String url = ossService.uploadFileAvatar(file);

        return R.ok().data("url",url);
    }
}
