package com.qfedu.bigdata.mr.day14;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;


/**
 * @ClassName MyPartitionerDemo
 * @Description TODO
 * @Author Chenfg
 * @Date 2018/8/30 0030 16:25
 * @Version 1.0
 *
 *
 * 自定义分区的注意事项：
 * 1、该类需要继承Partitioner类
 * 2、分区的数据类型必须和map端的输出数据类型一致
 * 3、getPartition方法只能返回int类型
 * 4、分区数量需要和reduce的数量相等
 * 5、分区返回值尽量用%（模于）去做，根据业务做
 * 6、默认使用的是hashPartitioner
 */
public class MyPartitionerDemo extends Partitioner<Text,IntWritable>{
    @Override
    public int getPartition(Text key, IntWritable value, int numPartitions) {
        //数据类型转换
        String word = key.toString();
        String firstChar = word.substring(0, 1);

        int part = 0;

        if(firstChar.matches("[a-z]")){
            part=1;
        } else if (firstChar.matches("[A-Z]")){
            part=2;
        } else if (firstChar.matches("[0-9]")){
            part=3;
        } else {
            part=4;
        }

        return part % numPartitions;
    }
}
