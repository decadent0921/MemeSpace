package com.yupi.memespace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.memespace.annotation.AuthCheck;
import com.yupi.memespace.common.BaseResponse;
import com.yupi.memespace.common.DeleteRequest;
import com.yupi.memespace.common.ResultUtils;
import com.yupi.memespace.constant.UserConstant;
import com.yupi.memespace.exception.BusinessException;
import com.yupi.memespace.exception.ErrorCode;
import com.yupi.memespace.exception.ThrowUtils;
import com.yupi.memespace.model.dto.user.*;
import com.yupi.memespace.model.entity.User;
import com.yupi.memespace.model.enums.UserRoleEnum;
import com.yupi.memespace.model.vo.LoginUserVO;
import com.yupi.memespace.model.vo.UserVO;
import com.yupi.memespace.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

//@RestController关注响应处理,所有方法的返回值转换为json格式，写入HTTP响应体
@RestController
//@RequestMapping关注请求的路由（匹配 URL）
@RequestMapping("/user")
public class UserController {

    /**注解@Resource和注解@Autowired区别常考，
     * 两个都是从Spring容器中获取该Bean并注入到当前类中（这就叫依赖注入）的注解
     * 注解@Autowired，Spring框架自带的自动注入，按照类型自动装配，如果有多个同类型的Bean，则需要通过@Qualifier指定具体的Bean；
     * 注解@Resource，Java自带的自动注入，按照名称进行自动装配。
     * 一般情况，建议用 @Resource*/
    @Resource
    private UserService userService;//这样的好处就是不依赖某个具体的实现类，而是依赖抽象接口，通过注解匹配合适的类



    //用户注册
    //POST请求，将数据保存到服务器端
    @PostMapping("/register")
    /**注解@RequestBody用于将HTTP请求体中的JSON/XML数据自动绑定到Java对象，
     * 在该代码中，将客户端发送的JSON数据反序列化为UserRegisterRequest对象（一个dto对象）。*/
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        //1、校验前端传入的参数
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        //2、提取前端传入的参数
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        //3、调用service层（核心操作）进行注册
        //这里的userService是通过注解实现DI所匹配到的的userServiceImpl
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        //4、返回响应封装类
        return ResultUtils.success(result);
    }


    //用户登录
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    //获取当前登录用户
    //可能不好理解，简单说就是前面登录成功后，将用户信息保存在服务器中的session空间，这里是通过session空间获取用户信息
    @GetMapping("/get/login")//GET请求，因为是从服务器中的session空间获取用户信息，所以使用GET请求
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(loginUser));//注意，controller返回的响应是要传给前端的，为了脱敏传递的是UserVO
    }

    //用户注销，这里的注销指的退出登录，前面证明登录是通过session空间存储的登录态，所以退出登录就是移除登录态
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        //1、校验参数
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);

        //2、调用userLogout()
        boolean result = userService.userLogout(request);

        return ResultUtils.success(result);
    }

    /**有同学可能会有疑惑：不是说不要在Controller中写؜业务逻辑代码么？
     * 我的建议是开发时要灵活一些，我们要保证Controller的精简没错，但不代表什么代码都不在Contro؜ller里写。
     * 对于后面这些代码，根本就没有复杂的业务逻辑。
     */

    //创建用户（仅管理员）
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        // 默认密码 12345678
        final String DEFAULT_PASSWORD = "12345678";
        String encryptPassword = userService.getEncryptPassword(DEFAULT_PASSWORD);
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    //根据 id 获取用户（仅管理员）
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    //根据 id 获取包装类
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id) {
        BaseResponse<User> response = getUserById(id);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    //删除用户（仅管理员）
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    //更新用户（仅管理员）
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    //分页获取用户封装列表（仅管理员）
    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long current = userQueryRequest.getCurrent();
        long pageSize = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, pageSize),
                userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, pageSize, userPage.getTotal());
        List<UserVO> userVOList = userService.getUserVOList(userPage.getRecords());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }



}
