package com.kusi.cn.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

public class CuratorMasterSelector {

    private final static String  CONNECTSTR = "192.168.32.132:2181,192.168.32.133:2181,192.168.32.134:2181";
    private static String MASTER_PATH = "/curator_master_path";

    public static void main(String[] args) {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString(CONNECTSTR)
                .retryPolicy(new ExponentialBackoffRetry(5000,3)).build();

        LeaderSelector leaderSelector =  new LeaderSelector(curatorFramework, MASTER_PATH, new LeaderSelectorListener() {
            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                System.out.println("获得leader成功。。。");
                TimeUnit.SECONDS.sleep(2);

            }

            @Override
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {

            }
        });
        leaderSelector.autoRequeue();
        leaderSelector.start();


    }

}

