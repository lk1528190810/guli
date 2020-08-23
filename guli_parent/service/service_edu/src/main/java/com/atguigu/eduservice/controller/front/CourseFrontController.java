package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.frontvo.CourseWebVoOrder;
import com.atguigu.commonutils.frontvo.UcenterMemberOrder;
import com.atguigu.eduservice.client.OrderClient;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(description = "这是前台讲师分页")
@RestController
@RequestMapping("/eduservice/coursefront")
@CrossOrigin
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;
    
    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrderClient orderClient;

    //这是获取前台课程列表信息带分页
    @ApiOperation(value = "这是获取前台课程列表信息带分页")
    @PostMapping("/getCourseFrontList/{currentPage}/{limit}")
    public R getCourseFrontList(@PathVariable long currentPage,
                                @PathVariable long limit,
                                @RequestBody(required=false) CourseFrontVo courseFrontVo){
        Page<EduCourse> pageCourse = new Page<>(currentPage,limit);
        Map<String, Object> map = courseService.getCourseFrontList(pageCourse,courseFrontVo);

        return R.ok().data(map);
    }
    
    //获取courseFrontInfo课程详情页的基本信息
    @ApiOperation(value = "获取courseFrontInfo课程详情页的基本信息")
    @GetMapping("/getCourseBaseInfo/{courseId}")
    public R getCourseBaseInfo(@PathVariable("courseId") String courseId, HttpServletRequest request){
        //1.根据courseId查询出课程的基本信息
        CourseWebVo courseWebVo = courseService.getCourseBaseInfo(courseId);
        
        //2.根据课程id查询出章节和小节的信息
        List<ChapterVo> allChapterVideo = chapterService.getAllChapterVideo(courseId);
        System.out.println("allChapterVideo = " + allChapterVideo);

        //3.获取课程的状态 是否购买
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        boolean isBuy = false;
        if(!StringUtils.isEmpty(userId)){
            isBuy = orderClient.queryStatusByUserIdAndCourseId(courseId,userId);
        }

        return R.ok().data("courseWebVo",courseWebVo).data("allChapterVideo",allChapterVideo).data("isBuy",isBuy);

    }

    //根据课程id获取到由课程的信息
    //CourseWebVoOrder 方便获取课程信息
    @ApiOperation(value = "根据课程id获取到课程的信息")
    @PostMapping("/getCourseInfoOrder/{courseId}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable("courseId") String courseId){
        CourseWebVo courseWebVo = courseService.getCourseBaseInfo(courseId);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(courseWebVo,courseWebVoOrder);
        return courseWebVoOrder;
    }

}
