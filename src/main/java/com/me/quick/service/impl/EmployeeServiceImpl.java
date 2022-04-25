package com.me.quick.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.me.quick.common.R;
import com.me.quick.entity.Employee;
import com.me.quick.mapper.EmployeeMapper;
import com.me.quick.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @author chen
 * @Date 2022-04-15-15:50
 */
@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public R<Employee> login(Employee employee, HttpServletRequest request) {
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee empReturn = employeeMapper.selectOne(queryWrapper);
        if(empReturn==null){
            return R.error("登录失败");
        }
        if(!password.equals(empReturn.getPassword())){
            return R.error("账号或密码错误");
        }
        if(empReturn.getStatus()==0){
            return R.error("账号已禁用");
        }
        request.getSession().setAttribute("employee",empReturn.getId());

        return R.success(empReturn);
    }

    @Override
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @Override
    public R<String> addEmpoyee(HttpServletRequest request, Employee employee) {
        //设置默认初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));
        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());
        //获取当前登录的用户
        //employee.setCreateUser((Long)request.getSession().getAttribute("employee"));
        //employee.setUpdateUser((Long)request.getSession().getAttribute("employee"));
        employeeMapper.insert(employee);
        return R.success("添加成功");
    }

    @Override
    public R<Page> listEmpoyees(int page, int pageSize, String name) {
        //分页构造器
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeMapper.selectPage(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    @Override
    public R<String> updateEmployee(HttpServletRequest request, Employee employee) {
       // employee.setUpdateTime(LocalDateTime.now());
        //employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        long id = Thread.currentThread().getId();
        log.info("线程id为：{}",id);
        employeeMapper.updateById(employee);

        return R.success("修改成功");
    }

    @Override
    public R<Employee> listEmpoyee(long id) {
        Employee employee = employeeMapper.selectById(id);
        if(employee!=null){
            return R.success(employee);
        }
        return R.error("查询失败");
    }
}
