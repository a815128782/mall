package com.changgou.file.controller;


import com.changgou.common.entity.R;
import com.changgou.common.entity.Result;
import com.changgou.common.exception.ExceptionCast;
import com.changgou.common.model.response.file.FileCode;
import com.changgou.file.pojo.FastDFSFile;
import com.changgou.file.util.FastDFSClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URI;

/**
 * @author LiXiang
 */
@RequestMapping("/file")
@RestController
public class FileController {

    @PostMapping("/upload")
    public Result uploadFile(MultipartFile file) {
        try {
            //判断文件是否存在
            if (file == null) {
                ExceptionCast.cast(FileCode.FILE_UPLOAD_ERROR);
            }
            //获取文件的完整名称
            String originalFilename = file.getOriginalFilename();
            if (StringUtils.isEmpty(originalFilename)) {
                ExceptionCast.cast(FileCode.FILE_UPLOAD_ERROR);
            }
            String name = file.getName();
            String contentType = file.getContentType();

            //获取文件的扩展名称
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //获取文件内容
            byte[] content = file.getBytes();
            //创建文件上传的封装实体类
            FastDFSFile fastDFSFile = new FastDFSFile(originalFilename, content, extName);
            //基于工具类进行文件上传，并接收返回参数
            String[] uploadResult = FastDFSClient.upload(fastDFSFile);
            //封装返回结果
            String url = FastDFSClient.getTrackerUrl() + uploadResult[0] + "/" + uploadResult[1];
            return R.T("文件上传", url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.F("文件上传");
    }
}
