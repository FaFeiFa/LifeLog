package com.hua.project.project_first.service.registerAndLoginService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;

@Service
@Slf4j
public class EncryptService {
    private static final String KEY_SHA = "SHA";
    public String getResult(String inputStr) {
        BigInteger password = null;
        byte[] inputData = inputStr.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(KEY_SHA);
            messageDigest.update(inputData);
            password = new BigInteger(messageDigest.digest());
            log.info("SHA加密成功!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password.toString(16);
    }
}
