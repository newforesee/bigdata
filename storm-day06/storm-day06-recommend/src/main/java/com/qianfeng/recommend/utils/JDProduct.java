package com.qianfeng.recommend.utils;

import com.qianfeng.recommend.domain.Product;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Describe: 请补充类描述
 * Author:   chenfenggao
 * Domain:   www.1000phone.com
 * Data:     2015/12/3.
 */
public class JDProduct {
    private static String HTTPS_PROTOCOL = "https:";

    public static void main(String[] args) throws Exception {
        System.out.println(getProduct());
    }

    public static List<Product> getProduct() {
        String url = "https://search.jd.com/Search?keyword=%E5%A5%B3%E8%A3%85&enc=utf-8&suggest=3.his.0.0&wq=&pvid=0471540cbb2e45a49d2e620545686e50";
        List<Product> list = getProductsByUrl(url);
        String url1 = "https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA&enc=utf-8&wq=%E6%89%8B%E6%9C%BA&pvid=14d2fbf8fc9a45a9a03977e16cc047a8";
        List<Product> list1 = getProductsByUrl(url1);

        list.addAll(list1);
        return list;
    }

    private static List<Product> getProductsByUrl(String url) {
        List<Product> list = new ArrayList<Product>();
        try {
            Document doucument = Jsoup.connect(url).get();
            Elements elements = doucument.select("ul[class=gl-warp clearfix]").select("li[class=gl-item]");
            for (Element element : elements) {
                String price = element.select("div[class=p-price]").select("strong").select("i").text();
                if (StringUtils.isBlank(price)) {
                    price = new Random().nextInt(1000) + "";
                }
                String title = element.select("div[class=p-name p-name-type-2]").select("a").attr("title");
                String producturl = element.select("div[class=p-name p-name-type-2]").select("a").attr("href");
                String skuid = element.attr("data-sku");
//                if (StringUtils.isNotBlank(producturl)) {
//                    Pattern pattern = Pattern.compile("\\d+");
//                    Matcher matcher = pattern.matcher(producturl);
//                    StringBuffer buffer = new StringBuffer();
//                    while (matcher.find()) {
//                        buffer.append(matcher.group());
//                        skuid = buffer.toString();
//                    }
//                }
                String pic = element.select("div[class=p-img]").select("a").select("img").attr("src");
                if (StringUtils.isBlank(pic)){
                    pic = element.select("div[class=p-img]").select("a").select("img").attr("source-data-lazy-img");
                };
                if (StringUtils.isNotBlank(pic)){
                    pic=HTTPS_PROTOCOL +pic;
                };

//                System.out.println("skuid : " + skuid);
//                System.out.println("title : " + title);
//                System.out.println("price : " + price);
//                System.out.println("pic : " + pic);
//                System.out.println("produceUrl : " + producturl);


                if (StringUtils.isNotBlank(skuid) &&
                        StringUtils.isNotBlank(title) &&
                        StringUtils.isNotBlank(price) &&
                        StringUtils.isNotBlank(pic) &&
                        StringUtils.isNotBlank(producturl)) {
                    list.add(new Product(skuid, title, price, producturl, pic));
//                } else {
//                    System.out.println(element.toString());
                }
            }
        } catch (Exception e) {
        }
        return list;
    }
}
