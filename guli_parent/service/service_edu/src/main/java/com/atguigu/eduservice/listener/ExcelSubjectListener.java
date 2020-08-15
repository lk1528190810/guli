package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.ExcelSubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public class ExcelSubjectListener extends AnalysisEventListener<ExcelSubjectData> {

    //因为SubjectExcelListener是new出来的没有交给spring管理所以最好把EduSubjectService注入进来
    //方便调用后序的方法

    private EduSubjectService subjectService;

    public ExcelSubjectListener() {
    }

    //创建有参数构造，传递subjectService用于操作数据库
    public ExcelSubjectListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }

    //一行一行读数据
    @Override
    public void invoke(ExcelSubjectData data, AnalysisContext context) {
        if(data == null){
            throw new GuliException(20001,"文件不可为空");
        }

        //添加一级标题
        EduSubject oneSubject = existOneSubject(subjectService, data.getOneSubjectName());
        if(oneSubject == null){
            oneSubject = new EduSubject();
            oneSubject.setTitle(data.getOneSubjectName());
            oneSubject.setParentId("0");
            subjectService.save(oneSubject);
        }

        //获取pid的值
        String pid = oneSubject.getId();

        //添加二级标题
        EduSubject twoSubject = existTwoSubject(subjectService, data.getTwoSubjectName(), pid);
        if(twoSubject == null){
            twoSubject = new EduSubject();
            twoSubject.setTitle(data.getTwoSubjectName());
            twoSubject.setParentId(pid);
            subjectService.save(twoSubject);
        }
    }

    //判断一级标题是否重复
    private EduSubject existOneSubject(EduSubjectService subjectService,String name){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        EduSubject eduSubject = subjectService.getOne(wrapper);
        return eduSubject;
    }


    //判断二级标题是否重复
    private EduSubject existTwoSubject(EduSubjectService subjectService,String name,String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        EduSubject eduSubject = subjectService.getOne(wrapper);
        return eduSubject;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
