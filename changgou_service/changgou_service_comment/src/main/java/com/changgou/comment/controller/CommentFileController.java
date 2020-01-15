package com.changgou.comment.controller;

import com.changgou.comment.pojo.FastDFSFile;
import com.changgou.comment.util.FastDFSClient;
import com.changgou.comment.util.ThumnailsImageUtil;
import com.changgou.common.entity.Result;
import com.changgou.common.entity.StatusCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/*接受图片,发送到前端图片名称类*/
@RestController
@RequestMapping("/file")
public class CommentFileController {

    //使用post
    @PostMapping("/uploadFile")
    public List uploadFile(@RequestParam("filename") MultipartFile[] files){
        try {
            if (files!=null && files.length>0){
                List<String> urls=new ArrayList<>();
                //循环获取file数据获得文件
                for (int i = 0; i <files.length; i++) {
                    MultipartFile file=files[i];
                    //判断当前文件是否存在
                    if (file==null){
                        throw new RuntimeException("文件不存在!");
                    }
                    //获取当前文件的完整名称
                    String filename = file.getOriginalFilename();
                    if (StringUtils.isEmpty(filename)){
                        throw new RuntimeException("文件不存在!");
                    }
                    //获取文件的扩展名称 aaa.jpg  .jpg
                    String exName = filename.substring(filename.lastIndexOf(",") + 1);
                    //获取当前文件内容
                    byte[] _content = file.getBytes();

                    //图片压缩技术
                    byte[] content = ThumnailsImageUtil.scale(_content);

                    //创建文件上传的封装实体类
                    FastDFSFile fastDFSFile=new FastDFSFile(filename,content,exName);
                    //基于工具类进行文件上传,并接受返回参数 String[]
                    String[] uploadFile = FastDFSClient.upload(fastDFSFile);
                    //整体：group1/M00/02/44/wKgDrE34E8wAAAAAAAAGKEIYJK42378.sh
                    //[0]:group1
                    //[1]:M00/02/44/wKgDrE34E8wAAAAAAAAGKEIYJK42378.sh
                    //封装文件路径地址
                    String url=FastDFSClient.getTrackerUrl()+uploadFile[0]+"/"+uploadFile[1];
                    urls.add(url);
                }
                return urls;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
