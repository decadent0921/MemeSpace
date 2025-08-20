//common包就是通用包
package com.yupi.memespace.common;

import com.yupi.memespace.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

//@Data用于自动生成类的getter、setter、toString、equals和hashCode方法，简化Java代码
@Data


//统一全局响应结果，又可称为“全局响应结果封装”
/**
 * 在Java的Spring Boot应用中，响应自动转为 JSON 格式是由框架的消息转换器自动处理的，不需要手动设置响应格式。
 * 但是我们需要设置响应结果要传递哪些信息，换言之就是无论正确还是报错，通常会对响应结果进行统一封装，而不是直接将信息混乱地一股脑全部暴露。
 * */
public class BaseResponse<T> implements Serializable {
/**
 * Java要求，如果一个类的对象需要被序列化，该类必须实现Serializable接口
 * 这个接口是一个标记接口，没有任何方法，仅用于告诉 JVM："这个类的对象支持被序列化。"
 * */

//即使暂时不用序列化，也建议实现 Serializable接口，避免未来扩展分布式架构时大规模重构

    private int code;

    //data的数据类型不确定，推荐使用泛型
    private T data;

    private String message;

    //三个构造方法
    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}


