package com.kusi.cn.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorConnectUtils {
    private final static String  CONNECTSTR = "192.168.32.132:2181,192.168.32.133:2181,192.168.32.134:2181";
    private static CuratorFramework curatorFramework;

    public static CuratorFramework getInstance(){
        curatorFramework = CuratorFrameworkFactory.newClient(CONNECTSTR,5000,
                5000,new ExponentialBackoffRetry(1000,3));
        curatorFramework.start();
        return curatorFramework;
    }

}
