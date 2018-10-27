package com.qianfeng.recommend;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class MyJob {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();

        Job job = new Job(conf, "preference");
        job.setJarByClass(MyJob.class);

        job.setMapperClass(MyMap1.class);
        job.setReducerClass(MyReduce1.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path("F:\\BigData\\data.txt"));
        FileOutputFormat.setOutputPath(job, new Path("F:\\BigData\\r"));

        if (job.waitForCompletion(true)){
            Job job1 = new Job(conf, "interest");

            job1.setMapperClass(MyMap2.class);
            job1.setReducerClass(MyReduce2.class);

            job1.setOutputKeyClass(Text.class);
            job1.setOutputValueClass(Text.class);
            FileInputFormat.addInputPath(job1, new Path("F:\\BigData\\r\\part-r-00000"));
            FileOutputFormat.setOutputPath(job1, new Path("F:\\BigData\\r1"));
            System.exit((job1.waitForCompletion(true)) ? 0 : 1);
        }
    }
}
