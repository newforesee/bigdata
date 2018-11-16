package hive;

import com.google.common.base.Strings;
import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * Created by newforesee on 2018/9/7.
 */
public class KaoshiUDF_old extends UDF {
    public String evaluate(String str) {
        if (Strings.isNullOrEmpty(str)) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        String[] split = str.split("\t");
        for (int i = 0; i < split.length; i++) {

            if (i == 0 || i == 6) {
                split[i] = split[i].substring(1, split[i].length() - 1);
                sb.append(split[i]);

            } else if (i == 1 || i == 2 || i == 4 || i == 5) {
                split[i] = "\'" + (split[i].substring(1, split[i].length() - 1)).toUpperCase() + "\'";
                if (i == 2 || i == 5) {
                    sb.append("[\'" + split[i - 1] + "\'," + split[i] + "\']");
                }
            } else if (i == 9 || i == 10 || i == 11) {
                if (i==11)
                    sb.append("{"+split[9]+","+split[10]+","+split[11]+"}");

            } else
                sb.append(split[i]);

        }


        return sb.toString();
    }




}
