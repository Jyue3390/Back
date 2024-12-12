package com.ums.sys.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ums.sys.entity.User;
import com.ums.sys.mapper.UserMapper;
import com.ums.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> login(User user) {
        // 查询数据库，获取用户名和密码匹配的用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        wrapper.eq(User::getPassword, user.getPassword());
        User loginUser = this.baseMapper.selectOne(wrapper);

        // 结果不为空，生成token，将用户信息存入redis
        if (loginUser != null) {
            // 用UUID，终极方案是jwt
            String key = "user:" + UUID.randomUUID();

            // 存入redis
            loginUser.setPassword(null);  // 设置密码为空，密码没必要放入
            redisTemplate.opsForValue().set(key, loginUser, 30, TimeUnit.MINUTES);  // timeout为登录时间

            // 返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("token", key);  // 返回生成的 token
            data.put("role", loginUser.getRole());  // 角色信息传到前端
            return data;
        }

        // 如果用户不存在，返回 null
        return null;
    }


    @Override
    public boolean register(User user) {
        // 检查用户名是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        User existingUser = userMapper.selectOne(queryWrapper);

        if (existingUser != null) {
            return false; // 用户名已存在
        }

        // 默认角色为 "editor"（可以根据需求调整）
        user.setRole("editor");

        // 插入新用户记录
        return userMapper.insert(user) > 0;
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        // 从redis中获取用户信息
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            Map<String, Object> data = new HashMap<>();
            data.put("name", loginUser.getUsername());
            data.put("avatar", loginUser.getAvatar());
            data.put("role", loginUser.getRole());  // 直接返回角色字符串

            return data;
        }

        return null;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete(token);  // 从redis中删除token
    }
}
