package com.yupi.memespace.common;

import lombok.Data;

//@Data用于自动生成类的getter、setter、toString、equals和hashCode方法，简化Java代码
@Data
//通用的请求中的分页类
//以后要是有相关类用到分页，就可以继承这个类，继承到他的成员变量（属性）了

//除此之外，我们要有一个意识，意识到要对常用的请求类进行封装，封装成通用的类！！！除了分页请求还可以封装其他类，比如删除请求类
public class PageRequest {

    //当前页号
    private int current = 1;

    //页面大小
    private int pageSize = 10;

    //排序字段
    private String sortField;

    //排序顺序（默认降序）
    private String sortOrder = "descend";
}
