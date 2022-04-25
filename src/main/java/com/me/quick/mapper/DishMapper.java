package com.me.quick.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.quick.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chen
 * @Date 2022-04-17-19:55
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
