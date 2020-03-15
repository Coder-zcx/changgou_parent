package com.changgou.file.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.file.util.FastDFSClient;
import com.changgou.file.util.FastDFSFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author: Coder-zcx
 * @Date: 2019/11/20 16:44
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @RequestMapping("/upload")
    public Result upload(MultipartFile file) {
        if (file == null) {
            throw new RuntimeException("上传文件不存在");
        }
        try {
            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            byte[] content = file.getBytes();
            FastDFSFile fastDFSFile = new FastDFSFile(originalFilename, content, ext);
            String[] upload = FastDFSClient.upload(fastDFSFile);
            String trackerUrl = FastDFSClient.getTrackerUrl();
            String url = trackerUrl + upload[0] + "/" + upload[1];
            return new Result(true, StatusCode.OK, "上传成功", url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false, StatusCode.ERROR, "上传失败");
    }
}
