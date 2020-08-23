package com.atguigu.eduservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "service-order")
public interface OrderClient {

    //3.根据课程id和用户id查询状态码
    @GetMapping("/eduorder/order/queryStatusByUserIdAndCourseId/{courseId}/{userId}")
    public boolean queryStatusByUserIdAndCourseId(@PathVariable("courseId") String courseId,
                                                  @PathVariable("userId") String userId);
}
