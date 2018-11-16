package hive;

import com.google.common.base.Strings;
import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * Created by newforesee on 2018/9/8.
 */
public class KaoshiUDF extends UDF {
    public String evaluate(String line){
        if (Strings.isNullOrEmpty(line)) {
            return null;
        }
        String[] strings = line.split("\t");

        return null;
    }
}
