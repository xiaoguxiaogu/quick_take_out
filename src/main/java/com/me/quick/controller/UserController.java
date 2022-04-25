package com.me.quick.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.me.quick.common.R;
import com.me.quick.entity.User;
import com.me.quick.service.UserService;
import com.me.quick.utils.SMSUtils;
import com.me.quick.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author chen
 * @Date 2022-04-20-11:57
 */
@RestController
@Slf4j
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 发送手机短信验证码
     * @param user
     * @return
     */
    @PostMapping("sendMsg")
    public R<String> sendMsg(@RequestBody User user,HttpSession session){
        //获取手机号
        String phone = user.getPhone();
        //生成四位随机验证码
        if(StringUtils.isNotEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);
            //调取阿里云提供的短信服务api,没买，所以注释掉
            //SMSUtils.sendMessage("快客外卖","",phone,code);


            //将验证码保存到session
            session.setAttribute(phone,code);
            return R.success(code);
        }




        return R.error("获取验证码失败");
    }

    /**
     * 登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //在Session中获取保存的验证码
        Object codeInsession = session.getAttribute(phone);
        if(codeInsession!=null&&codeInsession.equals(code)){
            //登录成功
            //判断当前用户是否为新用户，若是新用户完成自动注册
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(userLambdaQueryWrapper);

            if(user==null){
                //若是新用户
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登录失败");
    }
}
