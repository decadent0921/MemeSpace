package com.yupi.memespace.common;

import lombok.Data;

import java.io.Serializable;


//在通用包中，因为大部分项目都需要，按正常开发逻辑，该类应该在用户请求dto中
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
