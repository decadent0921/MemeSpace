package com.yupi.memespace.api.imageserch;

import com.yupi.memespace.api.imageserch.model.ImageSearchResult;
import com.yupi.memespace.api.imageserch.sub.GetImageFirstUrlApi;
import com.yupi.memespace.api.imageserch.sub.GetImageListApi;
import com.yupi.memespace.api.imageserch.sub.GetImagePageUrlApi;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

//门面模式
@Slf4j
public class ImageSearchApiFacade {

    /**
     * 搜索图片
     *
     * @param imageUrl
     * @return
     */
    public static List<ImageSearchResult> searchImage(String imageUrl) {
        String imagePageUrl = GetImagePageUrlApi.getImagePageUrl(imageUrl);
        String imageFirstUrl = GetImageFirstUrlApi.getImageFirstUrl(imagePageUrl);
        List<ImageSearchResult> imageList = GetImageListApi.getImageList(imageFirstUrl);
        return imageList;
    }

    public static void main(String[] args) {
        // 测试以图搜图功能
        String imageUrl = "http://mms1.baidu.com/it/u=3973288703,2479590243&fm=253&app=138&f=JPG";
        List<ImageSearchResult> resultList = searchImage(imageUrl);
        System.out.println("结果列表" + resultList);
    }
}
