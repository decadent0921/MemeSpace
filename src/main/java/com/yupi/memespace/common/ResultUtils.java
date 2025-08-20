package com.yupi.memespace.common;

import com.yupi.memespace.exception.ErrorCode;

//统一全局响应结果的工具类

//目的和ThrowUtils基本一样，为了简化代码量，但是这个工具类不是参考断言类，而是单纯为了简化“new...”的代码量
/*
* 举例理解
* 原来：
* return new BaseResponse<>(ErrorCode.NO_AUTH_ERROR，data);
* 之后：
* return ResultUtils.error(ErrorCode.NO_AUTH_ERROR);
* */
public class ResultUtils {


    //泛型方法有格式要求：修饰符<类型> 返回值类型 方法名(类型 变量名){}
    public static<T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    //data为null，所以泛型使用通配符？来表示返回值中的泛型类型不确定或无需关心
    public static BaseResponse<?> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }


    public static BaseResponse<?> error(int code, String message) {
        return new BaseResponse<>(code, null, message);
    }

    public static BaseResponse<?> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), null, message);
    }
}
