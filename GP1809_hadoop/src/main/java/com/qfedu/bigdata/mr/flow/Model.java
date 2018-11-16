package com.qfedu.bigdata.mr.flow;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Created by newforesee on 2018/8/31.
 */
public class Model extends ToolRunner implements Tool {



    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        setConf(conf);
        //创建job
        Job.getInstance(conf,"model");

        return 0;
    }

    public void setConf(Configuration configuration) {
        configuration.set("fs.defaultFS","file:///");
        configuration.set("mapreduce.framwork.name","local");
    }

    public Configuration getConf() {
        return null;
    }
}
