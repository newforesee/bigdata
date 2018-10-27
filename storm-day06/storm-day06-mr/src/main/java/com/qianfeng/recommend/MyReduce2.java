package com.qianfeng.recommend;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MyReduce2 extends Reducer<Text, Text, Text, NullWritable> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        double sum = 0;
        for (Text text : values) {
            String value = text.toString();
            sum = sum + Double.parseDouble(value);
        }
        context.write(new Text(key.toString() + "," + sum),NullWritable.get());
    }
}
