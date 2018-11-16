package com.qfedu.bigdata.mr;


import org.apache.hadoop.io.Text;

/**
 * @ClassName test
 * @Description TODO
 * @Author Chenfg
 * @Date 2018/8/29 0029 15:54
 * @Version 1.0
 */
public class test {
    public static void main(String[] args) {
        Text a = new Text("a");
        Text b = new Text("a");

        System.out.println(a.equals(b));

        String aa = "a";
        String bb = "a";

        System.out.println(aa.equals(bb));
    }
}
