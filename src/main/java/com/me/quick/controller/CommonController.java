package com.me.quick.controller;

import com.me.quick.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传下载
 * @author chen
 * @Date 2022-04-18-10:35
 */
@RestController
@RequestMapping("common")
@Slf4j
public class CommonController {

    @Value("${quick.path}")
    private String basePath;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("upload")
    public R<String> upload(MultipartFile file){
        //得到文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + suffix;

        //创建文件目录
        File dir = new File(basePath);
        //判断此目录是否在硬盘中存在
        if(!dir.exists()){
            dir.mkdir();
        }

        try {
            file.transferTo(new File(basePath+newFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(newFileName);
    }

    @GetMapping("download")
    public R<String> download(String name , HttpServletResponse response){
        try {
            //首先将文件从硬盘传到内存
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));


            //输出流，将内存文件协会浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");

            int len =0;
            byte[] bytes=new byte[1024];
            while((len=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
            }
            outputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
