package com.yupi.memespace.model.vo;

import com.yupi.memespace.model.entity.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;


//用户视图（脱敏），比如普通用户查看其他用户的信息，肯定不能查到手机号之类的信息，所以写一个UserVO
@Data
public class UserVO implements Serializable {

    //id
    private Long id;
    
    //账号
    private String userAccount;

    //用户名
    private String userName;

    //用户头像
    private String userAvatar;

    //用户简介
    private String userProfile;

    //用户角色：user/admin
    private String userRole;

    //创建时间
    private Date createTime;



    private static final long serialVersionUID = 1L;
}
