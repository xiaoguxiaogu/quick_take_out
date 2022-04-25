package com.me.quick.common;

/**
 * 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 * @author chen
 * @Date 2022-04-16-21:04
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public  static Long getCurrentId(){
        return threadLocal.get();
    }

    public  static void remove(){
        threadLocal.remove();
    }
}
