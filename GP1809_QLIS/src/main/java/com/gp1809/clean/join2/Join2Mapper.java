package com.gp1809.clean.join2;


import com.gp1809.utils.JointUtil;
import com.gp1809.utils.MD5Util;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * creat by newforesee 2018/10/23
 */
public class Join2Mapper extends Mapper<LongWritable, Text, Text, NullWritable> {
    //定义两个内存数据结构来存储缓存文件中的内容
    private Map<String, String> examMap = new ConcurrentHashMap<>();


    @Override
    protected void setup(Context context) throws IOException, InterruptedException {

        //首先获取缓存文件路径
        Path[] paths = context.getLocalCacheFiles();
        JointUtil.colum2Map(paths, examMap, "\001",0, 1);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String[] strings = value.toString().split("\\001");
        Text text = JointUtil.joint("\001",strings[1], strings[8], strings[6], strings[3], MD5Util.MD5(strings[4]), examMap.get(strings[1]));
        context.write(text, NullWritable.get());
    }
}
