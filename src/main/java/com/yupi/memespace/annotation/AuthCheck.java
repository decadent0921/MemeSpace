package com.yupi.memespace.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//通过ElementType枚举来指定注解生效范围的注解
@Retention(RetentionPolicy.RUNTIME)//指定注解的保留策略，即注解在什么时候可用（编译时、字节码处理，还是运行时动态读取），通过RetentionPolicy枚举来指定
public @interface AuthCheck {

    //必须有某个角色
    //解释：这个注解要配合角色权限校验拦截器（AOP）使用，拦截器拦截带有@AuthCheck注解的方法
    String mustRole() default "";
}
