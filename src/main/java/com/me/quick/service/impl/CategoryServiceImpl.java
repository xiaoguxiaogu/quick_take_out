package com.me.quick.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.quick.common.CustomException;
import com.me.quick.common.R;
import com.me.quick.entity.Category;
import com.me.quick.entity.Dish;
import com.me.quick.entity.Setmeal;
import com.me.quick.mapper.CategoryMapper;
import com.me.quick.service.CategoryService;
import com.me.quick.service.DishService;
import com.me.quick.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @author chen
 * @Date 2022-04-16-17:27
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public R<Page> listCategories(int p, int pageSize) {
        Page<Category> page = new Page<>(p,pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);

        categoryMapper.selectPage(page,queryWrapper);

        return R.success(page);
    }

    @Override
    public R<String> addCategory(Category category) {
        categoryMapper.insert(category);
        return R.success("新增分类成功");
    }

    @Override
    public R<String> updateCategory(Category category) {
        categoryMapper.updateById(category);
        return R.success("修改成功");
    }

    @Override
    public R<String> deleteCategory(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        //判断是否关联菜品
        if(count1>0){
            //抛出业务异常
            throw new CustomException("当前分类关联了菜品，不能删除");
        }
        //判断是否关联套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if(count2>0){
            //抛出业务异常
            throw new CustomException("当前分类关联了套餐，不能删除");
        }

        //正常删除
        categoryMapper.deleteById(id);
        return R.success("删除成功");
    }


}
