package com.me.quick.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.me.quick.common.R;
import com.me.quick.entity.Employee;
import com.me.quick.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 员工页面接口
 * @author chen
 * @Date 2022-04-15-15:50
 */
@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param request cookie和session
     * @param employee
     * @return
     */
    @PostMapping ("login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        return employeeService.login(employee,request);
    }


    @PostMapping("logout")
    public R<String> logout(HttpServletRequest request ){
        return employeeService.logout(request);
    }

    @PostMapping
    public R<String> addEmployee(HttpServletRequest request, @RequestBody Employee employee){

        return employeeService.addEmpoyee(request,employee);
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")//不是restform风格
    public R<Page> listEmployees( int page, int pageSize, String name){
        return employeeService.listEmpoyees(page,pageSize,name);

    }

    /**
     * 修改员工状态：1.启用，0，禁用
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> changeStatus(HttpServletRequest request,@RequestBody Employee employee){
        return employeeService.updateEmployee(request,employee);
    }

    @GetMapping("{id}")
    public R<Employee> listEmployee(@PathVariable("id") long id){
        System.out.println(id);
        return employeeService.listEmpoyee(id);
    }

}
