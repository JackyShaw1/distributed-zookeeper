package com.kusi.cn.curator;

import javafx.scene.layout.Background;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CurtorOpeatorDemo {
    /**
     * Curator 基于fluent风格，链式操作，可读性高，代码简洁
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        //建立会话连接
        CuratorFramework curatorFramework = CuratorConnectUtils.getInstance();
        System.out.println("创建会话成功...");
        //新增节点操作
     /*   try {
            String result = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/jacky04", "04".getBytes());
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //删除节点操作
     /*   try {
            curatorFramework.delete().deletingChildrenIfNeeded().forPath("/jack1");
            System.out.println("删除成功。。。");
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        //查询
        Stat stat = new Stat();
        try {
            byte[] bytes = curatorFramework.getData().storingStatIn(stat).forPath("/jacky04");
            System.out.println(new String(bytes) + "stat: " + stat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //更新
        try {
            Stat stat1 = curatorFramework.setData().forPath("/jacky04", "3333".getBytes());
            System.out.println("stat1 == >" + stat1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //异步操作
        //a.需要用到线程池来操作 ExecutorService ,此时创建节点的事件由线程池线程处理，而非当前线程
        //b.CountDownLatch来等待结果。CountDownLatch是通过一个计数器来实现的，计数器的初始值为线程的数量。每当一个线程完成了自己的任务后，计数器的值就会减1
       //c.withMode(CreateMode.EPHEMERAL)临时节点可以重复添加
        ExecutorService service = Executors.newFixedThreadPool(1);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
                @Override
                public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                    System.out.println("Thread.currentThread().getName()==>" + Thread.currentThread().getName() +
                            " curatorEvent.getResultCode()==>" + curatorEvent.getResultCode()
                            + " curatorEvent.getType()==>" + curatorEvent.getType());
                    countDownLatch.countDown();
                }
            },service).forPath("/jacky","123".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        countDownLatch.await();//主线程阻塞，等待线程池中线程完成创建节点后再把执行权交给主线程
        service.shutdown();


        //事物操作(curator独有)
        try {
            Collection<CuratorTransactionResult> results = curatorFramework.inTransaction().create().forPath("/jcaky11", "11".getBytes()).and().setData().
                    forPath("/jacky03", "12".getBytes()).and().commit();
            for (CuratorTransactionResult result : results){
                System.out.println(result.getForPath() + "=>" + result.getType() );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}
