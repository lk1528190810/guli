package com.atguigu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    //这是文件上传到Oss的方法
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ConstantPropertiesUtils.END_POINT;
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String accessBucketName = ConstantPropertiesUtils.BUCKET_NAME;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 上传文件流。
            InputStream inputStream = file.getInputStream();
            //获取文件名字
            String fileName = file.getOriginalFilename();
            
            //1. 文件名称不重复
            String uuid = UUID.randomUUID().toString();
            //aawudihaoidioa01.jpg
            fileName = uuid + fileName;

            //2. 实现分层管理根据时间
            String dataPath = new DateTime().toString("yyyy/MM/dd");
            //2020/08/13/aawudihaoidioa01.jpg
            fileName = dataPath + "/" + fileName;

            //第一个参数 ：bucketName 就是在阿里云创建的bucketName
            //第二个参数：fileName 可以是文件名称也可以是绝对路径
            //第三个参数： inputStream 文件的输入流
            ossClient.putObject(accessBucketName, fileName, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //拼接
            //https://edu-0430.oss-cn-beijing.aliyuncs.com/01.jpg
            //https://bucketName.endpoint/fileName
            String url = "https://" + accessBucketName + "." + endpoint + "/" + fileName;
            return  url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
