package com.yupi.memespace.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.memespace.model.dto.picture.*;
import com.yupi.memespace.model.entity.Picture;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.memespace.model.entity.User;
import com.yupi.memespace.model.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author www
* @description 针对表【picture(图片)】的数据库操作Service
* @createDate 2025-08-07 19:34:07
*/
public interface PictureService extends IService<Picture> {

    // 上传图片
    PictureVO uploadPicture(Object inputSource,
                            PictureUploadRequest pictureUploadRequest,
                            User loginUser);

    // 删除图片
    void deletePicture(long pictureId, User loginUser);

    //获取查询对象
    QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest);

    //获取图片封装类（单条）
    PictureVO getPictureVO(Picture picture, HttpServletRequest request);

    //获取图片封装类（分页）
    Page<PictureVO> getPictureVOPage(Page<Picture> picturePage, HttpServletRequest request);

    //数据校验
    void validPicture(Picture picture);

    //编辑图片
    void editPicture(PictureEditRequest pictureEditRequest, User loginUser);

    //图片审核
    void doPictureReview(PictureReviewRequest pictureReviewRequest, User loginUser);

    //填充审核参数，因为用很多次，所以封装一个方法
    void fillReviewParams(Picture picture, User loginUser);

    //批量抓取和创建图片
    Integer uploadPictureByBatch(
            PictureUploadByBatchRequest pictureUploadByBatchRequest,
            User loginUser
    );

    //删除图片(优化点：异步线程去删除)
    void clearPictureFile(Picture oldPicture);

    //空间权限校验
    void checkPictureAuth(User loginUser, Picture picture);

    //根据图片颜色在空间中搜索图片
    List<PictureVO> searchPictureByColor(Long spaceId, String picColor, User loginUser);

    //批量编辑图片（编辑分类和标签）
    void editPictureByBatch(PictureEditByBatchRequest pictureEditByBatchRequest, User loginUser);

}
