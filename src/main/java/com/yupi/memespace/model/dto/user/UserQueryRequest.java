package com.yupi.memespace.model.dto.user;

import com.yupi.memespace.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

//用户查询请求dto
//请求dto就是协调前端传到后端的请求的数据格式
@EqualsAndHashCode(callSuper = true)//这个不重要，但是得写
@Data
//查询操作中高频需要分页中的页码，一页多少条等参数，通用代码中有基础的分页请求，可以直接继承使用
public class UserQueryRequest extends PageRequest implements Serializable {

    //定义了一些查询条件

    //根据id查询
    private Long id;

    //根据用户名查询
    private String userName;

    //根据用户账号查询
    private String userAccount;

    //根据用户简介查询
    private String userProfile;

    //根据用户角色查询
    private String userRole;

    private static final long serialVersionUID = 1L;
}
