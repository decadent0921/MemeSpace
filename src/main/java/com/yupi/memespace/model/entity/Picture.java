package com.yupi.memespace.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 图片
 * @TableName picture
 */
@TableName(value ="picture")
@Data
public class Picture implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 编辑时间
     */
    private Date editTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 图片格式
     */
    private String picFormat;

    /**
     * 图片宽高比例
     */
    private Double picScale;

    /**
     * 图片高度
     */
    private Integer picHeight;

    /**
     * 图片宽度
     */
    private Integer picWidth;

    /**
     * 图片体积
     */
    private Long picSize;

    /**
     * 标签（JSON 数组）
     */
    //MySQL将JSON（一种格式）定义成了一个数据类型，Java还没有，所以还像以前一样，使用String接收。
    private String tags;//

    /**
     * 分类
     */
    private String category;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 图片名称
     */
    private String name;

    /**
     * 图片 url
     */
    private String url;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}