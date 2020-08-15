package com.atguigu.eduservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@ComponentScan(basePackages = {"com.atguigu"})
@CrossOrigin
public class EduTeacherApplication {

      public static void main(String[] args) {
        SpringApplication.run(EduTeacherApplication.class,args);
      }

}
