package com.me.quick.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.quick.entity.OrderDetail;
import com.me.quick.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chen
 * @Date 2022-04-25-21:13
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
