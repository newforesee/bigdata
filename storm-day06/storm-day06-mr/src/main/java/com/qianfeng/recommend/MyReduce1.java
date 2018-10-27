package com.qianfeng.recommend;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MyReduce1 extends Reducer<Text, Text, Text, NullWritable> {
    Map<String, Integer> scoreMap = new HashMap<String, Integer>();
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        //input userId+type,{10012,10012,10012,10013,10015,10017}
//<String, Double> 商品编号、喜好度
//        Map<String, Integer> scoreMap = new HashMap<String, Integer>();

        String[] keys = key.toString().split(",");
        String userId = keys[0];
        String typeId = keys[1];
        double weight = 0.1;
        //"1=浏览操作", "2=收藏操作", "3=点击操作", "4=关注操作", "5=评论操作", "6=加入购物车"
        switch (typeId) {
            case "1": //浏览了一个商品
                weight = 0.1;
                break;
            case "2"://收藏了一个商品
                weight = 0.2;
                break;
            case "3":
                weight = 0.1;//清理后的数据无此项，浏览等同于点击。
                break;
            case "4":
                weight = 0.2;//关注了商品
                break;
            case "5":
                weight = 0;//评论操作，清理后的数据无此项，需要单独进行文本语义分析。
                break;
            default:
                weight = 0.4;//加入购入车
        }
        //迭代所有商品
        for (Text text : values) {
            String pid = text.toString();
            Integer score = scoreMap.get(pid);
            if (score == null) {
                scoreMap.put(pid,10);
            } else {
                scoreMap.put(pid, 10 + score);
            }
        }

        for (Map.Entry<String, Integer> entry : scoreMap.entrySet()) {
            context.write(new Text(userId+","+entry.getKey()+","+entry.getValue()*weight), NullWritable.get());
        }
    }
}
