package com.GZC.reggie.common;

//全局异常处理器

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;


@ControllerAdvice (annotations = {RestController.class, Controller.class})//设置拦截加有括号中的注释的类
public class GlobalExceptionHandler {
    /**
     * 注解声明捕捉的是哪一个异常类，然后将异常传进函数中处理
     * @param ex
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptHandler (SQLIntegrityConstraintViolationException ex){

        if (ex.getMessage().contains("Duplicate entry"))
        {
            String[] split = ex.getMessage().split(" ");
            String msg= split[2] +"已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }
}
