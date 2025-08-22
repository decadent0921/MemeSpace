package com.yupi.memespace.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.memespace.constant.UserConstant;
import com.yupi.memespace.exception.BusinessException;
import com.yupi.memespace.exception.ErrorCode;
import com.yupi.memespace.model.dto.user.UserQueryRequest;
import com.yupi.memespace.model.entity.User;
import com.yupi.memespace.model.enums.UserRoleEnum;
import com.yupi.memespace.model.vo.LoginUserVO;
import com.yupi.memespace.model.vo.UserVO;
import com.yupi.memespace.service.UserService;
import com.yupi.memespace.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//用户服务实现类
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    //用户注册
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1、校验参数
        //使用hutool工具类的hasBlank()，判断参数是否为空
        //后续可以用ThrowUtils类来抛出异常，更加优雅
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }

        //2、校验用户账号是否（和数据库中已有）重复
        /**该类继承MyBatis Plus的ServiceImpl类
         * MyBatis-Plus提供了强大的条件构造器（本质是预先封装好的Java类），通过条件构造器可以写一些复杂的SQL语句
         * QueryWrapper就是一个条件构造器，用于动态构建SQL查询条件。*/
        QueryWrapper<User> queryWrapper = new QueryWrapper
                <>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.baseMapper.selectCount(queryWrapper);//this指的本类的对象，调用baseMapper是因为继承MyBatis-Plus的ServiceImpl类
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }

        //3、加密userPassword
        String encryptPassword = getEncryptPassword(userPassword);

        //4、插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);//这里设置为加密后的密码，要不然管理员能直接查数据库找到你密码了
        user.setUserName("无名");
        user.setUserRole(UserRoleEnum.USER.getValue());//由于该项目角色少，所以觉得调用getValue()很麻烦，但是以后角色多，这样做就大大减少失误率了。
        boolean saveResult = this.save(user);//使用MyBatis Plus提供的save()将对象user持久化到数据库中的user表
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }
        return user.getId();
    }

    //加密userPassword
    //因为以后还要使用，所以单独封装一个public方法
    @Override
    public String getEncryptPassword(String userPassword) {
        /**如果有人知道我们使用的加密算法，就直接逆推出userPassword，所以我们要“加盐”。
         * 在密码学中，"加盐"是一种增强密码存储安全性的技术，它的核心思想是：在用户密码加密之前，先混入一个随机字符串（盐值）。*/
        //这里我们选择一个简单的“盐值”,而不是随机字符串
        String salt = "crc";

        //选择单向加密算法md5...（如果选择对称加密，别人容易通过密钥解密）
        //我们随机选择了一个md5加密，发现其传入参数是Byte[]，所以这里传入的是字符串.getBytes()
        return DigestUtils.md5DigestAsHex((salt+userPassword).getBytes());//使用的spring框架的（使用hutool工具类的加密也可以）
    }

    //用户登录
    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1、校验参数
        if (StrUtil.hasBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码错误");
        }
        //2、对用户传递的userPassword加密，第3步就知道为啥要加密
        String encryptPassword = getEncryptPassword(userPassword);

        //3、查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //默认情况下 MyBatis-Plus的 QueryWrapper会将这些条件用AND连接生成SQL语句
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);//第2步的加密字符串与数据表中的加密字符串对比
        User user = this.baseMapper.selectOne(queryWrapper);//使用selectOne()，获取user对象（如果账号且密码都不匹配，user=null）
        // 若不存在，则抛出异常
        if(user == null){
            log.info("user login failed, userAccount cannot match userPassword");//日志记录最好用精简的英文记录
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");//这里不多写两步说清楚是账号错误还是密码错误是为了安全，防止别人猜完账号再猜密码
        }

        //4、保存用户的登录态
        //session的数据结构就是一个HashMap
        request.getSession()//通过该request对象的getSession()获取其对应的session空间(如果有旧的就进去，没有就创建)
               .setAttribute(UserConstant.USER_LOGIN_STATE, user);//setAttribute()就是将Xxx保存到session空间中
                                                                  //前面说了他是一个Map，所以以键值对形式写
        //this指向的是 UserServiceImpl对象，而不是LoginUserVO对象，所以在LoginUserVO类中写getUserVO()没用，因为就没有LoginUserVO对象，只能在UserServiceImpl类中写
        return this.getLoginUserVO(user);
    }

    //辅助用户登录，为了将User对象转换成脱敏的LoginUserVO对象
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        //这里有很多方法，可以在LoginUserVO类中写一个传入参数为User的构造方法或者使用hutool工具类
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(user, loginUserVO);//一种好用的copy()
        return loginUserVO;
    }

    //将User对象转换成脱敏的UserVO对象，可以用在普通用户查看其他用户等场景中
    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    //获取脱敏用户List，专门给管理员使用
    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {//判断集合是否为空
            return new ArrayList<>();//返回一个空的集合
        }
        return userList.stream()//这里用到了JDK8新增的Stream流，当然Lambda表达式也可以实现
                .map(this::getUserVO)//转成流之后，利用map映射成UserVO对象
                .collect(Collectors.toList());//收集流中的数据放到集合中，返回一个集合，另外Collector是Stream流的一个工具类
    }

    //获取登录用户，算是一种“校验”
    @Override
    public User getLoginUser(HttpServletRequest request) {

        // 1、先判断是否已登录

        //进入session空间（一个Map）通过getAttribute()获取UserConstant.USER_LOGIN_STATE（key）对应的userObj（value），
        //为什么先前存放的明明是User对象，现在需要用Object接收？通过getAttribute()返回值是Object知道需要用Object接收
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;//强制转换；current意思是当前的
        //判断是否登录
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        //1、先判断是否登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }

        //2、移除登录态
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return true;
    }

    //获取查询对象
    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        //先校验参数是否为空
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        //获取查询条件
        Long id = userQueryRequest.getId();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        //使用MyBatis-Plus的QueryWrapper构建动态SQL查询条件

        //eq()，第一个参数boolean类型决定SQL语句是否添加该条件
        queryWrapper.eq(ObjUtil.isNotNull(id), "id", id);
        queryWrapper.eq(StrUtil.isNotBlank(userRole), "userRole", userRole);
        //账号，用户名和用户简介不用一模一样，推荐模糊查询，使用使用like()
        queryWrapper.like(StrUtil.isNotBlank(userAccount), "userAccount", userAccount);
        queryWrapper.like(StrUtil.isNotBlank(userName), "userName", userName);
        queryWrapper.like(StrUtil.isNotBlank(userProfile), "userProfile", userProfile);
        //结果排序规则（id or 其他）和排序方式
        //sortField是前端传的排序字段
        queryWrapper.orderBy(StrUtil.isNotEmpty(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }



}




