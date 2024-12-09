package com.ums.sys.controller;

import com.ums.common.vo.Result;
import com.ums.sys.entity.User;
import com.ums.sys.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author anthony
 * @since 2023-06-16
 */
@RestController
@RequestMapping("/user")
// @CrossOrigin  //处理跨域，因为前端和后端的IP一致但端口不一致，所以浏览器认为跨域，不给访问，可用Ngx来解决
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("/all")
    public Result<List<User>> getAllUser() {
        List<User> list = userService.list();
        return Result.success(list,"查询成功");
    }

    @PostMapping("/login")
    public Result<Map<String,Object>> login(@RequestBody User user){
        // 因为 user传过来为json字符串，所以用@RequestBody 进行实体转换

        // 业务代码在userService里完成
        Map<String,Object> data = userService.login(user);

        if(data != null){
            return Result.success(data,"登录成功");
        }
        return Result.fail(2002,"用户名或密码错误");
    }

    @GetMapping("/info")
    public Result<Map<String,Object>> getUserInfo(@RequestParam("token") String token){
        // @RequestParam("token") 是从url中获取值
        // 根据token获取用户信息，信息存进了redis中
        Map<String,Object> data = userService.getUserInfo(token);
        if(data != null){
            return Result.success(data);
        }
        return Result.fail(2003,"登录信息无效，请重新登录");
    }

    @PostMapping("/logout")
    public Result<?> logout(@RequestHeader("X-Token") String token){
        userService.logout(token);
        return Result.success();
    }
}

