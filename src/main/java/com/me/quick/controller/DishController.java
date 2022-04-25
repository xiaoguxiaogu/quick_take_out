package com.me.quick.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.me.quick.common.R;
import com.me.quick.dto.DishDto;
import com.me.quick.entity.Category;
import com.me.quick.entity.Dish;
import com.me.quick.service.CategoryService;
import com.me.quick.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author chen
 * @Date 2022-04-16-17:28
 */
@RestController
@RequestMapping("dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @GetMapping("page")
    public R<Page> listDishes(@RequestParam("page") int page, int pageSize,String name){
        Page<DishDto> pageDto=dishService.PageDishDtos(page,pageSize,name);
        return R.success(pageDto);
    }

    @GetMapping("{id}")
    public R<DishDto> getDishDto(@PathVariable("id") Long id){
        DishDto dishDto=dishService.getDishDto(id);
        return R.success(dishDto);
    }

    /**
     * 根据条件查询对应菜品.
     * 在开发用户界面时，需要详细的口味数据，故作适当修改
     * @param dish
     * @return
     */
    @GetMapping("list")
    public R<List<DishDto>> list(Dish dish){
       /* LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(dish.getCategoryId()!=null, Dish::getCategoryId,dish.getCategoryId());
        dishLambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
        dishLambdaQueryWrapper.like(StringUtils.isNotEmpty(dish.getName()),Dish::getName,dish.getName());
        List<Dish> dishList = dishService.list(dishLambdaQueryWrapper);
        return R.success(dishList);*/

        List<DishDto> dishDtoList=dishService.listDishDtos(dish);
        return R.success(dishDtoList);
    }

    @PostMapping
    public R<String> addDish(@RequestBody DishDto dishDto){
        //不太ok，要更改dish_flavor
        //log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);

        return R.success("菜品添加成功");
    }

    @PutMapping
    public R<String> updateDish(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("菜品修改成功");
    }

    /**
     * 批量修改状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("status/{status}")
    public R<String> changeStatuses(@PathVariable("status")Integer status,@RequestParam("ids")String ids){


        String[] idsString = ids.split(",");
        Long[] idsLong = new Long[idsString.length];
        for(int i=0;i<idsLong.length;i++){
            idsLong[i]=Long.parseLong(idsString[i]);
        }
        dishService.changeStatuses(status,idsLong);


        return R.success("状态修改成功");
    }

    @DeleteMapping
    public R<String> deleteDish(@RequestParam("ids") String ids){

        String[] idsString = ids.split(",");
        Long[] idsLong = new Long[idsString.length];
        for(int i=0;i<idsLong.length;i++){
            idsLong[i]=Long.parseLong(idsString[i]);
        }
        dishService.removeWithFlavor(idsLong);
        return R.success("删除成功");
    }


}
