package com.atguigu.edumember.controller;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.edumember.entity.UcenterMember;
import com.atguigu.edumember.service.UcenterMemberService;
import com.atguigu.edumember.utils.ConstantPropertiesUtil;
import com.atguigu.edumember.utils.HttpClientUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@CrossOrigin
@Controller//注意这里没有配置 @RestController
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    //微信扫码时判断数据库中有没有扫码人的信息
    @Autowired
    private UcenterMemberService memberService;

    //获取扫码人信息，添加数据
    @GetMapping("callback")
    public String callback(String code,String state){
        try {
            //得到授权临时票据code
            System.out.println("code = " + code);
            System.out.println("state = " + state);

            //1.首先你需要获取code的值，就相当于验证码
            //2. 可以通过code访问固定的地址获取access_token和openid
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";

            //拼接参数 id 秘钥 和code
            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    ConstantPropertiesUtil.WX_OPEN_APP_ID,
                    ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                    code
            );
            //通过httpclient技术可以发送http请求  返回的是一个json字符串
            String result = HttpClientUtils.get(accessTokenUrl);
            //获取字符串中的值
            Gson gson = new Gson();
            HashMap map = gson.fromJson(result, HashMap.class);
            String accessToken = (String) map.get("access_token");
            String openId = (String) map.get("openid");

            //4. 如果是微信扫码登录需要给他自动加入数据库不用注册
            //判断该用户是否存在  根据openid查询数据
            UcenterMember member = memberService.queryMemberByOpenId(openId);
            if(member == null) {
                //3. 通过accessToken和openid去访问另一个固定地址可以获取到扫码人的信息
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                //拼接参数
                String userInfoUrl = String.format(baseUserInfoUrl,
                        accessToken,
                        openId
                );
                //可以使用httpclient.get()获取到用户的信息
                String resultUserInfo = HttpClientUtils.get(userInfoUrl);
                HashMap<String, Object> mapUserInfo = gson.fromJson(resultUserInfo,HashMap.class);
                String nickname = (String)mapUserInfo.get("nickname");
                String headimgurl = (String)mapUserInfo.get("headimgurl");

                member = new UcenterMember();
                member.setNickname(nickname);
                member.setOpenid(openId);
                member.setAvatar(headimgurl);
                memberService.save(member);
            }
            //5.为了让信息在前端显示需要将用户的信息，使用token jwt规则封装
            String token = JwtUtils.reverseJwtToken(member.getId(), member.getNickname());
            System.out.println("token = " + token);
            //最终跳转到首页面
            return "redirect:http://localhost:3000?token=" + token;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001,"微信扫码失败");
        }
    }


    //生成二维码
    @GetMapping("login")
    public String genQrConnect(){

        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        //需要使用URLEncode对redirect_uri进行编码
        String redirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl= URLEncoder.encode(redirectUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = String.format(baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl,
                "atguigu");
        return "redirect:"+url;
    }

}
