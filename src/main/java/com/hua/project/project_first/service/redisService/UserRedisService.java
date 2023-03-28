package com.hua.project.project_first.service.redisService;

import com.hua.project.project_first.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserRedisService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    public String GetV(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }
    public void PutUser(User user){

    }

}
