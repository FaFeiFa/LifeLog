package com.hua.project.project_first.service.redisService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RegisterRedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void SendV(int i, String email) {
        String key = email + "verify";
        String value = String.valueOf(i);
        stringRedisTemplate.opsForValue().set(key, value);
        stringRedisTemplate.expire(key, 5, TimeUnit.MINUTES);
        log.info("key:{}", key);

    }

    public String GetV(String email) {
        return stringRedisTemplate.opsForValue().get(email);
    }
}
