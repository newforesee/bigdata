package com.qianfeng.recommend;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 计算用户对物品的偏好度
 * uid,pid,value
 */
public class MyMap2 extends Mapper<LongWritable, Text, Text, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] params = line.split(",");
        context.write(new Text(params[0]+","+params[1]), new Text(params[2]));
    }
}
