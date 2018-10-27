package com.gp1809.clean.join3;


import com.gp1809.clean.join2.Join2Mapper;
import com.gp1809.clean.join2.Join2Runner;
import com.gp1809.utils.JointUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * creat by newforesee 2018/10/25
 */
public class Join3Runner {
    class Join3Mapper extends Mapper<LongWritable, Text, Text, NullWritable> {
        public Map<String, String> examMap = new ConcurrentHashMap<>();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            //首先获取缓存文件路径
            Path[] paths = context.getLocalCacheFiles();
            JointUtil.colum2Map(paths,examMap,"\001",1,0,3);

        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {


        }

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "join3");

        job.setJarByClass(Join3Runner.class);
        job.setMapperClass(Join3Mapper.class);


        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        job.setNumReduceTasks(0);

        //置job的输出数据的类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);


        job.addCacheFile(URI.create(args[0]));


        FileInputFormat.setInputPaths(job
                , new Path("hdfs://master:9000/qlis/input/paper_template_part"));

        //输出路径
        FileOutputFormat.setOutputPath(job
                , new Path("hdfs://master:9000/qlis/tmp/jion2"));

        boolean b = job.waitForCompletion(true);

        System.exit(b ? 0 : 1);
    }
}
