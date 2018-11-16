package com.qfedu.bigdata.mr.day14;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * @ClassName multipleDemo
 * @Description TODO
 * @Author Chenfg
 * @Date 2018/8/30 0030 14:22
 * @Version 1.0
 */
public class multipleDemo {
    private static Logger logger = Logger.getLogger(multipleDemo.class);

    static class MyMapper extends Mapper<LongWritable,Text,Text,Text>{
        /**
         * 只在map方法运行前执行一次（仅执行一次）
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */

        static Text k = new Text();
        static Text v = new Text();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
//            //获取文件名称
//            InputSplit inputSplit = context.getInputSplit();
//            String fileName = ((FileSplit) inputSplit).getPath().getName();
//            k.set(fileName);
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
                String first = word.substring(0, 1);
                k.set(first);
                v.set(word);
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

    static class MyReducer extends Reducer<Text,Text,Text,Text>{
        /**
         * 只在reduce方法调用之前调用一次（仅调用一次）
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        static MultipleOutputs<Text,Text> mos = null;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            mos = new MultipleOutputs<Text,Text>(context);
        }

        /**
         * 每一组数据调用一次
         * @param key
         * @param values
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            String first = key.toString();
            String fileName = "";

            if(first.matches("[a-z]")){
                fileName ="az";
            }else if(first.matches("[A-Z]")){
                fileName ="AB";
            } else if (first.matches("[0-9]")){
                fileName ="09";
            }else{
                fileName ="others";
            }

            for (Text word : values) {
                mos.write(fileName,word,new Text(""));
            }
        }

        /**
         * 只在reduce方法执行之后执行一次（仅执行一次）
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            mos.close();
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
            job.setJarByClass(multipleDemo.class);

            //5、设置Job执行的Mapper任务的业务类
            job.setMapperClass(MyMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);

            //6、设置reduce端的相关参数
            job.setReducerClass(MyReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);

            //7、设置job的输入文件目录
            FileInputFormat.setInputPaths(job,new Path("F:\\Data\\GP1809\\wordcount\\multiFiles"));

            MultipleOutputs.addNamedOutput(job,"az", TextOutputFormat.class,Text.class,Text.class);
            MultipleOutputs.addNamedOutput(job,"AB", TextOutputFormat.class,Text.class,Text.class);
            MultipleOutputs.addNamedOutput(job,"09", TextOutputFormat.class,Text.class,Text.class);
            MultipleOutputs.addNamedOutput(job,"others", TextOutputFormat.class,Text.class,Text.class);

            //8、设置job的输出文件目录
            FileOutputFormat.setOutputPath(job,new Path("F:\\Data\\GP1809\\wordcount\\multiFilesout"));

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
