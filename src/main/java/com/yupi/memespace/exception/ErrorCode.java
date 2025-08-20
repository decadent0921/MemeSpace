package com.yupi.memespace.exception;

import lombok.Getter;

//使用@Getter自动生成get方法
@Getter

//自定义异常枚举类，集中管理所有exception

public enum ErrorCode {
    /**设置状态码的原因和响应状态码原因一样，一是机器可读性更高，二是可以分类。
     * 分类参考了主流的HTTP响应状态码，4xx是客户端错误，5xx是服务器错误
     * 五位数是为了以后扩展（细化）报错信息
     */
    SUCCESS(0, "ok"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "无权限"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败");


    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
