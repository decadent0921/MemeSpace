//dto，（Data Transfer Object，数据传输对象），它包含需要传输的数据字段以及相应的getter()和setter()。它不包含任何业务逻辑，只负责数据的传输
//作用是数据格式的适配：不同层之间的数据格式可能不同，比如数据库实体类与前端要求的数据结构不同。可以编写代码来协调双方的数据格式，使得数据传输更加方便
package com.yupi.memespace.model.dto.user;

import lombok.Data;

import java.io.Serializable;
import java.rmi.server.UID;

//用户注册请求dto
//请求dto就是协调前端传到后端的请求的数据格式
@Data
public class UserRegisterRequest implements Serializable {//实现Serializable标记接口，使其对象支持序列化和反序列化，确保DTO对象能够在不同场景中可靠地传输和存储。

    /**这个知识点不太重要
     * serialVersionUID就是序列化的版本号（本身数字不重要），用于验证被序列化的对象是否与当前类的版本一致。
     * 当一个类被序列化后，它的字节会存储在磁盘上或通过网络传输到不同的JVM。
     * 在这种情况下，如果类的结构发生了变化，例如添加了新的字段或方法，JVM反射获取到该类的版本号发生变化，与反序列化时传进来的版本号不一样。*/
    // 默认的serialVersionUID计算对类详细信息高度敏感，不建议使用，建议显示声明
    private static final long serialVersionUID = 2978406077578199395L;

    //账号
    private String userAccount;

    //密码
    private String userPassword;

    //确认密码
    private String checkPassword;
}
