package com.kusi.cn.javaapilock;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DistributedLock {
    private static final String ROOT_LOCKS = "/LOCKS";
    private ZooKeeper zooKeeper;
    private String lockId; //记录锁节点id
    private int SESSION_TIME_OUT;
    private final static byte[] data = {1,2};
    private CountDownLatch countDownLatch= new CountDownLatch(1);

    public  DistributedLock() throws IOException, InterruptedException {
        this.zooKeeper = ZookeeperClient.getInstance();
        this.SESSION_TIME_OUT = ZookeeperClient.getSessionTimeOut();
    }

    public boolean lock() {
        try {
            //创建节点 LOCKS/00001
            lockId = zooKeeper.create(ROOT_LOCKS + "/", data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println("当前线程-->" + Thread.currentThread().getName() +
                    "成功创建了锁节点lockId--> "+lockId + "，开始去竞争锁");
            //获取子节点
            List<String> children = zooKeeper.getChildren(ROOT_LOCKS, true);
            //从小到大排序
            SortedSet<String > sortedSet =  new TreeSet<String>();
            for (String child : children){
                sortedSet.add(ROOT_LOCKS+"/"+child);
            }
            String first = sortedSet.first();
            if(lockId.equals(first)){
                System.out.println("当前线程：" + Thread.currentThread().getName() + " 获取到锁 : " + lockId);
                return true;
            }
            SortedSet<String> lessThanLockId = sortedSet.headSet(first);
            if (!lessThanLockId.isEmpty()){
                String prevLockId = lessThanLockId.last();//拿到比当前节点更小的节点
                zooKeeper.exists(prevLockId,new LockWatcher(countDownLatch));
                countDownLatch.await(SESSION_TIME_OUT, TimeUnit.MILLISECONDS);
                //上面代表着会话超时或者节点被删除
                System.out.println("当前线程：" + Thread.currentThread().getName() + "获取到锁：" + lockId);
            }


        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean unlock(){
        System.out.println("当前线程：" + Thread.currentThread().getName() + "，开始释放锁: " + lockId);
        try {
            zooKeeper.delete(lockId,-1);
            System.out.println("成功删除节点： " + lockId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        final CountDownLatch countDownLatch = new CountDownLatch(10);
        Random random =  new Random();

        for (int i=0; i<10 ;i++){
            new Thread(()->{
                DistributedLock lock = null;
                try {
                    lock = new DistributedLock();
                    countDownLatch.countDown();
                    countDownLatch.await();
                    lock.lock();
                    Thread.sleep(random.nextInt(500));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    if (lock!=null){
                        lock.unlock();
                    }
                }

            }).start();
        }
    }
}
