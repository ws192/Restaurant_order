package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api(tags = "common")
public class CommonController {

    @Autowired
    AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file){

        String url="https://kenshin.hk/blog/jdnews/202212/nitori-sayaka/nitori-sayaka-00.jpg";

        return Result.success(url);
//        try {
//            String originalFilename = file.getOriginalFilename();
//            String substring = originalFilename.substring(originalFilename.lastIndexOf('.'));
//            String newFileName = UUID.randomUUID().toString()+substring;
//            String uploadURL = aliOssUtil.upload(file.getBytes(), newFileName);
//            return Result.success(uploadURL);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}
