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
 * @ClassName highTem
 * @Description TODO
 * @Author Chenfg
 * @Date 2018/8/30 0030 11:17
 * @Version 1.0
 */
public class highTem {
    private static Logger logger = Logger.getLogger(highTem.class);

    static class MyMapper extends Mapper<LongWritable,Text,Text,DoubleWritable>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //1、数据类型转换
            String line = value.toString();

            //2、截断字符串，获取月份以及温度
            String month = line.substring(0, 6);
            Double tem = Double.parseDouble(line.substring(8));

            //3、往下发送数据
            context.write(new Text(month),new DoubleWritable(tem));
        }
    }

    static class MyReducer extends Reducer<Text,DoubleWritable,Text,DoubleWritable>{
        @Override
        protected void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
            //1、定义变量记录温度的最大值
            Double max = Double.MIN_VALUE;

            //2、循环遍历values
            for (DoubleWritable tem : values) {
                if (max < tem.get()){
                    max = tem.get();
                }
            }

            //3、往下发送数据
            context.write(key,new DoubleWritable(max));
        }
    }

    public static void main(String[] args) {
        //1、获取配置对象Configuration
        Configuration conf = new Configuration();

        //2、设置hdfs的连接参数
        conf.set("fs.defaultFS","hdfs://gp1809");

        try {
            //3、创建一个Job对象
            Job job = Job.getInstance(conf, "highTem");

            //4、设置job的执行路径
            job.setJarByClass(highTem.class);

            //5、设置Job执行的Mapper任务的业务类
            job.setMapperClass(MyMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(DoubleWritable.class);

            //6、设置reduce端的相关参数
            job.setReducerClass(MyReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(DoubleWritable.class);

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
