package com.yupi.memespace.model.dto.user;

import lombok.Data;

import java.io.Serializable;

//更新请求dto
//请求dto就是协调前端传到后端的请求的数据格式
@Data
public class UserUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}
