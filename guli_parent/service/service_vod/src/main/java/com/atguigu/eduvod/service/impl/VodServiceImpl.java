package com.atguigu.eduvod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.atguigu.commonutils.R;
import com.atguigu.eduvod.service.VodService;
import com.atguigu.eduvod.utils.AliyunVodSDKUtils;
import com.atguigu.eduvod.utils.ConstantVodUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class VodServiceImpl implements VodService {

    //这是文件上传视频的方法
    @Override
    public String uploadAliyunVideo(MultipartFile file) {

        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));

            UploadStreamRequest request = new UploadStreamRequest(
                    ConstantVodUtils.ACCESS_KEY_ID,
                    ConstantVodUtils.ACCESS_KEY_SECRET,
                    title, originalFilename, inputStream);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。
            // 其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            String videoId = response.getVideoId();
            return videoId;
        } catch (IOException e) {
            throw new GuliException(20001, "guli vod 服务上传失败");
        }
    }

    //删除多个视频
    @Override
    public void deleteAliyunVideoBatch(List<String> videoList) {

        try {
            //1.初始化客户端对象
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //2. 创建request和response对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //3.设置请求的id
            String videoIds = StringUtils.join(videoList.toArray(), ",");

            request.setVideoIds(videoIds);
            //4.调用client方法
            client.getAcsResponse(request);

        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"删除多个视频失败");
        }
    }
}
