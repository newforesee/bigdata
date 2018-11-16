package com.gp1809.clean.Answer_Collect;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class Answer_Collect {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://master:9000");
//      conf.set("mapreduce.framework.name", "local");

        Job job = Job.getInstance(conf, "AC_MapJoin");
        //指定程序的jar包运行的本地路径
        job.setJarByClass(Answer_Collect.class);
        //本业务需要执行的Map类
        job.setMapperClass(AC_MapJoin.class);

        //输出的Key
        job.setMapOutputKeyClass(NullWritable.class);
        //输出的value
        job.setMapOutputValueClass(Text.class);

        job.setNumReduceTasks(0);
        //文件来源
        FileInputFormat.setInputPaths(job
                , new Path("hdfs://master:9000/qlis/input/answer_paper"));

        //输出路径
        FileOutputFormat.setOutputPath(job
                , new Path("hdfs://master:9000/qlis/output/answer_collect"));

//        boolean b = job.waitForCompletion(true);

        System.exit(job.waitForCompletion(true)? 0 : 1);
    }
}
