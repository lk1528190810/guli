package com.atguigu.eduorder.client;

import com.atguigu.commonutils.frontvo.UcenterMemberOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(value = "service-ucenter")
public interface UcenterClient {

    //根据用户id获取到由用户的信息
    @PostMapping("/edumember/member/getUserInfoOrder/{userId}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable("userId") String userId);
}
