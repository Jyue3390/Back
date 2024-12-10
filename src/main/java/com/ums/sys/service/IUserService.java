package com.ums.sys.service;

import com.ums.sys.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenhao
 * @since 2023-06-16
 */
public interface IUserService extends IService<User> {

    // Map<String, Object> login(User user);

    Map<String, Object> getUserInfo(String token);

    void logout(String token);

    Map<String, Object> login(User user);

    boolean register(User user);
}
