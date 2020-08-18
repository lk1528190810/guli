package com.atguigu.msmservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.utils.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin
@RequestMapping("/edumsm/msm")
@Api(description = "发送信息的controller")
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //发送信息的方法
    @ApiOperation(value = "发送信息的方法")
    @GetMapping("/send/{phoneNumbers}")
    public R sendMsm(@PathVariable("phoneNumbers") String phoneNumbers){
        //1. 在发信息之前，看看信息是否过期
        String code = redisTemplate.opsForValue().get(phoneNumbers);
        //如果不等于null说明还没过期
        if(!StringUtils.isEmpty(code)) {
            return R.ok();
        }

        //首先你需要将你验证码规则是自己设置的
        //获取随机的四位验证码
        code = RandomUtil.getFourBitRandom();
        //方便后面使用
        Map<String,Object> params = new HashMap<>();
        params.put("code",code);
        //发送信息需要指明发送规则和电话号码
        boolean isSend = msmService.sendMsm(params,phoneNumbers);
        if(isSend) {
            //发送成功
            //2. 需要将验证码放入到redis中 5分钟之后过期
            redisTemplate.opsForValue().set(phoneNumbers,code,5, TimeUnit.MINUTES);
            return R.ok();
        }else {
            //发送失败
            return R.error().message("信息发送失败");
        }
    }

}
