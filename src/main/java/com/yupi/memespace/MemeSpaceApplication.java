package com.yupi.memespace;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

import java.sql.SQLOutput;

@SpringBootApplication
//MyBatis Plus使用要求
@MapperScan("com.yupi.memespace.mapper")
//（可选）开启AOP，exposeProxy = true意思是暴露代理对象，这样在任何地方都可以通过xxx方法获取当前代理对象
@EnableAspectJAutoProxy(exposeProxy = true)
//开启异步支持
@EnableAsync
public class MemeSpaceApplication {

    public static void main(String[] args) {

        SpringApplication.run(MemeSpaceApplication.class, args);
    }

}
