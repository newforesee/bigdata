package com.qianfeng.recommend.service.impl;


import com.qianfeng.recommend.domain.Product;
import com.qianfeng.recommend.domain.Template;
import com.qianfeng.recommend.service.ProductService;
import com.qianfeng.recommend.service.RecommendModelService;
import com.qianfeng.recommend.service.RecommendService;
import com.qianfeng.recommend.service.RuleService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Describe: 处理推荐逻辑
 * Author:   chenfenggao
 * Domain:   www.1000phone.com
 * Data:     2015/12/2.
 */
public class RecommendServiceImpl implements RecommendService {
    //推荐模型
    private RecommendModelService recommendModelService;
    //针对广告位的推荐规则
    private RuleService ruleService;
    //商品服务
    private ProductService productService;

    public List<Product> recomend(String adId, String userId, String views) {
        List<Product> recommendResult = new ArrayList<Product>();
        //判断当前广告位是否有对应的推荐模型,如果没有推荐推荐模型就返回NULL
        if (!ruleService.isExist(adId)) {
            return recommendResult;
        }
        //获取推荐对应的规则,计算需要推荐的商品数量
        Template template = ruleService.getTemplateByAdId(adId);
        //根据广告位使用推荐模型计算的结果，每个广告位都有独立的一个或者多个模型进行支撑
        recommendResult = recommend(adId, template.getNum(), userId, views);
        //对硬推广告进行设置
        setSaleAd(template.getProducts(), recommendResult);
        return recommendResult;
    }

    /**
     * 将销售出去的硬广插入其中
     *
     * @param products
     * @param recommendResult
     */
    private void setSaleAd(Map<Integer, Product> products, List<Product> recommendResult) {
        if (products.size() > 0) {
            for (Integer index : products.keySet()) {
                if (index.intValue() <= recommendResult.size() && index.intValue() >= 0) {
                    //这里的实现，是直接替代 某个位置上已经存在的结果。
                    recommendResult.set((index.intValue() - 1), products.get(index));
                }
            }
        }
    }

    /**
     * 每个广告位都有独立的推荐模型支撑，模型是不能通用的。
     *
     * @param adId    广告位的编号
     * @param needNum 广告位需要推荐的商品数量
     * @param userId  当前访问的用户编号
     * @param views   用户在当前会话其间浏览的商品
     * @return
     */
    private List<Product> recommend(String adId, int needNum, String userId, String views) {
        List<Product> list = new ArrayList<Product>();
        if ("其他广告".equals(adId)) {
            //属于其他广告的模型
        }
        //如果广告编号等于121,表示猜你喜欢的广告位
        if ("121".equals(adId)) {

            //获取离线计算好的结果----基于用户的协同过滤算法
            List<String> baseUserList = recommendModelService.recommendByUserCF(userId, needNum);
            //将推荐的商品id，转换成给一个商品对象
            List<Product> baseUserProducts = productService.baseInfo(baseUserList);
            //检查商品上线下线状态，将非上线移除掉---需要24个商品，经过移除，可能只剩14
            checkProduct(baseUserProducts);


            //获取离线计算好的结果----基于物品的协同过滤算法
            List<String> baseItemList = recommendModelService.recommendByItemCF(userId, needNum);
            //将推荐的商品id，转换成给一个商品对象
            List<Product> baseItemProducts = productService.baseInfo(baseItemList);
            //检查商品上线下线状态，将非上线移除掉---需要24个商品，经过移除，可能只剩23
            checkProduct(baseItemProducts);


            //获取基于物品的实时计算结果-----需要实时浏览的商品和物品与物品之间的相似度（协同过滤）
            //1234 3 0.1+0.1+0.1  1513848862:4,1333635503:3,10014487832:5,1447320017:1,1026254997:1,1030931729:5,1445360:5,1415151821:6,1447320017:4,1461610141:2
            //2345 1 0.1 1445360:1,1012930:3,1037992477:5,1468112889:6,1415151821:5,10006915516:2,1598030883:3,1012930:4,1113666013:3,1236759823:2
//            -------------------------
            //求交集，排序（增加浏览因子，同一个商品，每浏览一次，加0.1的权重）
            List<String> baseItemListReal = recommendModelService.recommendByItemCF(userId, needNum, views);
            //将推荐的商品id，转换成给一个商品对象
            List<Product> baseItemProductsReal = productService.baseInfo(baseItemListReal);
            //检查商品上线下线状态，将非上线移除掉---需要24个商品，经过移除，可能只剩16
            checkProduct(baseItemProductsReal);


            //获取基于内容的实时计算结果-----需要实时浏览的商品和物品与物品之间的相似度（按照内容的相似度计算）
            List<String> baseContentList = recommendModelService.recommendByContent(userId, needNum, views);
            //将推荐的商品id，转换成给一个商品对象
            List<Product> baseContentProducts = productService.baseInfo(baseContentList);
            //检查商品上线下线状态，将非上线移除掉---需要24个商品，经过移除，可能只剩24
            checkProduct(baseContentProducts);


            //获取默认的推荐结果
            List<String> defaultIdsList = recommendModelService.defaultRecommend(adId);
            List<Product> defaultList = productService.baseInfo(defaultIdsList);
            checkProduct(defaultList);
            Collections.sort(defaultIdsList);
            System.out.println(defaultIdsList);

            for (int i = 1; i <= needNum; i++) {
                if ((i % 6 == 1) || (i % 6 == 5)) {
                    //封装基于物品的实时推荐结果,如果元素不够，就从默认的推荐推荐结果中获取
                    //去重：如果list中已经有个该product，就从baseItemProductsReal里面继续取值，知道list中不包含
                    getFirstValidItem(list, baseItemProductsReal, defaultList);
                } else if ((i % 6 == 2) || (i % 6 == 0)) {
                    //封装基于用户的离线推荐结果，如果元素不够，就从默认的推荐推荐结果中获取
                    getFirstValidItem(list, baseUserProducts, defaultList);
                } else if (i % 6 == 3) {
                    //封装基于物品的离线推荐结果，如果元素不够，就从默认的推荐推荐结果中获取
                    getFirstValidItem(list, baseItemProducts, defaultList);
                } else {
                    //封装基于内容的离线推荐结果，如果元素不够，就从默认的推荐推荐结果中获取
                    getFirstValidItem(list, baseContentProducts, defaultList);
                }
            }
        }
        return list;
    }

