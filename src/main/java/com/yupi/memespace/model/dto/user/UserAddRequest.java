package com.yupi.memespace.model.dto.user;

import lombok.Data;

import java.io.Serializable;

//用户添加dto（管理员负责）
//请求dto就是协调前端传到后端的请求的数据格式
@Data
public class UserAddRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色: user, admin
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}
