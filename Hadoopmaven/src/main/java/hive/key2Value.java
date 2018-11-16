package hive;

import com.google.common.base.Strings;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * @ClassName key2Value
 * @Description TODO
 * @Author Chenfg
 * @Date 2018/9/5 0005 16:20
 * @Version 1.0
 * 输入：
 * 待查找的字符串：sex=1&hight=180&weight=130&sal=28000
 * 查找key：sal
 * 输出：
 * value：1
 * 思路：
 *将数据转换成json串
 * {sex:1,hight:180,weight:130,sal:28000}
 *
 */
public class key2Value extends UDF{
    public String evluate(String str,String key) throws JSONException {
        if(Strings.isNullOrEmpty(str) || Strings.isNullOrEmpty(key)){
            return null;
        }

        //转换数据到json串
        String s1 = str.replace("&", ",");
        String s2 = s1.replace("=",":");

        String s3 = "{" + s2 + "}";

        //构造json对象
        JSONObject jsonObject = new JSONObject(s3);
        return jsonObject.get(key).toString();
       // jsonObject.getString(key);
        //return null;
    }

    public static void main(String[] args) throws JSONException {
        System.out.println(new key2Value().evluate("sex=1&hight=180&weight=130&sal=28000&faceId=98","faceId"));
    }
}
