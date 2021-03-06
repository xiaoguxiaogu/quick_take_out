package com.me.quick.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.quick.common.CustomException;
import com.me.quick.dto.SetmealDto;
import com.me.quick.entity.Setmeal;
import com.me.quick.entity.SetmealDish;
import com.me.quick.mapper.SetmealMapper;
import com.me.quick.service.CategoryService;
import com.me.quick.service.SetmealDishService;
import com.me.quick.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chen
 * @Date 2022-04-17-19:57
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {


    @Autowired
    private SetmealDishService setmealDishService;
    
    @Autowired
    private CategoryService categoryService;


    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto,setmeal);
        this.save(setmeal);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmeal.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }



    @Override
    public Page<SetmealDto> pageSetmealDtos(int page, int pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);
        this.page(setmealPage, setmealLambdaQueryWrapper);

        //?????????pagesetmealDtos
        List<Setmeal> setmealList = setmealPage.getRecords();
        List<SetmealDto> setmealDtoList = new ArrayList<>();
        copyList(setmealList, setmealDtoList);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        setmealDtoPage.setRecords(setmealDtoList);
        setmealDtoPage.setTotal(setmealPage.getTotal());
        return setmealDtoPage;
    }

    @Override
    public SetmealDto getSetmealDto(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        copy(setmeal,setmealDto);
        return setmealDto;
    }

    @Override
    @Transactional
    public void updateWithDish(SetmealDto setmealDto) {
        //?????????setmeal??????
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto,setmeal);
        this.updateById(setmeal);
        //?????????setmeal_dish??????????????????????????????
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
        //??????
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmeal.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);

    }

    /**
     * ??????????????????
     * @param status
     * @param ids
     */
    @Override
    @Transactional
    public void changeStatus(Integer status, Long[] ids) {
        ArrayList<Setmeal> setmealList = new ArrayList<>();
        for (Long id : ids) {
            Setmeal setmeal = new Setmeal();
            setmeal.setStatus(status);
            setmeal.setId(id);
            setmealList.add(setmeal);
        }
        this.updateBatchById(setmealList);
    }

    @Override
    @Transactional
    public void removeWithDish(Long[] ids) {
        //????????????????????????????????????????????????
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId,ids);
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(setmealLambdaQueryWrapper);
        if(count>0){
            throw new CustomException("???????????????????????????????????????");

        }
        //???????????????????????????
        //?????????setmeal????????????
        this.removeByIds(Arrays.asList(ids));
        //??????setmeal_dish????????????
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
    }

    @Override
    public List<SetmealDto> listSetmealDtos(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        setmealLambdaQueryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        List<Setmeal> setmealList = this.list(setmealLambdaQueryWrapper);
        ArrayList<SetmealDto> setmealDtoList = new ArrayList<>();
        copyList(setmealList,setmealDtoList);

        return setmealDtoList;
    }

    private void copyList(List<Setmeal> setmealList, List<SetmealDto> setmealDtoList) {
        for (Setmeal setmeal : setmealList) {
            SetmealDto setmealDto = new SetmealDto();
            copy(setmeal, setmealDto);
            setmealDtoList.add(setmealDto);
        }
    }

    /**
     * ??????setmeal???setmealDto
     * @param setmeal
     * @param setmealDto
     */
    private void copy(Setmeal setmeal, SetmealDto setmealDto) {
        BeanUtils.copyProperties(setmeal, setmealDto);
        //??????????????????
        setmealDto.setCategoryName(categoryService.getById(setmeal.getCategoryId()).getName());
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
        //??????????????????
        List<SetmealDish> setmealDishes = setmealDishService.list(setmealDishLambdaQueryWrapper);
        setmealDto.setSetmealDishes(setmealDishes);


    }
}
