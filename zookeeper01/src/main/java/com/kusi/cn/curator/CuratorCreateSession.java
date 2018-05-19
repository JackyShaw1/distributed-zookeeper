package com.kusi.cn.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorCreateSession {
    private final static String  CONNECTSTR = "192.168.32.132:2181,192.168.32.133:2181,192.168.32.134:2181";

    public static void main(String[] args) {
        //两种方式创建session会话
        //1，常规操作 normal
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(CONNECTSTR,5000,
                5000,new ExponentialBackoffRetry(1000,3));
        curatorFramework.start();

        //2，链式风格 fluent
        CuratorFramework curatorFramework1 = CuratorFrameworkFactory.builder().connectString(CONNECTSTR).sessionTimeoutMs(5000).
                retryPolicy(new ExponentialBackoffRetry(1000,3)).namespace("/jacky").build();
        curatorFramework1.start();
        System.out.println("success...");
    }
}
