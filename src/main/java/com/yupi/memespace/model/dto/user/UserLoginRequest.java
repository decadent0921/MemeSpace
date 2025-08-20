//dto，（Data Transfer Object，数据传输对象），它包含需要传输的数据字段以及相应的getter()和setter()。它不包含任何业务逻辑，只负责数据的传输
//作用是数据格式的适配：不同层之间的数据格式可能不同，比如数据库实体类与前端要求的数据结构不同。可以编写代码来协调双方的数据格式，使得数据传输更加方便
package com.yupi.memespace.model.dto.user;


import lombok.Data;

import java.io.Serializable;

//用户登录请求dto
//请求dto就是协调前端传到后端的请求的数据格式
@Data
public class UserLoginRequest implements Serializable {
    //序列化版本号
    private static final long serialVersionUID = 5091374837221616709L;

    //账号
    private String userAccount;

    //密码
    private String userPassword;
}
