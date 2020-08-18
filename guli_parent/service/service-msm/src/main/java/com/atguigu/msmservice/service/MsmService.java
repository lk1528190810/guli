package com.atguigu.msmservice.service;

import java.util.Map;

public interface MsmService {
    //发送信息的方法
    boolean sendMsm(Map<String, Object> params, String phoneNumbers);
}
