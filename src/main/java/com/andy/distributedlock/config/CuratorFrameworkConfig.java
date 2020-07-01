package com.andy.distributedlock.config;

import io.reactivex.annotations.BackpressureSupport;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Andy on 2020/7/1 9:28
 */
@Configuration
public class CuratorFrameworkConfig {

    @Value("${zooClientIp}")
    private String zooClientIp;

    @Bean
    public CuratorFramework curatorFramework(){
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000,3);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(zooClientIp,retry);
        curatorFramework.getCuratorListenable().addListener(new MyCuratorListener());
        curatorFramework.start();
        return curatorFramework;
    }

    public class MyCuratorListener implements CuratorListener{

        @Override
        public void eventReceived(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
           CuratorEventType type = curatorEvent.getType();
           if(type == CuratorEventType.WATCHED){
               WatchedEvent watchedEvent = curatorEvent.getWatchedEvent();
               String path = watchedEvent.getPath();
               System.out.println(watchedEvent.getType()+" -- "+ path);
               // 重新设置改节点监听
               if(null != path){
                   curatorFramework.checkExists().watched().forPath(path);
               }

           }
        }
    }
}
