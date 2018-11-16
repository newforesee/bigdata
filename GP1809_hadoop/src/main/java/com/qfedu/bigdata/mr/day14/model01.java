package com.qfedu.bigdata.mr.day14;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * @ClassName model01
 * @Description TODO
 * @Author Chenfg
 * @Date 2018/8/30 0030 16:52
 * @Version 1.0
 */
public class model01 {
    private static Logger logger = Logger.getLogger(highTem.class);

    static class MyMapper extends Mapper<LongWritable,Text,Text,Text>{

        /**
         * 只在map方法调用之前调用一次（仅调用一次）
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            super.setup(context);
        }

        /**
         * 框架每读取一行数据，就调用一次map方法
         * @param key
         * @param value
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            super.map(key, value, context);
        }

        /**
         * 只在map方法调用结束之后调用一次（仅调用一次）
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            super.cleanup(context);
        }
    }

    static class MyReducer extends Reducer<Text,Text,Text,Text>{
        /**
         * 只在reduce方法调用之前调用一次（仅调用一次）
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            super.setup(context);
        }

        /**
         * 每一组数据（key相同的数据为一组）调用一次reduce方法
         * @param key
         * @param values
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            super.reduce(key, values, context);
        }

        /**
         * 只在reduce方法调用结束后调用一次（仅调用一次）
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            super.cleanup(context);
        }
    }

    public static void main(String[] args) {
        //1、获取配置对象Configuration
        Configuration conf = new Configuration();

        //2、设置hdfs的连接参数
        conf.set("fs.defaultFS","hdfs://gp1809");

        try {
            //3、创建一个Job对象
            Job job = Job.getInstance(conf, "model01");

            //4、设置job的执行路径
            job.setJarByClass(model01.class);

            //5、设置Job执行的Mapper任务的业务类
            job.setMapperClass(MyMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);

            //6、设置reduce端的相关参数
            job.setReducerClass(MyReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);

            //7、设置job的输入文件目录
            FileInputFormat.setInputPaths(job,new Path(args[0]));

            //8、设置job的输出文件目录
            FileOutputFormat.setOutputPath(job,new Path(args[1]));

            System.exit( job.waitForCompletion(true) ? 0:1);

        } catch (IOException e) {
            logger.error("创建job失败！！！",e);
        } catch (InterruptedException e) {
            logger.error("提交job失败！！！",e);
        } catch (ClassNotFoundException e) {
            logger.error("提交job失败！！！",e);
        }
    }
}
