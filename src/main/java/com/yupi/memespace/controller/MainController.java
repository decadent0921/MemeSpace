package com.yupi.memespace.controller;

import com.yupi.memespace.common.BaseResponse;
import com.yupi.memespace.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//健康检查类，用于项目初始化阶段，检查项目有无正常运行
@RestController
@RequestMapping("/")
public class MainController {
    @GetMapping("/health")
    public BaseResponse<String> health() {

        return ResultUtils.success("ok"
        );
    }
}
