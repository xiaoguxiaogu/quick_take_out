package com.me.quick.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.me.quick.common.R;
import com.me.quick.entity.Employee;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chen
 * @Date 2022-04-15-15:49
 */
public interface EmployeeService {
    R<Employee> login(Employee employee, HttpServletRequest request);

    R<String> logout(HttpServletRequest request);

    /**
     * 新增员工
     *
     * @param request
     * @param employee
     * @return
     */
    R<String> addEmpoyee(HttpServletRequest request, Employee employee);

    /**
     * 员工分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    R<Page> listEmpoyees(int page, int pageSize, String name);

    /**
     * 修改员工信息
     *
     * @param request
     * @param employee
     * @return
     */
    R<String> updateEmployee(HttpServletRequest request, Employee employee);

    /**
     * 查询单个员工
     * @param id
     * @return
     */
    R<Employee> listEmpoyee(long id);
}
