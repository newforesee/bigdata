package hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by newforesee on 2018/9/11.
 */
public class Table {
    private static Admin admin;
    private static Connection connection;

    @Before
    public void init() throws IOException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "master:2181,slave1:2181,slave2:2181");
        connection = ConnectionFactory.createConnection(conf);
        admin = connection.getAdmin();
    }

    @After
    public void mycloser() throws IOException {
        admin.close();
        connection.close();
    }

    @Test
    public void createTable() throws IOException {
        String tablename = "ns1:t_user_info";
        String columnName = "info";
        HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(tablename));
        //创建列簇描述器
        HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(columnName);
        hColumnDescriptor.setVersions(1,3);
        hTableDescriptor.addFamily(hColumnDescriptor);
        admin.createTable(hTableDescriptor);
        System.out.println("OK");
    }

    @Test
    public void getData(){

        Get get = new Get(Bytes.toBytes("rk00001"));


    }

    
    

}
