package com.GZC.reggie.common;


import java.time.LocalDateTime;

/**
 * 基于ThreadLocal封装的工具类，用户保存和获取当前登录的用户id
 */
public class BaseContext {
    private static ThreadLocal <Long> threadLocal =new ThreadLocal<>();

    public static Long getCurrentId (){
        return threadLocal.get();

    }

    public static void setCurrentId (Long id){
        threadLocal.set(id);
    }

}
