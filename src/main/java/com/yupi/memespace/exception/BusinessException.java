package com.yupi.memespace.exception;

import lombok.Getter;

//使用@Getter自动生成get方法
@Getter

//自定义异常类，属于运行时异常，所以继承RuntimeException
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    //三种构造方法
    //使用 super(message) 而不是 this.message 的原因涉及 Java 异常处理机制，错误信息必须得保存在 Throwable 对象中，否则会丢失
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

}

