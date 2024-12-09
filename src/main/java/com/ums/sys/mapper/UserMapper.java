package com.ums.sys.mapper;

import com.ums.sys.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chenhao
 * @since 2023-06-16
 */
public interface UserMapper extends BaseMapper<User> {


    public List<String> getRoleNameByUserId(Integer userId);
}
