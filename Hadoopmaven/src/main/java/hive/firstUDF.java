package hive;

import com.google.common.base.Strings;
import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * @ClassName firstUDF
 * @Description TODO
 * @Author Chenfg
 * @Date 2018/9/5 0005 14:47
 * @Version 1.0
 */
public class firstUDF extends UDF{
    public String evaluate(String str){
        //1、检查输出参数是否正确
        if (Strings.isNullOrEmpty(str)){
            return null;
        }

        //小写转大写
        return str.toUpperCase();
    }

    public static void main(String[] args) {
        System.out.println(new firstUDF().evaluate("A"));
    }
}
