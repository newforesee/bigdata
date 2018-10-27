package com.qianfeng.recommend.service;


import com.qianfeng.recommend.domain.Template;

/**
 * Describe: 规则配置服务
 * Author:   chenfenggao
 * Domain:   www.1000phone.com
 * Data:     2015/12/2.
 */
public interface RuleService {
    Template getTemplateByAdId(String adId);

    boolean isExist(String adId);
}
