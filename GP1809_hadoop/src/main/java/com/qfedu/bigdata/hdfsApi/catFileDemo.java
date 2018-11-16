package com.qfedu.bigdata.hdfsApi;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;

/**
 * @ClassName catFileDemo
 * @Description TODO
 * @Author Chenfg
 * @Date 2018/8/27 0027 17:13
 * @Version 1.0
 */
public class catFileDemo {
    public static void main(String[] args) throws IOException {
        //1、构造一个Configuration对象
        Configuration conf = new Configuration();

        //2、设置hdfs的连接参数
        conf.set("fs.defaultFS","hdfs://gp1809");

        //高可用集群需要增加的配置项
//        conf.set("dfs.nameservices","gp1809");
//        conf.set("dfs.ha.namenodes.gp1809","nn1,nn2");
//        conf.set("dfs.namenode.rpc-address.gp1809.nn1","hadoop05:9000");
//        conf.set("dfs.namenode.rpc-address.gp1809.nn2","hadoop06:9000");
//        conf.set("dfs.client.failover.proxy.provider.gp1809","org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");

        //3、获取一个文件系统的客户端操作对象
        FileSystem fs = FileSystem.get(conf);

        //4、先打开文件
        FSDataInputStream open = fs.open(new Path("/stopZK.sh"));

        //5、打印内容到控制台
        IOUtils.copyBytes(open,System.out,4096,true);

        //6、关闭资源
        fs.close();

        //7、打印状态
        System.out.println("----高圆圆，我喜欢你！！！！！----------------------------------------------------------------");
    }
}
