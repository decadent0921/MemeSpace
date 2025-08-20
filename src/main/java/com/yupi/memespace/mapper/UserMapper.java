package com.yupi.memespace.mapper;

import com.yupi.memespace.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author www
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2025-08-03 18:56:56
* @Entity com.yupi.memespace.model.entity.User
*/
public interface UserMapper extends BaseMapper<User> {
    /**BaseMapper 是 MyBatis-Plus 提供的一个通用接口，
     * 它已经包含了常见的数据库操作方法（如插入、删除、更新、查询等）。*/
}




