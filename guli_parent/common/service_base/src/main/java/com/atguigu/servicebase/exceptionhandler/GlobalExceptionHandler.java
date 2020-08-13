package com.atguigu.servicebase.exceptionhandler;

import com.atguigu.commonutils.ExceptionUtil;
import com.atguigu.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

//当controller发生异常时会被controllerAdvice捕获 进入下面的方法
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //指定出现什么异常执行该方法Exception及其子类异常
    @ExceptionHandler(Exception.class) //捕捉全部的异常
    @ResponseBody //返回的结果以json字符串返回
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("执行了全局异常的方法...");
    }

    //特定异常
    //指定出现什么异常执行该方法Exception及其子类异常
    @ExceptionHandler(ArithmeticException.class) //捕捉全部的异常
    @ResponseBody //返回的结果以json字符串返回
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("执行了ArithmeticException异常的方法...");
    }


    //自定义异常
    //指定出现什么异常执行该方法Exception及其子类异常
    @ExceptionHandler(GuliException.class) //捕捉全部的异常
    @ResponseBody //返回的结果以json字符串返回
    public R error(GuliException e){
        log.error(ExceptionUtil.getMessage(e));
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }

}
