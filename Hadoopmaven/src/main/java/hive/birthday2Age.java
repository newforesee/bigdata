package hive;

import com.google.common.base.Strings;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.Calendar;

/**
 * @ClassName birthday2Age
 * @Description TODO
 * @Author Chenfg
 * @Date 2018/9/5 0005 16:02
 * @Version 1.0
 * 生日转换成年龄
 * 输入：
 * 1981-12-18
 * 输出：
 * 37
 * 思路：
 * age=当前年-生日年
 * 判断 当前月< 生日月
 * age-=1
 * elseif(当前月=生日月 && 当前日期<生日日期)
 * age-=1
 *
 */
public class birthday2Age extends UDF{
    public int evaluate(String birth){
        //1、判断输入参数
        if (Strings.isNullOrEmpty(birth)){
            return -1;
        }

        //2、获取生日年、月、日
        String[] birthdays = birth.split("-");

        int bYear = Integer.parseInt(birthdays[0]);
        int bMonth = Integer.parseInt(birthdays[1]);
        int bDay = Integer.parseInt(birthdays[2]);

        //3、获取当前日期
        Calendar calendar = Calendar.getInstance();

        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH) + 1;
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);

        //3、计算年龄
        int age = nowYear - bYear;

        //4、判断月份
        if (nowMonth < bMonth){
            age-=1;
        } else if (nowMonth == bMonth && nowDay < bDay){
            age-=1;
        }

        return age;
    }

    public static void main(String[] args) {
        System.out.println(new birthday2Age().evaluate("1981-10-18"));
    }
}
