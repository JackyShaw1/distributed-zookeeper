package com.kusi.cn.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.TimeUnit;

public class CuratorEventDemo {

    /**
     * 三种watcher来做节点监听 : 可以缓存子节点下所有数据
     *1,pathCache 子节点监听
     * 2,nodeCache 当前节点监听
     *3,treeCache :  pathCache + nodeCache
     */

    public static void main(String[] args) throws Exception {
        CuratorFramework curatorFramework = CuratorConnectUtils.getInstance();

        //nodeCache 当前节点监听
        /*NodeCache nodeCache = new NodeCache(curatorFramework,"/jacky04",false);
        nodeCache.getListenable().addListener(()-> System.out.println("变化后的节点数据为=》" +
                new String(nodeCache.getCurrentData().getData())));

        curatorFramework.setData().forPath("/jacky04","3332112".getBytes());
        System.in.read();*/

        //pathCache 子节点监听
        PathChildrenCache cache = new PathChildrenCache(curatorFramework,"/jacky",true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener((curatorFramework1,pathChildrenCacheEvent)->{
            switch (pathChildrenCacheEvent.getType()){
                case CHILD_ADDED:
                    System.out.println("子节点增加");
                    break;
                case CHILD_UPDATED:
                    System.out.println("子节点更新");
                    break;
                case CHILD_REMOVED:
                    System.out.println("子节点删除");
                    break;
                default:
                    break;
            }
        });
        curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath("/jacky","1".getBytes());
        TimeUnit.SECONDS.sleep(1);

        curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath("/jacky/jacky1-1","1-1".getBytes());
        TimeUnit.SECONDS.sleep(1);

        curatorFramework.setData().forPath("/jacky/jacky1-1","1-11".getBytes());
        TimeUnit.SECONDS.sleep(1);

        curatorFramework.delete().forPath("/jacky/jacky1-1");

        System.in.read();
    }
}
