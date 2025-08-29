package com.yupi.memespace.model.dto.space;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 空间等级（可以作为响应或者DTO）
 */
@Data
@AllArgsConstructor
public class SpaceLevel {

    /**
     * 等级值
     */
    private int value;

    /**
     * 中文
     */
    private String text;

    /**
     * 最大数量
     */
    private long maxCount;

    /**
     * 最大容量
     */
    private long maxSize;
}
