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
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * @ClassName subjectAvg
 * @Description TODO
 * @Author Chenfg
 * @Date 2018/8/30 0030 14:22
 * @Version 1.0
 */
public class subjectAvg {
    private static Logger logger = Logger.getLogger(highTem.class);

    static class MyMapper extends Mapper<LongWritable,Text,Text,DoubleWritable>{
        /**
         * 只在map方法运行前执行一次（仅执行一次）
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */

        static Text k = new Text();
        static DoubleWritable v = new DoubleWritable();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            //获取文件名称
            InputSplit inputSplit = context.getInputSplit();
            String fileName = ((FileSplit) inputSplit).getPath().getName();
            k.set(fileName);
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
            String[] scores = line.split(" ");

            //3、获得成绩
            Double score = Double.parseDouble(scores[1]);

            //4、往下输出数据
            v.set(score);
            context.write(k,v);
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

    static class MyReducer extends Reducer<Text,DoubleWritable,Text,DoubleWritable>{
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
         * 每一组数据调用一次
         * @param key
         * @param values
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        static DoubleWritable v = new DoubleWritable();

        @Override
        protected void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
            double count=0;
            int sum=0;

            for (DoubleWritable score :values) {
                count+=score.get();
                sum+=1;
            }

            double avg = count / sum ;

            v.set(avg);
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
            super.cleanup(context);
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
            Job job = Job.getInstance(conf, "subjectAvg");

            //4、设置job的执行路径
            job.setJarByClass(subjectAvg.class);

            //5、设置Job执行的Mapper任务的业务类
            job.setMapperClass(MyMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(DoubleWritable.class);

            //6、设置reduce端的相关参数
            job.setReducerClass(MyReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(DoubleWritable.class);

            //7、设置job的输入文件目录
            FileInputFormat.setInputPaths(job,new Path("F:\\Data\\GP1809\\wordcount\\subjectScore"));

            //8、设置job的输出文件目录
            FileOutputFormat.setOutputPath(job,new Path("F:\\Data\\GP1809\\wordcount\\subjectScoreout"));

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
