package com.me.quick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.quick.entity.Dish;
import com.me.quick.entity.DishFlavor;
import com.me.quick.mapper.DishFlavorMapper;
import com.me.quick.mapper.DishMapper;
import com.me.quick.service.DishFlavorService;
import com.me.quick.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author chen
 * @Date 2022-04-17-19:57
 */
@Service
@Slf4j
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {

}
