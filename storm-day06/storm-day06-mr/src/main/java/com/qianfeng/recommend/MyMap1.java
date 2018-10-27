package com.qianfeng.recommend;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 计算用户对物品的偏好度
 * uid,pid,value
 */
public class MyMap1 extends Mapper<LongWritable, Text, Text, Text> {
    /**
     * 浏览操作	1	liming	4	http://wwww.1000phone.com?pid=100214.html	100214
     浏览操作	1	liming	4	http://wwww.1000phone.com?pid=100211.html	100211
     收藏操作	2	liming	4	http://wwww.1000phone.com?pid=100211.html	100211
     收藏操作	2	guofucheng	3	http://wwww.1000phone.com?pid=100211.html	100211
     收藏操作	2	liming	4	http://wwww.1000phone.com?pid=100214.html	100214
     关注操作	4	liming	4	http://wwww.1000phone.com?pid=100211.html	100211
     加入购物车	6	guofucheng	3	http://wwww.1000phone.com?pid=100218.html	100218
     加入购物车	6	liudehua	1	http://wwww.1000phone.com?pid=100212.html	100212
     */
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] logs = value.toString().split("\t");
        String typeId = logs[1];//用户操作的编号
        String userId = logs[3];//用户编号
        String pId = logs[5];//商品编号
        context.write(new Text(userId + "," + typeId), new Text(pId));
    }
}
