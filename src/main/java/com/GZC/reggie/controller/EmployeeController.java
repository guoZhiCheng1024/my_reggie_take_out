package com.GZC.reggie.controller;

import com.GZC.reggie.common.R;
import com.GZC.reggie.entity.Employee;
import com.GZC.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private  EmployeeService employeeService;


    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        /**
         * 1.将页面提交的password进行md5加密处理
         * 2.根据提交的username查询数据库
         */
        // 1.将页面提交的password进行md5加密处理
        String password = employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());

        //2.根据提交的username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3.如果没有查到返回登录失败
        if (emp==null)
        {
            return R.error("登录失败");
        }

        //4.如果密码错误返回登录失败
        if (!emp.getPassword().equals(password))
        {
            return R.error("登录失败");
        }

        //5.如果状态被禁用返回登录失败
        if (emp.getStatus()==0)
        {
            return R.error("账号被禁用");
        }

        //6.登录成功,将员工Id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    @PostMapping("/logout")
    public R <String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R <String> save(HttpServletRequest request,Employee employee){
        //设置初始密码  123456，用md5进行加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        long empId=(long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        employeeService.save(employee);
        return R.success("新增成员成功");
    }

    /**
     * 分页查询功能
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page (int page ,int pageSize,String name){

       Page pageInfo = new Page(page, pageSize);

       LambdaQueryWrapper <Employee> queryWrapper =new LambdaQueryWrapper();

       queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);

       queryWrapper.orderByDesc(Employee::getUpdateTime);

       employeeService.page(pageInfo,queryWrapper);


        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){

        Long empId =(Long) request.getSession().getAttribute("employee");

        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);

        employeeService.updateById(employee);

        return R.success("员工信息修改成功");
    }

}
