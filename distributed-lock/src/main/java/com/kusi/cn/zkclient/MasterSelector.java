package com.kusi.cn.zkclient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

public class MasterSelector {
    private ZkClient zkClient;
    private static String MASTER_PATH =  "/master";
    private IZkDataListener dataListener;//注册节点是否变化
    private UserCenter server;//其它从服务器
    private UserCenter master;//主服务器
    private static boolean isRuning = false;

    public MasterSelector(UserCenter server) {
        this.server = server;
        this.dataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {

            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                //如果节点被删除，发起一个选主操作


            }
        };

    }
    //具体master选举实现
    private void choiceMaster(){
        if (!isRuning){
            System.out.println("当前服务没有启动。。。");
        }


    }

    public void start(){
        //开始选举
    }

    public void stop(){
        //停止

    }

    private void releaseMaster(){
        //释放锁
    }

    private boolean isMaster(){
        //判断是否为master
        return false;
    }


}
