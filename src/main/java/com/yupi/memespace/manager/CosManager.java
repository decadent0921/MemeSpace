//manager包一般指可以复用的代码，不仅仅在这个项目中可以使用，还可以作用到其他地方
package com.yupi.memespace.manager;

import cn.hutool.core.io.FileUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.PicOperations;
import com.yupi.memespace.config.CosClientConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


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
     *  根据SDK开发文档，将Pic-Operations对象里面的rules和setIsPicInfo设置好等等操作实现上传时对图片进行压缩。
     * @param key  唯一键，自定义的COS存储路径（键）
     * @param file 文件
     */
    public PutObjectResult putPictureObject(String key, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key,
                file);
        // 对图片进行处理（获取基本信息也被视作为一种处理）
        PicOperations picOperations = new PicOperations();
        // 1 表示返回原图信息
        picOperations.setIsPicInfo(1);
        //图片处理规则列表，每个规则都是在原图的基础上操作
        List<PicOperations.Rule> rules = new ArrayList<>();
        // 图片压缩（转成 webp 格式）
        PicOperations.Rule compressRule = new PicOperations.Rule();
        String webpKey = FileUtil.mainName(key) + ".webp";
        compressRule.setFileId(webpKey);
        compressRule.setRule("imageMogr2/format/webp");
        compressRule.setBucket(cosClientConfig.getBucket());
        rules.add(compressRule);
        // 缩略图处理，同时仅对>20KB的图片，小图片经过压缩算法反而变大了，会插入一些数据。
        if (file.length() > 2 * 1024) {
            PicOperations.Rule thumbnailRule = new PicOperations.Rule();
            thumbnailRule.setBucket(cosClientConfig.getBucket());
            String thumbnailKey = FileUtil.mainName(key) + "_thumbnail." + FileUtil.getSuffix(key);
            thumbnailRule.setFileId(thumbnailKey);
            // 缩放规则 /thumbnail/<Width>x<Height>>（如果大于原图宽高，则不处理）
            thumbnailRule.setRule(String.format("imageMogr2/thumbnail/%sx%s>", 256, 256));
            rules.add(thumbnailRule);
        }

        // 构造处理参数
        picOperations.setRules(rules);
        putObjectRequest.setPicOperations(picOperations);
        return cosClient.putObject(putObjectRequest);
    }

    /**
     * 删除对象
     *
     * @param key 文件 key
     */
    public void deleteObject(String key) throws CosClientException {
        cosClient.deleteObject(cosClientConfig.getBucket(), key);
    }





}
