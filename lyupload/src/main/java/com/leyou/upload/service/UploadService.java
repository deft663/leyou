package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.upload.controller.UploadController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhang
 * @date 2019年04月05日 21:12
 */
@Service
public class UploadService {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    // 支持的文件类型
    private static final List<String> suffixes = Arrays.asList("image/png", "image/jpeg");
    public String upload(MultipartFile file)  {
        String type = file.getContentType();
        if (!suffixes.contains(type)) {
            logger.info("上传失败，文件类型不匹配：{}", type);

            return null;
        }
        // 2)校验图片内容
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                logger.info("上传失败，文件内容不符合要求");
                return null;
            }
            String fileExtName = "";
            if (file.getOriginalFilename().contains(".")) {
                fileExtName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            } else {
                logger.warn("Fail to upload file, because the format of filename is illegal.");
            }

            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileExtName, null);
            System.out.println(storePath.toString());
            // 2、保存图片
            // 2.1、生成保存目录
            /*File dir = new File("D:\\img\\upload");
            if (!dir.exists()) {
                dir.mkdirs();
            }*/
            // 2.2、保存图片
            //file.transferTo(new File(dir, file.getOriginalFilename()));
            // 2.3、拼接图片地址
            String url = "http://image.leyou.com/" + storePath.getFullPath();
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
