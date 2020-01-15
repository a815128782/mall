package com.changgou.comment.util;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ThumnailsImageUtil {
    //该方法是利用Thumnails这个类把图片的字节按照比列压缩,不破坏图片质量,返回的还是图片字节
    //得先添加thumbnailator的坐标才能使用
    public static byte[] scale(byte [] imageByte){
        byte[] byteArray=null;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageByte);
        //0.3f图片质量压缩比例，0.1~1之间，越小图片质量越差,这个可以改,也可以放在参数位置,让调用者传
        Thumbnails.Builder<? extends InputStream> builder = Thumbnails.of(inputStream).scale(0.2f);
        try {
            BufferedImage bufferedImage = builder.asBufferedImage();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage,"png",baos);
            byteArray = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
            return byteArray;
    }
}
