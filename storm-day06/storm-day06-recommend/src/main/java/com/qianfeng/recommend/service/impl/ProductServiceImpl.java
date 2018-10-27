package com.qianfeng.recommend.service.impl;

import com.qianfeng.recommend.domain.Product;
import com.qianfeng.recommend.service.ProductService;
import com.qianfeng.recommend.utils.MyShardedJedisPool;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.ShardedJedis;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe: 请补充类描述
 * Author:   chenfenggao
 * Domain:   www.1000phone.com
 * Data:     2015/12/4.
 */
public class ProductServiceImpl implements ProductService {
    public List<Product> baseInfo(List<String> productIds) {
        ShardedJedis jedis = MyShardedJedisPool.getShardedJedisPool().getResource();
        List<Product> list = new ArrayList<Product>();
        for (String id : productIds) {
            String json = jedis.get("recom:prod:" + id);
            if (StringUtils.isNotBlank(json)) {
                list.add(new Gson().fromJson(json, Product.class));
            }
        }
        return list;
    }
}
