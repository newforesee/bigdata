package com.gp1809.utils;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;

import java.io.*;
import java.util.Map;

/**
 * creat by newforesee 2018/10/25
 */
public class JointUtil {

    public static Text joint(String...strings) {

        return new Text(appender(strings).toString());
    }


    private static StringBuilder appender(String... strings){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            if (i == strings.length - 1) {
                sb.append(strings[i]);
            } else
                sb.append(strings[i]).append("\001");
        }
        return sb;
    }


    public static void colum2Map(Path[] paths,Map<String, String> map, int columKey, int... valves) throws IOException {
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

                map.put(strs[columKey], appender(strings).toString());
            }
            br.close();

        }
    }

}
