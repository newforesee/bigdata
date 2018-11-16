package hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by newforesee on 2018/9/11.
 */
public class Namespace {

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
    public void listNamespace() throws IOException {
        NamespaceDescriptor[] nss = admin.listNamespaceDescriptors();
        for (NamespaceDescriptor namespaceDescriptor : nss) {
            System.out.println(namespaceDescriptor.getName());
        }

    }

    @Test
    public void listTables() throws IOException {
        HTableDescriptor[] hTableDescriptors = admin.listTables();
        for (HTableDescriptor hTableDescriptor : hTableDescriptors) {
            System.out.println(hTableDescriptor.getNameAsString());
        }
    }
    @Test
    public void listNamespaceTables() throws IOException {
        HTableDescriptor[] ns1s = admin.listTableDescriptorsByNamespace("ns1");
        for (HTableDescriptor ns1 : ns1s) {
            System.out.println(ns1.getNameAsString());
        }
    }

    @Test
    public void deleteNamesoace() throws IOException {
        admin.deleteNamespace("ns2");
    }


}
