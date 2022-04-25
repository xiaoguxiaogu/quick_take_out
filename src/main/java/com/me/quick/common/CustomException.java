package com.me.quick.common;

/**
 * 自定义异常
 * @author chen
 * @Date 2022-04-17-20:18
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
