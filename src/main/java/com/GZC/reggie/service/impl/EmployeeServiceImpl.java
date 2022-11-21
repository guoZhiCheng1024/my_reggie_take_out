package com.GZC.reggie.service.impl;


import com.GZC.reggie.entity.Employee;
import com.GZC.reggie.mapper.EmployeeMapper;
import com.GZC.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService
    {

}
