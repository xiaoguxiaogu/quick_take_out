package com.me.quick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.quick.entity.ShoppingCart;
import com.me.quick.mapper.ShoppingCartMapper;
import com.me.quick.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author chen
 * @Date 2022-04-22-21:21
 */

@Service
@Slf4j
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
