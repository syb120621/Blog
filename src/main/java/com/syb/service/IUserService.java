package com.syb.service;

import com.syb.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author syb
 * @since 2020-06-23
 */
public interface IUserService extends IService<User> {
    public List<User> findAll();
}
