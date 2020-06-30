package com.andy.distributedlock;

import com.andy.distributedlock.service.redissetnx.Lock4RedisSetNxService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

@Slf4j
@SpringBootTest
class DistributedlockApplicationTests {

    @Autowired
    private Lock4RedisSetNxService lock4RedisSetNxService;

    int[] counter = {0};

    @Test
    void testRedisSetNxLock() throws Exception{

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
        System.out.println(counter[0]);
        log.info(counter[0] + "");
    }




}
