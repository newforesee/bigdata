package com.gp1809.clean.Answer_Collect;

import com.gp1809.utils.JointUtil;
import com.gp1809.utils.MD5Util;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class AC_MapJoin extends Mapper<LongWritable, Text, NullWritable, Text> {
    private static Text k = new Text();

    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String lines = value.toString();
        String[] words = lines.split("\\001");

        String[] arr = {words[1], words[8], words[6], words[5], MD5Util.MD5(words[4])
                , words[10], words[9], words[12], words[11], words[11]};

        k.set(JointUtil.joint("\001",arr));
        if (words[14].equals("1")) {
            context.write(NullWritable.get(), k);//主观题是null,所以总成绩就是客观题的分数
        }


    }
}