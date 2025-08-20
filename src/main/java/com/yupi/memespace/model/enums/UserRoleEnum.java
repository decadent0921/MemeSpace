package com.yupi.memespace.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

@Getter
public enum UserRoleEnum {

    USER("用户", "user"),
    ADMIN("管理员", "admin");

    private final String text;
    private final String value;

    //枚举类的构造方法默认就是private的，因此private可以省略
    UserRoleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**getEnumByValue()是通过v؜alue找到具体的枚举对象，省的在业务代码中重复写枚举值判断逻辑；
     * 同时如果角色多，调用这个方法大大减少失误率，eg：UserRoleEnum.USER.getValue()比UserRoleEnum.USER失误率低。*/
    //Tip：如果枚举值特别多，可以Map缓存所有؜枚举值来加速查找，而不是遍历列表。
    public static UserRoleEnum getEnumByValue(String value) {
        //校验参数
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        //增强for遍历，枚举类都隐式包含一个静态的values()，它会返回包含所有枚举值的数组
        for (UserRoleEnum anEnum : UserRoleEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
