package com.qfedu.bigdata.mr.day14;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
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
 * @ClassName multipleDemo
 * @Description TODO
 * @Author Chenfg
 * @Date 2018/8/30 0030 14:22
 * @Version 1.0
 */
public class partitionerDemo {
    private static Logger logger = Logger.getLogger(partitionerDemo.class);

    static class MyMapper extends Mapper<LongWritable,Text,Text,IntWritable>{
        /**
         * 只在map方法运行前执行一次（仅执行一次）
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */

        static Text k = new Text();
        static IntWritable v = new IntWritable(1);
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            //
        }

        /**
         * 每读一行数据就调用一次map方法
         * @param key
         * @param value
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //1、类型转换
            String line = value.toString();

            //2、以空格切分数据
            String[] words = line.split(" ");

            for (String word : words) {
                k.set(word);
                context.write(k,v);
            }
        }

        /**
         * 在map方法调用结束后调用一次（仅调用一次）
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            super.cleanup(context);
        }
    }

    static class MyReducer extends Reducer<Text,IntWritable,Text,IntWritable>{
        /**
         * 只在reduce方法调用之前调用一次（仅调用一次）
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            //
        }

        /**
         * 每一组数据调用一次
         * @param key
         * @param values
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        private static IntWritable v = new IntWritable();

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int count=0;

            for (IntWritable value : values) {
                count+=value.get();
            }

            v.set(count);
            context.write(key,v);
        }

        /**
         * 只在reduce方法执行之后执行一次（仅执行一次）
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            //
        }
    }

    public static void main(String[] args) {
        //1、获取配置对象Configuration
        Configuration conf = new Configuration();

        //2、设置hdfs的连接参数
//        conf.set("fs.defaultFS","hdfs://gp1809");

        conf.set("fs.defaultFS","file:///");
        conf.set("mapreduce.framework.name","local");

        try {
            //3、创建一个Job对象
            Job job = Job.getInstance(conf, "multipleDemo");

            //4、设置job的执行路径
            job.setJarByClass(partitionerDemo.class);

            //5、设置Job执行的Mapper任务的业务类
            job.setMapperClass(MyMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(IntWritable.class);

            //6、设置reduce端的相关参数
            job.setReducerClass(MyReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);

            //设置自定义分区使用的类
            job.setPartitionerClass(MyPartitionerDemo.class);
            job.setNumReduceTasks(Integer.parseInt(args[0]));

            //7、设置job的输入文件目录
            FileInputFormat.setInputPaths(job,new Path("F:\\Data\\GP1809\\wordcount\\multiFiles"));

            //8、设置job的输出文件目录
            FileOutputFormat.setOutputPath(job,new Path("F:\\Data\\GP1809\\wordcount\\multiFilesout1"));

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
