package com.syb.mapper;

import com.syb.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author syb
 * @since 2020-06-23
 */

@Repository
public interface UserMapper extends BaseMapper<User> {
    List<User> findAll();
}
