package com.kusi.cn.javaApi;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ApiOperationDemo implements Watcher {
    private final static String  CONNECTSTR = "192.168.32.132:2181,192.168.32.133:2181,192.168.32.134:2181";
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static Stat stat = new Stat();
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
         zooKeeper =  new ZooKeeper(CONNECTSTR, 5000,new ApiOperationDemo());
        countDownLatch.await();
        System.out.println(zooKeeper.getState());

        String result = zooKeeper.create("/jacky04", "32332".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zooKeeper.getData("/jacky04",true,stat);
        System.out.println(result);
        TimeUnit.SECONDS.sleep(1);

        zooKeeper.setData("/jacky04","111".getBytes(),-1);
        TimeUnit.SECONDS.sleep(1);

        List<String> children = zooKeeper.getChildren("/jacky03", true);
        System.out.println(children);

    }


    public void process(WatchedEvent watchedEvent) {//监听事件
        //如果连接成功，则交给计数器控制
        System.out.println("watchedEvent.getType()=》" + watchedEvent.getType());
        System.out.println("watchedEvent.getPath()=》" + watchedEvent.getPath());
        if (watchedEvent.getState()== Watcher.Event.KeeperState.SyncConnected){
            if (Event.EventType.None == watchedEvent.getType() && watchedEvent.getPath() == null ){
                countDownLatch.countDown();
            }else if (Event.EventType.NodeDataChanged == watchedEvent.getType()){
                try {
                    System.out.println("节点变化事件，路径：" + watchedEvent.getPath() + "改变后的值为："
                            + zooKeeper.getData(watchedEvent.getPath(),true,stat));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if (Event.EventType.NodeChildrenChanged == watchedEvent.getType()){
                try {
                    System.out.println("子节点变化事件，路径：" + watchedEvent.getPath() + "改变后的值为："
                            + zooKeeper.getData(watchedEvent.getPath(),true,stat));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if (Event.EventType.NodeCreated == watchedEvent.getType()){
                try {
                    System.out.println("新增节点事件，路径：" + watchedEvent.getPath() + "改变后的值为："
                            + zooKeeper.getData(watchedEvent.getPath(),true,stat));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if (Event.EventType.NodeDeleted == watchedEvent.getType()){
                try {
                    System.out.println("删除节点事件，路径：" + watchedEvent.getPath() + "改变后的值为："
                            + zooKeeper.getData(watchedEvent.getPath(),true,stat));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }
    }


}
