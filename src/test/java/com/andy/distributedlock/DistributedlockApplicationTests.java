package com.andy.distributedlock;

import com.andy.distributedlock.service.redissetnx.Lock4RedisSetNxService;
import com.andy.distributedlock.service.redisson.RedissonLock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.concurrent.CountDownLatch;


@Slf4j
@SpringBootTest
class DistributedlockApplicationTests {

    @Autowired
    private Lock4RedisSetNxService lock4RedisSetNxService;

    @Autowired
    private RedissonLock redissonLock;



    @Test
    void testRedisSetNxLock() throws Exception{

        final int[] counter = {0};
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        for (int i=0;i<1000;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {

                    boolean lock  = lock4RedisSetNxService.lock("testRedisSetNxLock","testRedisSetNxLock",20000);

                    while (!lock){
                        lock  = lock4RedisSetNxService.lock("testRedisSetNxLock","testRedisSetNxLock",20000);
                    }
                    try{

                        int a = counter[0];
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        counter[0] = a + 1;
                    }finally {
                        countDownLatch.countDown();
                        lock4RedisSetNxService.unLock("testRedisSetNxLock");
                    }


                }
            }).start();
        }

        countDownLatch.await();
        Assertions.assertEquals(1000,counter[0]);
    }


    @Test
    void testRedissonLock() throws Exception{

        final int[] counter = {0};
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        for (int i=0;i<1000;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {

                    redissonLock.lock("testRedisSetNxLock",20000);


                    try{

                        int a = counter[0];
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        counter[0] = a + 1;
                    }finally {
                        countDownLatch.countDown();
                        redissonLock.unlock("testRedisSetNxLock");
                    }


                }
            }).start();
        }

        countDownLatch.await();
        Assertions.assertEquals(1000,counter[0]);
    }




}
