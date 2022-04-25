package com.me.quick.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.quick.entity.Dish;
import com.me.quick.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chen
 * @Date 2022-04-17-19:55
 */
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {
}
