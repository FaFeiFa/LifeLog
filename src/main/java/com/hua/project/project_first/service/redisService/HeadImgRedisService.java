package com.hua.project.project_first.service.redisService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class HeadImgRedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void PutHeadImg(String ImgStr , String ID){
        String key = ID + "_" + "HeadImg";
        stringRedisTemplate.opsForValue().set(key,ImgStr);
        stringRedisTemplate.expire(key,1, TimeUnit.DAYS);
        log.info("key:{}",key);
    }
    public String GetHeadImg(String ID){
        String key = ID + "_" + "HeadImg";
        return stringRedisTemplate.opsForValue().get(key);
    }
}
