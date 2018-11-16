package zk;

import org.apache.zookeeper.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by newforesee on 2018/8/21.
 */
public class Waiter {

    private ZooKeeper zkClient;
    public static final String WAITERROOT = "/Server";
    private ArrayList<String> folloers = new ArrayList<String>();//保存在线子节点

    /**
     * 链接zk服务器
     */
    public void connectZkServer() throws Exception {
        String connectString = "10.211.55.28:2181,10.211.55.29:2181,10.211.55.30:2181";
        int sessionTimeout = 5000;
        final CountDownLatch cd = new CountDownLatch(1);
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    cd.countDown();
                }


                if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                    getFolloer();
                }
            }
        });
        cd.await();
        createRootNode();

    }

    /**
     * 创建一个名为Server的永久节点
     */
    public void createRootNode() {
        try {
            zkClient.create(
                    WAITERROOT,
                    "Server_data".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获得子节点
     */
    public void getFolloer() {
        folloers.clear();
        try {
            List<String> children = zkClient.getChildren(WAITERROOT, true);
//            folloers.addAll(children);
            for (String child : children) {
                byte[] followerName_b = zkClient.getData(WAITERROOT+"/"+child,null,null);
                String followerName = new String(followerName_b);
                folloers.add(followerName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 实时打印当前在线机器名
     */
    public void showFollowers() {
        while (true) {
            StringBuffer sb = new StringBuffer();
            for (String folloer : folloers) {
                sb.append(folloer).append("+++>>>>");
            }
            System.out.println(sb.toString());
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws Exception {

        Waiter waiter = new Waiter();
        waiter.connectZkServer();
        waiter.getFolloer();
        waiter.showFollowers();

    }
}
