package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VodFeginClientImpl implements VodFeginClient{
    @Override
    public R deleteAliyunVideo(String videoSourceId) {
        return R.error().message("删除视频出错了");
    }

    @Override
    public R deleteAliyunVideoBatch(List<String> videoList) {
        return R.error().message("删除多个视频出错了");
    }
}
