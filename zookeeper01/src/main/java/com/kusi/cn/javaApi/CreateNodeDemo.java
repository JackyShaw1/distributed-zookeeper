package com.kusi.cn.javaApi;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class CreateNodeDemo {
    private final static String  CONNECTSTR = "192.168.32.132:2181,192.168.32.133:2181,192.168.32.134:2181";
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper =  new ZooKeeper(CONNECTSTR, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                //如果连接成功，则交给计数器控制
                if (watchedEvent.getState()==Event.KeeperState.SyncConnected){
                    countDownLatch.countDown();
                    System.out.println(watchedEvent.getState());
                }
            }
        });
        countDownLatch.await();
        System.out.println(zooKeeper.getState());

        String result = zooKeeper.create("/jacky02", "32332".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("创建节点成功：" + result);
    }
}
