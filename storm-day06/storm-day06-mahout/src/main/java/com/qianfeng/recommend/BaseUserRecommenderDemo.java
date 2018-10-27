package com.qianfeng.recommend;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.similarity.precompute.example.GroupLensDataModel;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName BaseUserRecommenderDemo
 * @Description TODO
 * @Author Chenfg
 * @Date 2018/10/23 0023 11:40
 * @Version 1.0
 * 第一代协同过滤算法
 */
public class BaseUserRecommenderDemo {
    public static void main(String[] args) throws IOException, TasteException {
        //1、准备数据，这里是电影评分数据
        File file = new File("F:\\BigData\\ml-10M100K\\ratings.dat");

        //2、将数据加载到内存
        DataModel dataModel = new GroupLensDataModel(file);

        //3、计算用户的相似度。相似度的算法有很多种，采用皮尔逊算法来计算相似度
        UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);

        //4、计算最近邻域，主要有两种方法，一种是固定数量，一种是固定阈值。采用固定数量
        UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(100, similarity, dataModel);

        //5、构造推荐器
        Recommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, similarity);

        //6、给用户2265推荐10部电影
        List<RecommendedItem> itemList = recommender.recommend(2265, 10);

        //打印推荐结果
        for (RecommendedItem item : itemList) {
            System.out.println(item.toString());
        }
    }
}
