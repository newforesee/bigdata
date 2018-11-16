package hive;

import com.google.common.base.Strings;
import org.apache.hadoop.hive.ql.exec.UDF;
import parquet.org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * @ClassName jsonParser
 * @Description TODO
 * @Author Chenfg
 * @Date 2018/9/6 0006 11:39
 * @Version 1.0
 */
public class jsonParser extends UDF{
    public String evaluate(String jsonLine){
        if (Strings.isNullOrEmpty(jsonLine)){
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            MovieRateBean bean = objectMapper.readValue(jsonLine, MovieRateBean.class);
            return bean.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(new jsonParser().evaluate("{\"movie\":\"1193\",\"rate\":\"5\",\"timeStamp\":\"978300760\",\"uid\":\"1\"}"));
    }
}
