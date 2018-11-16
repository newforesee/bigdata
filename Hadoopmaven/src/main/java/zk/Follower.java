package zk;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * Created by newforesee on 2018/8/21.
 */

public class Follower {
    private ZooKeeper zkClient;
    private String fname;

    public Follower(String fname) {
        this.fname = fname;
    }

    /**
     * 链接zk服务器
     */
    public void connectZkServer() throws Exception {
        String connectString = "10.211.55.28:2181,10.211.55.29:2181,10.211.55.30:2181";
        int sessionTimeout = 5000;
        final CountDownLatch cd = new CountDownLatch(1);
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    cd.countDown();
                }
            }
        });
        cd.await();
        upLine();
    }

    /**
     * 上线
     */
    public  void upLine(){
        try {
            zkClient.create(Waiter.WAITERROOT+"/folloer", fname.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) throws Exception {
        Follower follower = new Follower(args[0]);
        follower.connectZkServer();
        Thread.sleep(60*1000);

    }
}
