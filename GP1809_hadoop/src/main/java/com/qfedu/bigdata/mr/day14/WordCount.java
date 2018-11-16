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

import java.io.IOException;

/**
 * @ClassName WordCount
 * @Description TODO
 * @Author Chenfg
 * @Date 2018/8/28 0028 15:59
 * @Version 1.0
 *
 *
 * Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
 *     框架在调用咱们写的map()方法时，会将数据作为参数（一个key，一个value）传递给map方法，
 *     KEYIN：是框架（maptask）要传递给我们写的map()方法的传入参数中的key的数据类型
 *     VALUEIN：是框架(maptask)要传递给我们写的map()放大的传入参数中value的数据类型
 *
 *      在默认情况下
 *      框架传入的key是框架从待处理的数据中读取到的某一行数据的起始偏移量，所以key的类型是Long
 *      框架传入的value是框架从待处理数据中读取到的某一行数据的内容，所以value的类型String
 *
 *      但是，Long或者String等java原声的类型的序列化的效率比较低，所以，hadoop对其进行了封装改造，
 *      有替代品：LongWriteable/Text
 *
 *      map()方法处理数据完成后，需要返回（一个key，一个value）
 *      KEYOUT：是我们的map()方法处理完成后返回的结果的key的数据类型
 *      VALUEOUT：是我们的map()方法处理完成后返回的结果的value的数据类型
 *
 *
 *
 *      业务需求：
 *      统计某个目录中的文件内每个单词出现的总次数
 *
 *      数据：
 *      hello world
 *      hello gaoyuanyuan
 *      hello jiajingwen
 *      hello gp1809
 *      hello qianfeng
 *      hello canglaoshi
 *
 *
 *      思路：
 *      map端的输入：
 *      一行数据
 *      map内部处理流程：
 *      1、通过空格切换行内容
 *      2、循环遍历数组，直接输出数据
 *      map端的输出：
 *      key：word
 *      value：1
 *
 *
 */
public class WordCount {
    /**
     * map()方法的调用规律：每一行数据调用一次map()方法
     */
    static class WCMapper extends Mapper<LongWritable, Text, Text, LongWritable>{
        private static Text k = new Text();
        private static LongWritable v = new LongWritable(1);

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //1、数据类型转换，将Text类型的数据转换成可处理的数据类型String
            String line = value.toString();

            //2、根据空格切分单词
            String[] words = line.split(" ");

            //3、循环遍历单词，每一个单词输出一个<word,1>
            for (String word : words) {
                k.set(word);
                context.write(k,v);
            }
        }
    }

    /**
     *     Reducer<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
     *     reduce()方法要接收的输入参数是一个key，一个value的迭代器<T> values
     *     KEYIN：是框架要传递给咱们自定义的reduce()方法的输入参数中的key的数据类型  --->对应map()方法的输出的key的类型
     *     VALUEIN：是框架要产地给咱们自定义的reduce()方法的输入参数的value的数据类型 -->对应map()方法的输出的value的类型
     *
     *     KEYOUT：是reduce方法处理完成后输出数据的key的类型
     *     VALUEOUT：是reduce方法处理完成后输出数据的value的类型
     *
     *     reduce()方法的调用规律：框架会从map阶段的输出结果中挑出所有的key相同的<key,value>数据对组成一组，然后调用一次reduce()方法
     */
    static class WCReducer extends Reducer<Text, LongWritable, Text, LongWritable>{
        /**
         * 输入数据格式：
         * key：word
         * value：迭代器，同组的所有数据
         * @param key
         * @param values
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */

        /**
         *<canglaoshi,1>
         * <gaoyuanyuan,1>
         * <gp1809,1>
         *  <hello,1>
         *  <hello,1>
         *  <hello,1>
         *  <hello,1>
         * <hello,1>
         *  <hello,1>
         *  <jiajingwen,1>
         *  <qianfeng,1>
         *   <world,1>
         */

        private static LongWritable v = new LongWritable();
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
           long count=0;

            //1、循环遍历value
            for (LongWritable value : values) {
                count+=value.get();
            }

            //将结果输出到文件
            v.set(count);
            context.write(key,v);
        }
    }

    /**
     * 相当于一个yarn的客户端
     * 需要封装一个mr的job，指定job的相关参数
     * 最后提交job给yarn集群
     * @param args
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1、获取配置对象Configuration
        Configuration conf = new Configuration();

        //2、设置hdfs的连接参数
        conf.set("fs.defaultFS","file:///");
        conf.set("mapreduce.framework.name","local");

        //3、获取Job对象
        Job job = Job.getInstance(conf, "wordcount");

        //4、指定程序的jar包运行的本地路径
        job.setJarByClass(WordCount.class);

        //5、设置本业务需要执行的Mapper类
        job.setMapperClass(WCMapper.class);

        //6、设置Mapper的输出参数的数据类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        //7、指定本Job要使用的Reducer业务类
        job.setReducerClass(WCReducer.class);

        //8、设置job的输出数据的类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        //设置combiner组件使用的类
        job.setCombinerClass(WCReducer.class);

        //job.setNumReduceTasks(Integer.parseInt(args[2]));

        //9、设置job的输入文件的路径
        FileInputFormat.setInputPaths(job,new Path("F:\\Data\\GP1809\\wordcount\\multiFiles"));

        //10、设置job的输出文件的路径
        FileOutputFormat.setOutputPath(job,new Path("F:\\Data\\GP1809\\wordcount\\multiFilesout2"));

        //11、提交job到集群中
        boolean b = job.waitForCompletion(true);

        System.exit(b?0:1);
    }
}
