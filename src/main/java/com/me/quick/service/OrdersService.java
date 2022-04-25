package com.me.quick.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.quick.entity.Orders;

/**
 * @author chen
 * @Date 2022-04-25-21:15
 */
public interface OrdersService extends IService<Orders> {
    /**
     * 用户下单
     * @param orders
     */
    void submit(Orders orders);
}
