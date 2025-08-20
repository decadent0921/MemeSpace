package com.yupi.memespace.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@TableName(value ="user")
@Data
public class User implements Serializable {

    //ID
    /** MyBatis Plus的注解，其中type属性表示主键的生成策略
     * 属性值IdType.AUTO表示1，2...N这样自增，这样生成id有一个缺点，就是别人非常容易去爬取你的数据
     * 属性值IdType.ASSIGN_ID表示生成相对较长的不连续的id
     * */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    //账号
    private String userAccount;

    //密码
    private String userPassword;

    //用户昵称
    private String userName;

    //用户头像
    private String userAvatar;

    //用户简介
    private String userProfile;

    //用户角色：user/admin
    private String userRole;

    //编辑时间
    private Date editTime;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //逻辑删除
    @TableLogic//让MyBatis Plus框架知道这个字段是逻辑删除字段
    private Integer isDelete;

    //版本号
    private static final long serialVersionUID = 1L;
}