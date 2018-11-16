package zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by newforesee on 2018/8/21.
 */
public class BasicOpt {


    /**
     * 链接方式1的问题
     * Zookeeper的构造方法是非阻塞式的,当执行到这个构造方法时,不等这个方法执行完毕
     * 就会继续执行.这时候很可能会造成还没有等连接成功,就会主线程执行完毕造成JVM关闭.
     *
     * @throws IOException
     */
    @Test
    public void testConnect() throws IOException {
        String connectString = "10.211.55.28:2181,10.211.55.29:2181,10.211.55.30:2181";
        int sessionTimeout = 5000;
        ZooKeeper zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("==========process============");
            }
        });
        System.out.println("++++++=====end======+++++++");
    }

    /**
     * 连接方法2:
     * 使用休眠的方式让主线程解决"方式1"的问题
     * 但是这个方式不够优雅
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testConnect2() throws IOException, InterruptedException {
        String connectString = "10.211.55.28:2181,10.211.55.29:2181,10.211.55.30:2181";
        int sessionTimeout = 5000;
        ZooKeeper zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("==========process============");
            }
        });
        Thread.sleep(10 * 1000);
        System.out.println("++++++=====end======+++++++");
    }


    /**
     * 连接方式3:
     * 使用curr包的一个类CountDownLatch,解决"方式2"的问题
     * 这个类的特点是:
     * 1.构造一个CountDownLatch对象时,指定一个数值如本例的new CountDownLatch(1)
     * 2.执行实例的await方法时,会阻塞所在线程
     * 3.当执行实例的countDown方法时,会使构造对象时指定的那个数值减一,一旦那个数值变为0,则
     * await继续往后执行,否则继续阻塞
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testConnect3() throws IOException, InterruptedException {
        final CountDownLatch cd1 = new CountDownLatch(1);
        String connectString = "10.211.55.28:2181,10.211.55.29:2181,10.211.55.30:2181";
        int sessionTimeout = 5000;
        ZooKeeper zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("==========process============");
                cd1.countDown();
            }
        });
        cd1.await();
        System.out.println("++++++=====end======+++++++");
    }

    /**
     * 创建持久化节点
     *
     * @throws Exception
     */
    @Test
    public void testCreateNode() throws Exception {
        final CountDownLatch cd1 = new CountDownLatch(1);
        String connectString = "10.211.55.28:2181,10.211.55.29:2181,10.211.55.30:2181";
        int sessionTimeout = 5000;
        ZooKeeper zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {

                    cd1.countDown();
                    System.out.println("==========process============" + watchedEvent.getState());
                }
            }
        });
        cd1.await();
        String resultVal = zkClient.create("/znode-from", "javaapi".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(resultVal);
        zkClient.close();
    }

    /**
     * 创建一个临时节点
     *
     * @throws Exception
     */
    @Test
    public void testCreateEphNode() throws Exception {
        final CountDownLatch cd1 = new CountDownLatch(1);
        String connectString = "10.211.55.28:2181,10.211.55.29:2181,10.211.55.30:2181";
        int sessionTimeout = 5000;
        ZooKeeper zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {

                    cd1.countDown();
                    System.out.println("==========process============" + watchedEvent.getState());
                }
            }
        });
        cd1.await();
        String resultVal = zkClient.create("/pnode_eph_from", "javaapi".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println(resultVal);
        Thread.sleep(20 * 1000);
        zkClient.close();
    }


    /**
     * 创建持久化序列化节点
     *
     * @throws Exception
     */
    @Test
    public void testCreateSequNode() throws Exception {
        final CountDownLatch cd1 = new CountDownLatch(1);
        String connectString = "10.211.55.28:2181,10.211.55.29:2181,10.211.55.30:2181";
        int sessionTimeout = 5000;
        ZooKeeper zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {

                    cd1.countDown();
                    System.out.println("==========process============" + watchedEvent.getState());
                }
            }
        });
        cd1.await();
        String resultVal = zkClient.create("/Server/aae", "javaapi".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT_SEQUENTIAL);
        System.out.println(resultVal);
        zkClient.close();
    }

    /**
     * 删除一个节点
     *
     * @throws Exception
     */
    @Test
    public void testDeleteNode() throws Exception {
        final CountDownLatch cd1 = new CountDownLatch(1);
        String connectString = "10.211.55.28:2181,10.211.55.29:2181,10.211.55.30:2181";
        int sessionTimeout = 5000;
        ZooKeeper zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {

                    cd1.countDown();
                    System.out.println("==========process============" + watchedEvent.getState());
                }
            }
        });
        cd1.await();
        zkClient.delete("/znode-from0000000005", -1);

        zkClient.close();
    }

    /**
     * 判断节点是否存在
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws KeeperException
     */
    @Test
    public void testExistNode() throws IOException, InterruptedException, KeeperException {
        final CountDownLatch cd1 = new CountDownLatch(1);
        String connectString = "10.211.55.28:2181,10.211.55.29:2181,10.211.55.30:2181";
        int sessionTimeout = 5000;
        ZooKeeper zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println(">>>>>>>>>>>>>>>>>process>>>>>>>>>>>>" + watchedEvent.getType());

                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {

                    cd1.countDown();
                    System.out.println("==========process============" + watchedEvent.getState());
                }

            }
        });
        cd1.await();
        Stat stat = zkClient.exists("/znode-from0000000004", true);
        System.out.println("++++++++++++++++++=" + stat);
        Thread.sleep(10 * 1000);
        zkClient.close();

    }

    /**
     * 获得一个节点的值
     *
     * @throws InterruptedException
     * @throws IOException
     * @throws KeeperException
     */
    @Test
    public void getNodeValue() throws InterruptedException, IOException, KeeperException {
        final CountDownLatch cd1 = new CountDownLatch(1);
        String connectString = "10.211.55.28:2181,10.211.55.29:2181,10.211.55.30:2181";
        int sessionTimeout = 5000;
        ZooKeeper zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {

                    cd1.countDown();
                    System.out.println("==========process============" + watchedEvent.getState());
                }

            }
        });
        cd1.await();
        byte[] data = zkClient.getData("/znode-from", false, null);
        System.out.println(new String(data));
        zkClient.close();

    }

    /**
     * 获取一个节点的子节点
     * @throws InterruptedException
     * @throws IOException
     * @throws KeeperException
     */
    @Test
    public void getChildNodes() throws InterruptedException, IOException, KeeperException {
        final CountDownLatch cd1 = new CountDownLatch(1);
        String connectString = "10.211.55.28:2181,10.211.55.29:2181,10.211.55.30:2181";
        int sessionTimeout = 5000;
        ZooKeeper zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {

                    cd1.countDown();
                    System.out.println("==========process============" + watchedEvent.getState());
                }

            }
        });
        cd1.await();
        List<String> children = zkClient.getChildren("/znode-from", false);
        for (String child : children) {
            System.out.println(child);
        }
        zkClient.close();

    }


}
