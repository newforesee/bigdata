import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by newforesee on 2018/9/13.
 */
public class Hbase2Hdfs {
    static class MyMapper extends TableMapper<Text, NullWritable>{
        @Override
        protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {

//            String val = null;
//            for (Cell listCell : value.listCells()) {
//                byte[] bytes = CellUtil.cloneQualifier(listCell);
//
//
//            }
            StringBuffer sb = new StringBuffer();
            CellScanner scanner = value.cellScanner();
            while (scanner.advance()) {
                Cell cell = scanner.current();
                sb.append(new String(CellUtil.cloneQualifier(cell), StandardCharsets.UTF_8));
                sb.append(":");
                sb.append("\t");
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {


        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS","hdfs://master:9000");
        configuration.set("hbase.zookeeper.quorum","master:2181,slave1:2181,slave2:2181");

        Job job = Job.getInstance(configuration,"hbase2hdfs");
        TableMapReduceUtil.initTableMapperJob("ns1:table",
                getScan(),
                MyMapper.class,
                Text.class,
                NullWritable.class,
                job
                );
        FileOutputFormat.setOutputPath(job,new Path(args[0]));
        boolean b = job.waitForCompletion(true);
        System.exit(b?0:1);


    }
    private static Scan getScan(){
        return new Scan();
    }
}
