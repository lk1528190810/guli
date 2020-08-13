package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author lk
 * @since 2020-08-10
 */
@Api(description = "讲师管理的controller")
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin
public class EduTeacherController {

    @Autowired
    private EduTeacherService teacherService;


    //7.更新讲师信息的接口
    @ApiOperation(value = "更新讲师信息的方法")
    @PostMapping("/updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean flag = teacherService.updateById(eduTeacher);
        if(flag){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //6.查询讲师的接口
    @ApiOperation(value = "查询讲师的方法")
    @GetMapping("/getTeacher/{id}")
    public R getTeacher(@PathVariable("id") long id){
        EduTeacher eduTeacher = teacherService.getById(id);
        if(!StringUtils.isEmpty(eduTeacher)){
            return R.ok().data("teacher",eduTeacher);
        }else{
            return R.error().data("info","未找到id为" + id + "的信息");
        }
    }

    //5.添加讲师的接口
    @ApiOperation(value = "添加讲师的方法")
    @PostMapping("/addTeacher")
    public R addTeacher(@RequestBody(required = true) EduTeacher eduTeacher){
        boolean save = teacherService.save(eduTeacher);
        if(save){
            return R.ok();
        }else {
            return R.error();
        }
    }


    //4.多条件组合查询  (将条件封装成一个类)
    //注意：  **************使用 @RequestBody 需要post的提交方式
    @ApiOperation(value = "多条件组合查询")
    @PostMapping("/pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable("current") long current,
                                  @PathVariable("limit") long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery){
        //创建一个page对象
        Page<EduTeacher> page = new Page<>(current,limit);

        //条件判断 ， 动态sql需要进行拼接
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();

        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();

        //条件判断 模糊查询
        if(!StringUtils.isEmpty(name)){
            //第一个参数表示数据库中的列名 ， 第二个参数表示从前端页面传递过来的值
            wrapper.like("name",name);
        }

        //这个是比较
        if(!StringUtils.isEmpty(level)){
            wrapper.eq("level",level);
        }

        //大于
        if(!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create",begin);
        }

        //小于
        if(!StringUtils.isEmpty(end)){
            wrapper.le("gmt_modified",end);
        }

        //根据创建时间降序排序
        wrapper.orderByDesc("gmt_modified");

        teacherService.page(page,wrapper);
        List<EduTeacher> records = page.getRecords(); // 每页的数据信息
        long total = page.getTotal(); //总记录数
        return R.ok().data("total",total).data("rows",records);
    }

    //3.分页查询的方法
    @GetMapping("pageTeacher/{current}/{limit}")
    @ApiOperation(value = "分页查询的方法")
    public R pageListTeacher(@PathVariable("current") long current,@PathVariable("limit") long limit){
        Page<EduTeacher> page = new Page<>(current,limit);
        //调用pageMaps方法会将数据库的信息全部封装到page中
        teacherService.pageMaps(page,null);

        try {
            int a = 10 / 0;
        } catch (Exception e) {
            throw new GuliException(20001,"执行了自定义的方法....");
        }


        List<EduTeacher> records = page.getRecords(); // 每页的数据信息
        long total = page.getSize(); //总记录数

//        Map<String, Object> map = new HashMap<>();
//        map.put("total",total);
//        map.put("rows",records);
//        return  R.ok().data(map);
        return R.ok().data("total",total).data("rows",records);
//        return R.ok().data("pageInfo",page);
    }


    //2.逻辑删除讲师
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "根据ID删除讲师")
//    http://localhost:8001/eduservice/teacher/delete/1
    public R removeTeacher(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable("id") String id){
        boolean flag = teacherService.removeById(id);
        if(flag == true){
            return R.ok();
        }else {
            return R.error();
        }
    }


    //访问地址 http://localhost:8001/eduservice/teacher/findAll
    //1.查询所有的教师的信息
    @GetMapping("/findAll")
    @ApiOperation(value = "所有讲师列表")
    public R findAll(){
        List<EduTeacher> teachers = teacherService.list(null);
        System.out.println("teachers = " + teachers);
        return R.ok().data("items",teachers);
    }

}

