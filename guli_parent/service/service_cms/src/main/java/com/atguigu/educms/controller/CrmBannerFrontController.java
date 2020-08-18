package com.atguigu.educms.controller;


import com.atguigu.commonutils.R;
import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author lk
 * @since 2020-08-17
 */
@Api(description = "网站首页Banner列表")
@RestController
@RequestMapping("/educms/bannerfront")
@CrossOrigin
public class CrmBannerFrontController {

    @Autowired
    private CrmBannerService bannerService;

    //查询所有的banner
    @ApiOperation(value = "查询所有的banner")
    @GetMapping("/getAllBanner")
    @Cacheable(value = "banner",key = "'selectIndexList'")
    public R getAllBanner(){
        List<CrmBanner> list = bannerService.getAllBanners();
        return R.ok().data("banners",list);
    }

}

