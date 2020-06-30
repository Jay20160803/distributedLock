package com.andy.distributedlock.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Andy on 2020/6/30 17:58
 */
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private String redisPort;


    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();

        // 单实例模式
        config.useSingleServer()
                .setAddress(String.format("redis://%s:%s", redisHost, redisPort))

                // 同任何节点建立连接时的等待超时,时间单位是毫秒,默认：10000
                .setConnectTimeout(30000)
                // 等待节点回复命令的时间,该时间从命令发送成功时开始计时,默认:3000
                .setTimeout(10000)
                // 如果尝试达到 retryAttempts（命令失败重试次数） 仍然不能将命令发送至某个指定的节点时,将抛出错误.
                // 如果尝试在此限制之内发送成功,则开始启用 timeout（命令等待超时） 计时,默认值：3
                .setRetryAttempts(5);

        return Redisson.create(config);
    }
}
