//constant，存储常量
//常量更适用于单一或者单一组合的全局性应用，枚举更适合多组合多属性关联（例如SUN(0, "星期日")）的复杂情景应用，二者各有其用
package com.yupi.memespace.constant;

//用户常量
public interface UserConstant {//接口很适合定义常量，因为接口的成员变量默认是public static final
    //用户状态方面
    //用户登录态键，在用户登录后，为了维持登录态，保存进session中时作为键值对的键
    String USER_LOGIN_STATE = "user_login";


    //用户权限方面

    //默认角色
    String DEFAULT_ROLE = "user";

    //管理员角色
    String ADMIN_ROLE = "admin";

}
