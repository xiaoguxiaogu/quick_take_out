package com.me.quick.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.me.quick.common.R;
import com.me.quick.entity.Category;
import com.me.quick.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author chen
 * @Date 2022-04-16-17:28
 */
@RestController
@RequestMapping("category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("page")
    public R<Page> listCategories(@RequestParam("page") int p, int pageSize){
        return categoryService.listCategories(p,pageSize);
    }



    @PostMapping
    public R<String> addCategory(@RequestBody Category category){
        return categoryService.addCategory(category);
    }


    @PutMapping
    public R<String > updateCategory(@RequestBody Category category){
        return categoryService.updateCategory(category);
    }

    @DeleteMapping
    public R<String> deleteCategory(@RequestParam("ids") Long id){
        return categoryService.deleteCategory(id);
    }

    @GetMapping("list")
    public R<List<Category>> listCategories(Category category ){
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        List<Category> categoryList = categoryService.list(categoryLambdaQueryWrapper);
        return R.success(categoryList);
    }
}
