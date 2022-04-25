package com.me.quick.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.quick.common.CustomException;
import com.me.quick.common.R;
import com.me.quick.dto.DishDto;
import com.me.quick.entity.Category;
import com.me.quick.entity.Dish;
import com.me.quick.entity.DishFlavor;
import com.me.quick.mapper.DishMapper;
import com.me.quick.service.CategoryService;
import com.me.quick.service.DishFlavorService;
import com.me.quick.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author chen
 * @Date 2022-04-17-19:57
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Override
    @Transactional//设置事务
    public void saveWithFlavor(DishDto dishDto) {


        //存dish
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);
        this.save(dish);
        //存dish_flavor
        //使用Stream操作
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dish.getId());
            //log.info("菜品id:--·······················测试{}",dishDto);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);

    }

    @Override
    public Page<DishDto> PageDishDtos(int page, int pageSize, String name) {
        //查询dishes
        Page<Dish> dishPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);

        this.page(dishPage, dishLambdaQueryWrapper);
        //转换成pageDishDtos
        List<Dish> dishList = dishPage.getRecords();
        List<DishDto> dishDtoList = new ArrayList<>();
        copyList(dishList, dishDtoList);
        Page<DishDto> dishDtoPage = new Page<>();
        dishDtoPage.setRecords(dishDtoList);
        dishDtoPage.setTotal(dishPage.getTotal());
        return dishDtoPage;
    }

    @Override
    public DishDto getDishDto(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        copy(dish,dishDto);

        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);
        this.updateById(dish);
        //不能直接更新口味数据，因为不知道dish_flavor的id
        //先清理口味信息，再新插入数据
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dish.getId());
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);

        //添加当前提交过来的口味数据 dish_flavor的insert操作
        //存dish_flavor
        //使用Stream操作
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dish.getId());
            //log.info("菜品id:--·······················测试{}",dishDto);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);

    }

    @Override
    @Transactional
    public void changeStatuses(Integer status, Long[] idsLong) {
        Dish[] dishes = new Dish[idsLong.length];
        for(int i=0;i<idsLong.length;i++){
            Dish dish = new Dish();
            dish.setId(idsLong[i]);
            dish.setStatus(status);
            dishes[i]=dish;
        }
        this.updateBatchById(Arrays.asList(dishes));
    }

    @Override
    @Transactional
    public void removeWithFlavor(Long[] idsLong) {
        //查询对象状态，要停售了才能删除
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(Dish::getId,idsLong);
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
        int count = this.count(dishLambdaQueryWrapper);
        if(count>0){
            throw new CustomException("需停售才能删除");
        }
        //删除相关口味
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.in(DishFlavor::getDishId,Arrays.asList(idsLong));
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
        //删除菜品
        this.removeByIds(Arrays.asList(idsLong));

    }

    @Override
    public List<DishDto> listDishDtos(Dish dish) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(dish.getCategoryId()!=null, Dish::getCategoryId,dish.getCategoryId());
        dishLambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
        dishLambdaQueryWrapper.like(StringUtils.isNotEmpty(dish.getName()),Dish::getName,dish.getName());
        List<Dish> dishList = this.list(dishLambdaQueryWrapper);
        ArrayList<DishDto> dishDtoList = new ArrayList<>();
        copyList(dishList,dishDtoList);


        return dishDtoList;
    }

    private void copyList(List<Dish> dishList, List<DishDto> dishDtoList) {
        for (Dish dish : dishList) {
            DishDto dishDto = new DishDto();
            copy(dish, dishDto);
            dishDtoList.add(dishDto);
        }

    }

    private void copy(Dish dish, DishDto dishDto) {
        BeanUtils.copyProperties(dish, dishDto);
        //找dish的套餐分类
        Category category = categoryService.getById(dish.getCategoryId());
        String categoryName = category.getName();
        dishDto.setCategoryName(categoryName);

        //添加完整的口味数据
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        dishDto.setFlavors(dishFlavorList);
    }


}
