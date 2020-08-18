package com.atguigu.edumember;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.atguigu")
@MapperScan(value = "com.atguigu.edumember.mapper")
public class MemberApplication {
      public static void main(String[] args) {
        SpringApplication.run(MemberApplication.class,args);
      }

}
