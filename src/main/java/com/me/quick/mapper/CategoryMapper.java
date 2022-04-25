package com.me.quick.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.quick.entity.Category;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chen
 * @Date 2022-04-16-17:26
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
