//manager包一般指可以复用的代码，不仅仅在这个项目中可以使用，还可以作用到其他地方
package com.yupi.memespace.manager;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.PicOperations;
import com.yupi.memespace.config.CosClientConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;


//通用的对象存储操作，比如文件上传、文件下载等
@Component
public class CosManager {

    @Resource
    private CosClientConfig cosClientConfig;
  
    @Resource  
    private COSClient cosClient;

    //上传文件，根据开发文档来写的(练手)
    public PutObjectResult putObject(String key, File file) {

        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key,
                file);
        return cosClient.putObject(putObjectRequest);
    }

    //下载对象
    /**官方文档介绍了2种文件下载方式。
     * 一种是获取到文件下载؜输入流（适合返回给前端用户），
     * 另一种是直接下载COS的文件到后؜端服务器（适合服务器端处理文件）。
     * 其实还有第三种“下载方式”，直接通过URL路径链接访问，؜适用于单一的、可以被用户公开访问的资源，比如用户؜头像、本项目中的公开图片。
     *
     * */
    //还是演示官方文档的第二种下载方式
    public COSObject getObject(String key) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(cosClientConfig.getBucket(), key);
        return cosClient.getObject(getObjectRequest);
    }

    /**
     * 上传对象（附带图片信息）
     *
     * @param key  唯一键
     * @param file 文件
     */
    public PutObjectResult putPictureObject(String key, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key,
                file);
        // 对图片进行处理（获取基本信息也被视作为一种处理）
        PicOperations picOperations = new PicOperations();
        // 1 表示返回原图信息
        picOperations.setIsPicInfo(1);
        // 构造处理参数
        putObjectRequest.setPicOperations(picOperations);
        return cosClient.putObject(putObjectRequest);
    }



}
