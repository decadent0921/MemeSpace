package com.yupi.memespace.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.memespace.model.dto.picture.PictureQueryRequest;
import com.yupi.memespace.model.dto.picture.PictureUploadRequest;
import com.yupi.memespace.model.entity.Picture;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.memespace.model.entity.User;
import com.yupi.memespace.model.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
* @author www
* @description 针对表【picture(图片)】的数据库操作Service
* @createDate 2025-08-07 19:34:07
*/
public interface PictureService extends IService<Picture> {

    // 上传图片
    PictureVO uploadPicture(MultipartFile multipartFile,
                            PictureUploadRequest pictureUploadRequest,
                            User loginUser);

    //获取查询对象
    QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest);

    //获取图片封装类（单条）
    PictureVO getPictureVO(Picture picture, HttpServletRequest request);

    //获取图片封装类（分页）
    Page<PictureVO> getPictureVOPage(Page<Picture> picturePage, HttpServletRequest request);

    //数据校验
    void validPicture(Picture picture);

}
