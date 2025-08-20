package com.yupi.memespace.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.memespace.model.dto.user.UserQueryRequest;
import com.yupi.memespace.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.memespace.model.vo.LoginUserVO;
import com.yupi.memespace.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface UserService extends IService<User> {

    //关于用户注册的抽象方法
    //接口中方法默认public abstract，所以不需要写public abstract
    //抽象方法不能加{}
    long userRegister(String userAccount, String userPassword, String checkPassword);

    //加密userPassword的抽象方法
    String getEncryptPassword(String userPassword);

    //关于用户登录的抽象方法
    /** HttpServletRequest它封装了客户端HTTP请求的所有信息在登录方法中传入该对象，
     * 原因是登录成功后，通常需要将用户信息存入 Session，避免重复认证 */
    //返回值应该是登录完的东西，一般使用VO（视图对象），同时注意要脱敏
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    //User转换成VO的抽象方法，在登录成功后，返回的信息得是VO
    //为什么在这里写这个方法？原因找UserServiceImpl类用户登录方法的最后一行注释，并且这个方法在其他功能中还可以使用
    LoginUserVO getLoginUserVO(User user);

    //User转换成VO的抽象方法，可以用在普通用户查看其他用户等场景中
    UserVO getUserVO(User user);

    //获取用户List的抽象方法，专门给管理员使用
    List<UserVO> getUserVOList(List<User> userList);


    //关于登录完获取登录用户的抽象方法
    //前面登录是存储在session中的，这里从session中获取，获取出来可以展示前端，还可以在后端传递实现其他功能
    //为什么不是VO而是User？因为返回User既可以在后端之间传递使用，并且返回前端只要在controller中调用完再使用getLoginUserVO()就可以转换成VO
    User getLoginUser(HttpServletRequest request);

    //用户注销，这里的注销指的退出登录，前面证明登录是通过session空间存储的登录态，所以退出登录就是移除登录态
    boolean userLogout(HttpServletRequest request);

    //关于获取查询“语句”QueryWrapper的抽象方法
    /**假设有一个用户查询功能，支持10+字段的过滤条件，实现这个功能太多重复代码，
     * 所以写一个方法，把普通的Java对象转换成Mybatis Plus需要的QueryWrapper对象*/
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    boolean isAdmin(User user);

}
