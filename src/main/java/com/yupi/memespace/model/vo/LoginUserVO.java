//VO就是返回给前端的数据，所以叫视图对象
//大体上和dto很相似，区别是VO是后端返回给前端的数据，dto是前端传递给后端的数据或者在后端层级间传递的数据
package com.yupi.memespace.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


//顺便脱敏的登录用户的VO类
@Data
public class LoginUserVO implements Serializable {
    //ID
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

    private static final long serialVersionUID = 1L;
}
