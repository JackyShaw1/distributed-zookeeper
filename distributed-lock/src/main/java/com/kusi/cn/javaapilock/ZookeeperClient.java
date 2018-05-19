package com.kusi.cn.javaapilock;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZookeeperClient {

    private final static String  CONNECTSTR = "192.168.32.132:2181,192.168.32.133:2181,192.168.32.134:2181";
    private static ZooKeeper zooKeeper;
    private static int SESSION_TIME_OUT=5000;

    public static ZooKeeper getInstance() throws IOException, InterruptedException {
        final CountDownLatch connectStatus =  new CountDownLatch(1);
        zooKeeper =  new ZooKeeper(CONNECTSTR, SESSION_TIME_OUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if(watchedEvent.getState() == Event.KeeperState.SyncConnected){
                    connectStatus.countDown();
                }
            }
        });
        connectStatus.await();
        return zooKeeper;
    }

    public static int getSessionTimeOut() {
        return SESSION_TIME_OUT;
    }
}
