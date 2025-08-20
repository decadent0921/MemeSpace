package com.yupi.memespace.exception;

//一个类似断言的工具类ThrowUtils，目的和断言一样，为了简化判断语句的代码量
/*
* 举例理解
* 没有这个工具类，平时写抛出异常得这样写：
  if(noAuth){
    throw new BusinessException(ErrorCode,NO_AUTH_ERROR,"没权限");
  }
* 有了这个工具类，可以这样写：
  ThrowUtils.throwIf(noAuth,ErrorCode.NO_AUTH_ERROR,"没权限");
* */
public class ThrowUtils {

    //三个方法名一样的方法体现了函数的重载

    /**
     * 条件成立则抛异常
     * condition        条件
     * runtimeException 异常
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     * condition 条件
     * errorCode 枚举类错误码
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        //如果还按照前面“if...”写，不够优雅，可以用前面刚写完的throwIf()，这样可以少写一些代码
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * 条件成立则抛异常
     * condition 条件
     * errorCode 枚举类错误码
     * message   错误信息
     */
    //如果想将错误信息描述更加准确，不想用那个错误码对应的报错信息，想自己写更为详细的，所以再写一个该重载方法
    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }
}

