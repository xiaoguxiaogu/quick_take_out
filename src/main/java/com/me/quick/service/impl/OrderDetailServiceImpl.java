package com.me.quick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.quick.entity.OrderDetail;
import com.me.quick.entity.Orders;
import com.me.quick.mapper.OrderDetailMapper;
import com.me.quick.mapper.OrdersMapper;
import com.me.quick.service.OrderDetailService;
import com.me.quick.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author chen
 * @Date 2022-04-25-21:16
 */
@Slf4j
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
