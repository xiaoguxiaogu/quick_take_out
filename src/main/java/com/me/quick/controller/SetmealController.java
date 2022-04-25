package com.me.quick.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.me.quick.common.R;
import com.me.quick.dto.DishDto;
import com.me.quick.dto.SetmealDto;
import com.me.quick.entity.Setmeal;
import com.me.quick.entity.SetmealDish;
import com.me.quick.service.SetmealDishService;
import com.me.quick.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author chen
 * @Date 2022-04-16-17:28
 */
@RestController
@RequestMapping("setmeal")
@Slf4j
public class SetmealController {
   @Autowired
    private SetmealService setmealService;



    /**
     * 添加套餐
     * @param setmealDto
     * @return
     */
   @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){

       setmealService.saveWithDish(setmealDto);
       return R.success("添加套餐成功");
   }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
   @GetMapping("page")
    public R<Page> page(int page,int pageSize,String name){
       Page<SetmealDto> setmealDtoList=setmealService.pageSetmealDtos(page,pageSize,name);

       return R.success(setmealDtoList);
   }

    /**
     * 按照指定的status和categoryId查询套餐
     * 同时考虑应纳入口味数据
     * @param setmeal
     * @return
     */
   @GetMapping("list")
   public R<List<SetmealDto>>  List(Setmeal setmeal){
       List<SetmealDto> setmealDtoList=setmealService.listSetmealDtos(setmeal);
       return R.success(setmealDtoList);
   }

    /**
     * 查询单个套餐Dto
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public R<SetmealDto> getSetMealDto(@PathVariable("id") Long id){
        SetmealDto setmealDto=setmealService.getSetmealDto(id);
        return R.success(setmealDto);
    }

    /**
     * 修改套餐
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> updateSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithDish(setmealDto);
        return R.success("菜品修改成功");
    }


    @PostMapping("status/{status}")
    public R<String> changeStatus(@PathVariable("status")Integer status,@RequestParam("ids") Long[] ids){
        setmealService.changeStatus(status,ids);
        return R.success("状态修改成功");
    }

    @DeleteMapping
    public R<String> deleteSetmeal(Long[] ids){
        setmealService.removeWithDish(ids);
        return R.success("删除成功");
    }


}
