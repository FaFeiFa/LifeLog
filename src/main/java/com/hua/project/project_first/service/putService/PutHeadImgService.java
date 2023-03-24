package com.hua.project.project_first.service.putService;

import com.alibaba.fastjson.JSON;
import com.hua.project.project_first.config.ConstantPropertiesConfig;
import com.hua.project.project_first.service.getMsgService.GetHeadStringService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

@Service
@Slf4j
public class PutHeadImgService {

    @Autowired
    GetHeadStringService getHeadStringService;
    @Autowired
    private ResourceLoader resourceLoader;

    public String upload(MultipartFile file, String ID) throws IOException {
        String endpoint = ConstantPropertiesConfig.END_POINT;
        String bucketName = ConstantPropertiesConfig.BUCKET_NAME;
        // 1 初始化用户身份信息（secretId, secretKey）。
        String secretId = ConstantPropertiesConfig.ACCESS_KEY_ID;
        String secretKey = ConstantPropertiesConfig.ACCESS_KEY_SECRET;
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的地域
        // clientConfig 中包含了设置 region, https(默认 http),超时, 代理等 set 方法
        Region region = new Region(ConstantPropertiesConfig.END_POINT);
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);

            /*File fileN = (File) file;
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            String key = ID;
            String headImgUrl = "headImg";
            key = headImgUrl + "/" + key + ".jpg" ;
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, fileN);
            putObjectRequest.setMetadata(objectMetadata);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            cosClient.shutdown();*/
        //PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        // 指定要上传的文件
        InputStream inputStream = file.getInputStream();
        // 指定文件将要存放的存储桶
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        //*String key = UUID.randomUUID().toString().replaceAll("-","")+
        //file.getOriginalFilename();
        //String dateUrl = new DateTime().toString("yyyy/MM/dd");

        String key = ID;
        String headImgUrl = "headImg";
        key = headImgUrl + "/" + key + ".jpg";
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        PutObjectRequest putObjectRequest =
                new PutObjectRequest(bucketName, key, inputStream, objectMetadata);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        //System.out.println(JSON.toJSONString(putObjectResult));
        cosClient.shutdown();
        String url = "https://" + bucketName + "." + "cos" + "." + endpoint + ".myqcloud.com" + "/" + key;

        /*将base64字符串转图片*/
        /*String HeadImg = getHeadStringService.GetImgByID(ID);
        byte[] imageBytes = Base64.getDecoder().decode(HeadImg);
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));

        // 获取资源目录的绝对路径
        String resourcePath = resourceLoader.getResource("").getURL().getPath();

        // 保存图像到资源目录下的images目录中
        File fileImg = new File(resourcePath + "/"+ID+".png");
        ImageIO.write(image, "png", fileImg);
        log.info("ID为{}的用户保存了头像,本地路径为{}",ID,fileImg.getPath());*/
        return url;
    }
}
