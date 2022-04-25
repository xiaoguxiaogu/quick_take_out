package com.me.quick.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.me.quick.common.R;
import com.me.quick.entity.Category;

/**
 * @author chen
 * @Date 2022-04-16-17:27
 */
public interface CategoryService extends IService<Category> {
    /**
     * 查询所有菜品分类
     * @param p
     * @param pageSize
     * @return
     */
    R<Page> listCategories(int p, int pageSize);

    /**
     * 添加菜品/套餐分类
     * @param category
     * @return
     */
    R<String> addCategory(Category category);

    /**
     * 修改分类
     * @param category
     * @return
     */
    R<String> updateCategory(Category category);

    /**
     * 删除分类,需要先判断是否关联
     * @param id
     * @return
     */
    R<String> deleteCategory(Long id);
}
