package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author lk
 * @since 2020-08-14
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService descriptionService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private EduVideoService videoService;

    @Override
    public String addCourseInfo(CourseInfoVo courseInfoVo) {
        //将信息添加到edu_course表中
        EduCourse eduCourse = new EduCourse();
        //将页面传递过来的数据赋值到eduCourse对象中
        BeanUtils.copyProperties(courseInfoVo,eduCourse);

        int insert = baseMapper.insert(eduCourse);
        if(insert <= 0){
            //说明插入失败
            throw new GuliException(20001,"插入课程信息失败");
        }

        String cid = eduCourse.getId();

        //将课程描述信息插入到课程描述表中
        EduCourseDescription description = new EduCourseDescription();
        description.setDescription(courseInfoVo.getDescription());
        description.setId(cid);

        //因为baseMapper是EduCourse的mapper保存EduCourseDescription
        // 需要EduCourseDescription的mapper所以可以使用自动注入将mapper注入进来
        descriptionService.save(description);

        return cid;

    }

    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        //1. 先查出eduCourse
        EduCourse eduCourse = baseMapper.selectById(courseId);
        //将eduCourse设置到CourseInfo里面
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);

        //2. 在查出eduCourseDescription
        EduCourseDescription eduCourseDescription = descriptionService.getById(courseId);
        courseInfoVo.setDescription(eduCourseDescription.getDescription());
        return courseInfoVo;
    }

    @Override
    public CoursePublishVo getCoursePublishVoById(String courseId) {
        return baseMapper.getCoursePublishVoById(courseId);
    }

    @Override
    public void removeCourse(String courseId) {
        //1. 删除全部的小节根据课程id
        videoService.removeVideoByCourseId(courseId);

        //2. 删除全部的章节根据课程id
        chapterService.removeChapterByCourseId(courseId);

        //3. 删除课程描述根据课程id
        descriptionService.removeById(courseId);

        //4. 删除课程
        int result = baseMapper.deleteById(courseId);
        if(result == 0 ){
            throw new GuliException(20001,"删除课程失败");
        }
    }


    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //将数据设置到eduCourse中
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);

        //更新到数据库中
        int flag = baseMapper.updateById(eduCourse);
        if(flag <= 0 ){
            throw new GuliException(20001,"更新课程信息失败");
        }

        //描述表的信息
        EduCourseDescription description = new EduCourseDescription();
        description.setId(eduCourse.getId());
        description.setDescription(courseInfoVo.getDescription());
        //更新数据库
        descriptionService.updateById(description);
    }

    //这是获取前台课程列表信息带分页
    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseFrontVo courseFrontVo) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        String subjectParentId = courseFrontVo.getSubjectParentId();//一级分类
        String subjectId = courseFrontVo.getSubjectId();//二级分类
        String buyCountSort = courseFrontVo.getBuyCountSort();//购买量
        String gmtCreateSort = courseFrontVo.getGmtCreateSort();//根据时间
        String priceSort = courseFrontVo.getPriceSort();//价格

        //根据查询条件拼接
        if(!StringUtils.isEmpty(subjectParentId)) {//一级分类
            wrapper.eq("subject_parent_id",subjectParentId);
        }

        if(!StringUtils.isEmpty(subjectId)) {//二级分类
            wrapper.eq("subject_id",subjectId);
        }

        if(!StringUtils.isEmpty(buyCountSort)) {//根据购买量降序
            wrapper.orderByDesc("buy_count");
        }

        if(!StringUtils.isEmpty(gmtCreateSort)) {//根据时间
            wrapper.orderByDesc("gmt_create");
        }

        if(!StringUtils.isEmpty(priceSort)) {//根据价格
            wrapper.orderByDesc("price");
        }

        baseMapper.selectPage(pageCourse,wrapper);

        //将pageCourse中的值存到map中
        List<EduCourse> records = pageCourse.getRecords();
        long current = pageCourse.getCurrent();
        long pages = pageCourse.getPages();
        long size = pageCourse.getSize();
        long total = pageCourse.getTotal();
        boolean hasNext = pageCourse.hasNext();
        boolean hasPrevious = pageCourse.hasPrevious();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    //根据courseId查询出课程的基本信息
    @Override
    public CourseWebVo getCourseBaseInfo(String courseId) {
        CourseWebVo courseWebVo = baseMapper.getCourseBaseInfo(courseId);
        return courseWebVo;
    }
}
