package com.gp1809.clean.join1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


import java.io.IOException;

public class PQSTable {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://master:9000/");
//        conf.set("mapreduce.framework.name", "local");

        Job job = Job.getInstance(conf, "Objectvie_Anwser_TB");
        //指定程序的jar包运行的本地路径
        job.setJarByClass(PQSTable.class);
        //本业务需要执行的Map类
        job.setMapperClass(PQSMap.class);

        //输出的Key
        job.setMapOutputKeyClass(NullWritable.class);
        //输出的value
        job.setMapOutputValueClass(Text.class);

        job.setNumReduceTasks(0);
        //文件来源

        //置job的输出数据的类型
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);


        FileInputFormat.setInputPaths(job
                , new Path("hdfs://master:9000/qlis/input/answer_paper"));

        //输出路径
        FileOutputFormat.setOutputPath(job
                , new Path("hdfs://master:9000/qlis/tmp/join1"));

        boolean b = job.waitForCompletion(true);

        System.exit(b ? 0 : 1);
    }
}
