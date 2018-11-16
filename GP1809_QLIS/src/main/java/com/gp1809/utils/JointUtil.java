package com.gp1809.utils;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;

import java.io.*;
import java.util.Map;

/**
 * creat by newforesee 2018/10/25
 */
public class JointUtil {

    /**
     * 将传入的字段按顺序拼接
     * @param strings 需要拼接的字段
     * @return
     */
    public static Text joint(String separator,String...strings) {

        return new Text(appender(separator,strings).toString());
    }

    /**
     * 拼接
     * @param strings
     * @return
     */
    private static StringBuilder appender(String separator,String... strings){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            if (i == strings.length - 1) {
                sb.append(strings[i]);
            } else
                sb.append(strings[i]).append(separator);
        }
        return sb;
    }


    /**
     * 将缓存文件按行取出,按照指定key和value存入map,传入value为可变参数
     *
     * @param paths     缓存文件路径
     * @param map       数据要存入的map
     * @param separator 两个字段之间的切割符
     * @param columKey  key所在的字段号(从0开始)
     * @param valves    value字段号,可变参数,可以用数组传入,传入-1将取本行所有数据
     * @throws IOException 由于涉及缓存文件读取,可能会抛出IOException
     */
    public static void colum2Map(Path[] paths,Map<String, String> map, String separator,int columKey, int... valves) throws IOException {
        for (Path path : paths) {
            BufferedReader br;
            String str;

            br = new BufferedReader(new FileReader(new File(path.toString())));
            while ((str = br.readLine()) != null) {
                String strs[] = str.split("\\001");

                String[] strings = new String[valves.length];
                int i = 0;
                for (int valve : valves) {
                    strings[i++]=strs[valve];
                }

                map.put(strs[columKey], appender(separator,strings).toString());
            }
            br.close();

        }
    }

}
