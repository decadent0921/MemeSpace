package com.yupi.memespace.aop;

import com.yupi.memespace.annotation.AuthCheck;
import com.yupi.memespace.exception.BusinessException;
import com.yupi.memespace.exception.ErrorCode;
import com.yupi.memespace.model.entity.User;
import com.yupi.memespace.model.enums.UserRoleEnum;
import com.yupi.memespace.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Aspect//表示是一个切面
@Component//表示该类进入Spring容器成为一个Bean
public class AuthInterceptor {//权限拦截器

    //引入UserService，因为权限校验肯定要获取登录的用户信息
    @Resource
    private UserService userService;

    /**
     * 执行拦截
     *
     * @param joinPoint 切入点
     * @param authCheck 权限校验注解
     */
    @Around("@annotation(authCheck)")//@Around和@Before都可以用，@Around因为前后都能执行，效果更好
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        //获取当前角色
        String mustRole = authCheck.mustRole();

        //通过mustRole获取被注解的方法所要求的角色值
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
        // 如果角色值为空，说明这个方法虽然被@authCheck修饰，但是@authCheck的要求为空，所有用户（包括游客）可直接访问，直接放行
        if (mustRoleEnum == null) {
            return joinPoint.proceed();
        }

        /*上面代码说明被修饰方法没有角色要求已经放行了，能执行到后面的代码说明被修饰方法一定有一个要求的角色值（即mustRoleEnum非空）！
          这里没有使用else，所以可读性较差，但是逻辑没问题*/

        //获取请求对象
        /**通过全局上下文RequestContextHolder（Spring提供的一个工具类，用于在当前线程中获取与HTTP请求相关的上下文信息）
         * 其getRequestAttributes()就能获取当前请求所有的属性
         * 然后强制转换为其子类，调用getRequest()就能获取到咱们熟知的HttpServletRequest对象*/
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        // 通过请求获取当前登录用户（调用的这个方法里面第一步就包含了判断是否已经登录，所以这里不用校验）
        User loginUser = userService.getLoginUser(request);

        // 通过loginUser获取当前用户具有的角色值
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        // 用户的角色值为null，说明当前用户没有角色，返回“无权限”
        if (userRoleEnum == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 被注解的方法必须有管理员权限，但用户没有管理员权限，拒绝
        if (UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(userRoleEnum)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 经过前面的权限校验，放行
        return joinPoint.proceed();
    }
}
