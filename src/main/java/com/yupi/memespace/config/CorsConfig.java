package com.yupi.memespace.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**跨域问题就是前端和后端不在一个域（可能是端口号不一样，比如前端端口在5678而后端端口在8080，又或者协议不一样），前端请求后端接口的时候，会报跨域问题
   跨域问题只在浏览器里才会出现，因为浏览器会进行同源策略，同源策略是默认不允许跨域请求
   跨域问题的解决方法有一些，做后端开发那么就在后端解决*/

//全局跨域配置类
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 覆盖所有请求
        registry.addMapping("/**")
                // 允许发送 Cookie
                .allowCredentials(true)
                // 放行哪些域名（必须用allowedOriginPatterns()而不是allowedOrigins()，否则 * 会和 allowCredentials 冲突）
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*");
    }
}
