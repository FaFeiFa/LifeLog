package com.hua.project.project_first.service.getMsgService;

import com.hua.project.project_first.config.ConstantPropertiesConfig;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

@Service
public class GetHeadStringService {
    /**
     * 字符串生成图像
     * Base64.Decoder decoder = Base64.getDecoder();
     * byte[] bytes = decoder.decode(String);
     * outputStream = new FileOutputStream("e://temp.jpg");
     * outputStream.write(bytes);
     * outputStream.close();
     * @param file 图像文件
     * @return base64技术生成的字符串
     * @throws IOException
     */
    public String ImgToStringBase64(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);;
        Base64.Encoder encoder = Base64.getEncoder();

        int available = inputStream.available();
        byte[] bytes = new byte[available];
        inputStream.read(bytes);

        String base64Str = encoder.encodeToString(bytes);
        inputStream.close();
        return base64Str;
    }

    String endpoint = ConstantPropertiesConfig.END_POINT;
    String bucketName = ConstantPropertiesConfig.BUCKET_NAME;
    public String GetImgByEmail(String EMAIL) throws IOException {

        String headImgUrl = "headImg";
        String key = headImgUrl + "/" + EMAIL + ".jpg";
        String url = "https://"+bucketName+"."+"cos"+"."+endpoint+".myqcloud.com"+"/"+key;
        URL img = new URL(url);
        HttpURLConnection httpConn = (HttpURLConnection) img.openConnection();
        httpConn.setRequestMethod("GET");
        int responseCode = httpConn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = httpConn.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = outputStream.toByteArray();
            Base64.Encoder encoder = Base64.getEncoder();
            String base64Str = encoder.encodeToString(imageBytes);
            /*ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // 将图片写入字节数组输出流
            int bytesRead = -1;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            // 将字节数组输出流转换为字符串
            String imageString = outputStream.toString();*/


            return base64Str;
        }
        return null;
    }
}






















