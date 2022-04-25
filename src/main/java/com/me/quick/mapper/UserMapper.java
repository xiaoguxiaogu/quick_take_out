package com.me.quick.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.quick.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chen
 * @Date 2022-04-21-11:09
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
