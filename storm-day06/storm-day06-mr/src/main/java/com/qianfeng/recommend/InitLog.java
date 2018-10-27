package com.qianfeng.recommend;

import java.util.Random;


public class InitLog {
    private static String[] userName = new String[]{"liudehua", "zhangxueyou", "guofucheng", "liming", "wuyifan"};
    private static String[] type = new String[]{"浏览操作", "收藏操作", "点击操作", "关注操作", "评论操作", "加入购物车"};
    private static String[] products = new String[]{"100211", "100212", "100213", "100214", "100215", "100216", "100217", "100218"};

    public static void main(String[] args) {
        for (int i = 0; i < 30; i++) {
            int nameIndex = new Random().nextInt(userName.length);
            int typeIndex = new Random().nextInt(type.length);
            int pIdIndex = new Random().nextInt(products.length);
            System.out.println(type[typeIndex] + "\t" + (typeIndex+1) + "\t" + userName[nameIndex] + "\t" + (nameIndex+1) + "\t" + "http://wwww.1000phone.com?pid="+products[pIdIndex]+".html"+"\t"+products[pIdIndex]);
        }
    }
}
