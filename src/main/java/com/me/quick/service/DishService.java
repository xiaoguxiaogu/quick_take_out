package com.me.quick.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.me.quick.dto.DishDto;
import com.me.quick.entity.Dish;

import java.util.List;

/**
 * @author chen
 * @Date 2022-04-17-19:57
 */
public interface DishService extends IService<Dish> {
    /**
     * 新增菜品，并且更改dish_flavor
     * @param dishDto
     */
    void saveWithFlavor(DishDto dishDto);

    /**
     * 分页查询dish,但是用dishDto展示
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    Page<DishDto> PageDishDtos(int page, int pageSize, String name);

    /**
     * 通过id查询菜品，并回显dto到页面上
     * @param id
     * @return
     */
    DishDto getDishDto(Long id);

    /**
     * 更新菜品，并且更改dish_flavor
     * @param dishDto
     */
    void updateWithFlavor(DishDto dishDto);

    /**
     * 修改一个或者多个菜品状态
     * @param status
     * @param idsLong
     */
    void changeStatuses(Integer status, Long[] idsLong);

    /**
     * 删除一个或多个菜品，与口味一起删除
     * @param idsLong
     */
    void removeWithFlavor(Long[] idsLong);

    /**
     * 列出所有当前分类下的菜品，同时添加完整的口味信息
     * @param dish
     * @return
     */
    List<DishDto> listDishDtos(Dish dish);
}
