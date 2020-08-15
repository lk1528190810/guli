package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author lk
 * @since 2020-08-14
 */
@Api(description="课程分类管理")
@RestController
@RequestMapping("/eduservice/subject")
@CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService subjectService;

    //添加课程分类
    @ApiOperation(value = "Excel批量导入")
    @PostMapping("/addSubject")
    public R addSubject(MultipartFile file){
        //1 获取上传的excel文件 MultipartFile
        //返回错误提示信息
        subjectService.addSubjectData(file,subjectService);
        return R.ok();
    }

    //查询数据列表
    @ApiOperation(value = "嵌套数据列表")
    @GetMapping("/getAllSubject")
    public  R getAllSubject(){
        List<OneSubject> list = subjectService.getOneTwoSubject();
        return R.ok().data("list",list);
    }
}

