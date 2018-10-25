package com.gp1809.clean.join2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URI;

/**
 * creat by newforesee 2018/10/25
 */
public class Join2Runner {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "join2");

        job.setJarByClass(Join2Runner.class);
        job.setMapperClass(Join2Mapper.class);


        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        job.setNumReduceTasks(0);

        //置job的输出数据的类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);


        job.addCacheFile(URI.create(args[0]));


        FileInputFormat.setInputPaths(job
                , new Path("hdfs://master:9000/qlis/input/answer_paper"));

        //输出路径
        FileOutputFormat.setOutputPath(job
                , new Path("hdfs://master:9000/qlis/tmp/jion2"));

        boolean b = job.waitForCompletion(true);

        System.exit(b ? 0 : 1);
    }
}
