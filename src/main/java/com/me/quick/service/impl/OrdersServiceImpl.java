package com.me.quick.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.quick.common.BaseContext;
import com.me.quick.common.CustomException;
import com.me.quick.entity.*;
import com.me.quick.mapper.OrdersMapper;
import com.me.quick.service.*;
import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author chen
 * @Date 2022-04-25-21:16
 */
@Slf4j
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private UserService userService;



    @Override
    @Transactional
    public void submit(Orders orders) {
        //获取当前用户id
        Long userId = BaseContext.getCurrentId();
        User user = userService.getById(userId);

        //查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);

        if(addressBook==null){
            throw new CustomException("地址信息有误，不能下单");
        }

        //查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(shoppingCartLambdaQueryWrapper);


        if(shoppingCartList==null||shoppingCartList.size()==0){
            throw  new CustomException("购物车为空，不能下单");
        }
        long orderId = IdWorker.getId();
        //计算总钱数
        AtomicInteger amount = new AtomicInteger(0);

        List<OrderDetail> orderDetailList = shoppingCartList.stream().map(
                (item) -> {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrderId(orderId);
                    orderDetail.setName(item.getName());
                    orderDetail.setDishFlavor(item.getDishFlavor());
                    orderDetail.setDishId(item.getDishId());
                    orderDetail.setSetmealId(item.getSetmealId());
                    orderDetail.setName(item.getName());
                    orderDetail.setImage(item.getImage());
                    orderDetail.setAmount(item.getAmount());
                    amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
                    return orderDetail;
                }
        ).collect(Collectors.toList());
        //向订单表插入数据

        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);//2表示待派送
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(
                addressBook.getProvinceName()==null?"":addressBook.getProvinceName()
                +addressBook.getCityName()==null?"":addressBook.getCityName()
                +addressBook.getDistrictName()==null?"":addressBook.getDistrictName()
                +addressBook.getDetail()==null?"":addressBook.getDetail()
        );
        this.save(orders);
        //向订单明细表插入数据
        orderDetailService.saveBatch(orderDetailList);
        //删除购物车
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
    }
}
