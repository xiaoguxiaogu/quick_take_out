package com.me.quick.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.me.quick.dto.DishDto;
import com.me.quick.dto.SetmealDto;
import com.me.quick.entity.Dish;
import com.me.quick.entity.Setmeal;

import java.util.List;

/**
 * @author chen
 * @Date 2022-04-17-19:57
 */
public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐保存，同时新增setmeal_dish表
     * @param setmealDto
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 分页查询套餐
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    Page<SetmealDto> pageSetmealDtos(int page, int pageSize, String name);

    /**
     * 查询单条套餐，返回dto
     * @param id
     * @return
     */
    SetmealDto getSetmealDto(Long id);

    /**
     * 修改单条套餐，同时修改setmeal_dish表
     * @param setmealDto
     */
    void updateWithDish(SetmealDto setmealDto);

    /**
     * 批量修改套餐状态
     * @param status
     * @param ids
     */
    void changeStatus(Integer status, Long[] ids);

    /**
     * 批量删除套餐，同时要删除setmeal_dish表中关联数据
     * @param ids
     */
    void removeWithDish(Long[] ids);

    /**
     * 按照categoryId查询套餐，status
     * 同时包含套餐口味数据
     * @param setmeal
     * @return
     */
    List<SetmealDto> listSetmealDtos(Setmeal setmeal);
}