    /**
     * 从原始List中获取一个有效的元素存放到目标List中
     * 如果原始List中没有足够多的元素，就从默认推荐的结果中获取。
     *
     * @param tarList 目标List
     * @param orgList 原始List
     */
    private void getFirstValidItem(List<Product> tarList, List<Product> orgList, List<Product> defaultList) {
        if (orgList == null || orgList.size() < 1) {
            Product product = null;
            do {
                if (defaultList.size() > 0) {
                    product = defaultList.get(0);
                    defaultList.remove(0);
                } else {
                    product = null;
                }
            } while (product != null && tarList.contains(product));
            tarList.add(product);
        }
        Product product = null;
        //将原始的list中的product，抽取一个放入到到目标list中。
        // 1、先获取一个product
        //2、担心目标list中，已经包含了product
        //3、判断目标list中是否已经了该product
        // 4、如果有，再循环，
        do {
            if (orgList.size() > 0) {
                product = orgList.get(0);
                orgList.remove(0);
            } else {
                product = null;
            }
        } while (product != null && tarList.contains(product));
        if (product != null) {
            tarList.add(product);
        }
    }

    private boolean isProductInTarList(Product product, List<Product> tarList) {
        boolean isContains = tarList.contains(product);
        System.out.println(isContains);
        return isContains;
    }


    private void checkProduct(List<Product> recommendList) {
        int size = recommendList.size();
        for (int i = 0; i < size; i++) {
            //如果商品状态为下线状态，将商品移除掉
            if (recommendList.get(i).getStatus() != 1) {
                recommendList.remove(i);
                i--;
                size--;
            }
        }
    }

    public List<Product> defaultRecommend(String adId) {
        return productService.baseInfo(recommendModelService.defaultRecommend(adId));
    }

    public RecommendModelService getRecommendModelService() {
        return recommendModelService;
    }

    public void setRecommendModelService(RecommendModelService recommendModelService) {
        this.recommendModelService = recommendModelService;
    }

    public RuleService getRuleService() {
        return ruleService;
    }

    public void setRuleService(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}
