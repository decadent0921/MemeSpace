package com.yupi.memespace.exception;

import com.yupi.memespace.common.BaseResponse;
import com.yupi.memespace.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//环绕切面注解，它结合了@ControllerAdvice和@ResponseBody的功能，专门用于全局异常处理
@RestControllerAdvice
//日志注解，能够输出日志
@Slf4j

//全局异常处理器，为了防止开发时没有发现的异常同时兼顾打印报错日志的功能，所以捕获自定义业务异常和运行时异常
//全局异常处理器本质的捕获异常是在抛出异常的时候，会自动调用这个方法，说明抛出异常和捕获异常没有绝对的2选1，可以是合作关系
public class GlobalExceptionHandler {

    //捕获自定义业务异常
    //@ExceptionHandler字面意思就是异常处理器。这行代码的意思是，当出现BusinessException异常时，就会被捕获并执行该方法
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    //自定义异常有时涵盖不够全面，再写一个对运行时异常的捕获来补充，这样就实现全局涵盖
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }
}


