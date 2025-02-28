package com.example.app.controller;

import com.aliyun.oss.OSS;
import com.example.module.utils.Response;
import java.util.Date;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.UUID;


@RestController
public class FileUploadController {

    private static final String ALI_DOMAIN = "https://web0-store.oss-cn-beijing.aliyuncs.com/";

    @RequestMapping("/upload")
    public Response uploadFiles(@RequestParam("file") MultipartFile file) {

        try {
            // 格式化年月和日
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
            String year = yearFormat.format(new Date());
            String month = monthFormat.format(new Date());
            String day = dayFormat.format(new Date());
            // 读取图像以获取宽度和高度
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
            int width = image.getWidth();
            int height = image.getHeight();
            // 生成唯一的图片名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID().toString() + "_" + width + "x" + height + fileExtension;
            // 构建文件路径
            String imagePath = String.format("/image/%s%s/%s/%s", year, month, day, uniqueFileName);
            // 上传文件到OSS
            String endpoint = "https://oss-cn-beijing.aliyuncs.com/";
            // 替换为环境变量或配置文件中的占位符
            String accessKeyId = System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID");
            String accessKeySecret = System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET");
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            ossClient.putObject("web0-store", imagePath.substring(1), new ByteArrayInputStream(file.getBytes()));
            ossClient.shutdown();
            return new Response(1001, ALI_DOMAIN + imagePath);
        } catch (IOException e) {
            e.printStackTrace();
            return new Response(3001, null);
        }
    }
}