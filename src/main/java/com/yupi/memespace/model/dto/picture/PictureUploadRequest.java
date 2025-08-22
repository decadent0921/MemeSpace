package com.yupi.memespace.model.dto.picture;

import lombok.Data;

import java.io.Serializable;

@Data
public class PictureUploadRequest implements Serializable {
  
    //图片 id（用于修改）
    /**因为这个项目的业务逻辑是将“上传图片”细化成仅上传图片和编辑图片信息，
     * 简单讲就是先上传进去，再编辑图片的介绍呀之类的信息完成真正意义上的“上传图片”，
     * 而现在这个dto就是上传功能的第一步业务，仅上传。
     */

    private Long id;

    private String fileUrl;

    private static final long serialVersionUID = 1L;
}
