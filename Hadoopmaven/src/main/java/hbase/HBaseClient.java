package hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

/**
 * Created by newforesee on 2018/9/11.
 */
public class HBaseClient {
    public static void main(String[] args) throws IOException {
        //1.创建configuration对象
        Configuration conf = HBaseConfiguration.create();
        //2.设置链接参数
        conf.set("hbase.zookeeper.quorum","master:2181,slave1:2181,slave2:2181");
        //Configuration conf = new Configuration();
        //3.获取Admin管理员对象
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();
        //4.创建NamespaceDescriptor描述器
        NamespaceDescriptor nsd = NamespaceDescriptor.create("ns3").build();
        //5提交
        admin.createNamespace(nsd);
        admin.close();
        connection.close();
        System.out.println("+++++++++++");


    }

}
