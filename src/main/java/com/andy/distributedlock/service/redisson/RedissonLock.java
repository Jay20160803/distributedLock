package com.andy.distributedlock.service.redisson;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 *
 *还有很多可以实现
 * Created by Andy on 2020/6/30 16:03
 */
@Service
public class RedissonLock {

    @Autowired
    private RedissonClient redissonClient;


    public void lock(String lockKey,long expireTime){
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(expireTime, TimeUnit.MILLISECONDS);
    }

    public void unlock(String lockKey){
        RLock lock =  redissonClient.getLock(lockKey);
        lock.unlock();
    }




}
