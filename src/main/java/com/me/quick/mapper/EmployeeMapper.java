package com.me.quick.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.quick.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chen
 * @Date 2022-04-15-15:48
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
