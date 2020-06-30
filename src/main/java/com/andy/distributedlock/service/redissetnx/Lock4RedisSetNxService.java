package com.andy.distributedlock.service.redissetnx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 *
 * 保证不了原子性，容易死锁
 * Created by Andy on 2020/6/30 15:27
 */
@Service
public class Lock4RedisSetNxService {

    @Autowired
    private StringRedisTemplate redisTemplate;



    public boolean lock(String key,String value,long expiredTime) {
        return redisTemplate.opsForValue().setIfAbsent(key,value, Duration.ofMillis(expiredTime));
    }


    public void unLock(String key){
        redisTemplate.delete(key);
    }

}
