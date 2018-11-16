package hive;

import com.google.common.base.Strings;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName parseLogger
 * @Description TODO
 * @Author Chenfg
 * @Date 2018/9/5 0005 16:46
 * @Version 1.0
 */
public class parseLogger extends UDF{
    public String evaluate(String log) throws ParseException {
        if (Strings.isNullOrEmpty(log)){
            return null;
        }

        //定义一个正则表达式
        //日志数据
        //220.181.108.151 - - [31/Jan/2012:00:02:32 +0800] \"GET /home.php?mod=space&uid=158&do=album&view=me&from=space HTTP/1.1\" 200 8784 \"-\" \"Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)\"
        //220.181.108.151	20120131 120232	GET	/home.php?mod=space&uid=158&do=album&view=me&from=space	HTTP	200	Mozilla
        String reg = "^([0-9.]+\\d+) - - \\[(.* \\+\\d+)\\] .+(GET|POST) (.+) (HTTP)\\S+ (\\d+) .+\\\"(\\w+).+$";

        //构造Pattern
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(log);

        StringBuffer sb = new StringBuffer();

        //判断是否匹配成功
        if (matcher.find()){
            //先获取匹配上的总段数
            int count = matcher.groupCount();

            //循环遍历匹配上的段数
            for (int i = 1; i < count; i++) {
                //对日期字段进行处理转换
                if (i == 2){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd hhmmss");

                    //解析日期数据
                    //31/Jan/2012:00:02:32 +0800
                    Date dt = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH).parse(matcher.group(i));

                    String sdt = sdf.format(dt);
                    sb.append(sdt + "\t");
                }else{
                    sb.append(matcher.group(i) + "\t");
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(new parseLogger().evaluate("220.181.108.151 - - [31/Jan/2012:00:02:32 +0800] \\\"GET /home.php?mod=space&uid=158&do=album&view=me&from=space HTTP/1.1\\\" 200 8784 \\\"-\\\" \\\"Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)\\\""));
    }
}
