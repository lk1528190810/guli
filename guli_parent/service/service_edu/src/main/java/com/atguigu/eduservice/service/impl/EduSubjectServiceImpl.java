package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.ExcelSubjectData;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.listener.ExcelSubjectListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author lk
 * @since 2020-08-14
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    //读取excel中的数据
    @Override
    public void addSubjectData(MultipartFile file , EduSubjectService subjectService) {
        try {
            InputStream in = file.getInputStream();
            EasyExcel.read(in, ExcelSubjectData.class,new ExcelSubjectListener(subjectService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<OneSubject> getOneTwoSubject() {

        List<OneSubject> finalEduSubject = new ArrayList<>();

        //1.查询出所有的一级标题
        //根据parent_id = 0 可以查询出所有的一级标题
        QueryWrapper<EduSubject> OneWrapper = new QueryWrapper<>();
        //条件
        OneWrapper.eq("parent_id","0");
        //查询出所有的一级标题
        List<EduSubject> oneEduSubject = baseMapper.selectList(OneWrapper);


        //2.查询出所有的二级标题
        //根据parent_id != 0 可以查询出所有的二级标题
        QueryWrapper<EduSubject> twoWrapper = new QueryWrapper<>();
        //条件
        twoWrapper.ne("parent_id","0");
        //查询出所有的一级标题
        List<EduSubject> twoEduSubject = baseMapper.selectList(twoWrapper);

        //3.将所有的一级标题放入到oneSubject中
        //遍历所有的一级标题
        for (int i = 0; i < oneEduSubject.size(); i++) {
            //每一个的一级标题
            EduSubject eduSubject = oneEduSubject.get(i);
            //需要将EduSubject中的id和title设置到OneSubject对象中
            OneSubject oneSubject = new OneSubject();
//            oneSubject.setId(eduSubject.getId());
//            oneSubject.setTitle(eduSubject.getTitle());
            //可以使用一个工具类实现上面两部的操作
            //将eduSubject中的值get出来set到oneSubject中去  只会set两个类中属性相同的值
            BeanUtils.copyProperties(eduSubject,oneSubject);
            //需要将每个oneSubject存储起来
            finalEduSubject.add(oneSubject);


            //用于存储二级标题
            List<TwoSubject> twoFinalSubject = new ArrayList<>();

            //4.将所有的二级标题放入到twoSubject中
            for (int j = 0; j < twoEduSubject.size(); j++) {
                EduSubject eduSubject2 = twoEduSubject.get(j);
                //获取到了所有的二级标题
                if(eduSubject.getId().equals(eduSubject2.getParentId())){
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(eduSubject2,twoSubject);
                    twoFinalSubject.add(twoSubject);
                }
            }

            oneSubject.setChildren(twoFinalSubject);
        }
        return finalEduSubject;
    }
}
