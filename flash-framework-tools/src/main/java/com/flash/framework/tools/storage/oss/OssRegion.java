package com.flash.framework.tools.storage.oss;

import lombok.Getter;

/**
 * 阿里云Oss存储所处地域，每一个地域对应一个访问域名节点
 *
 * @author zhurg
 * @date 2019/6/12 - 下午3:21
 */
@Getter
public enum OssRegion {

    /**
     * 华东1（杭州）
     */
    hangzhou("cn-hangzhou", "oss-cn-hangzhou.aliyuncs.com"),
    /**
     * 华东2（上海）
     */
    shanghai("cn-shanghai", "oss-cn-shanghai.aliyuncs.com"),
    /**
     * 华北1（青岛）
     */
    qingdao("cn-qingdao", "oss-cn-qingdao.aliyuncs.com"),
    /**
     * 华北2（北京）
     */
    beijing("cn-beijing", "oss-cn-beijing.aliyuncs.com"),
    /**
     * 华北3（张家口）
     */
    zhangjiakou("cn-zhangjiakou", "oss-cn-zhangjiakou.aliyuncs.com"),
    /**
     * 华北5（呼和浩特）
     */
    huhehaote("cn-huhehaote", "oss-cn-huhehaote.aliyuncs.com"),
    /**
     * 华北6（乌兰察布
     */
    wulanchabu("cn-wulanchabu", "oss-cn-wulanchabu.aliyuncs.com"),
    /**
     * 华南1（深圳）
     */
    shenzhen("cn-shenzhen", "oss-cn-shenzhen.aliyuncs.com"),
    /**
     * 华南2（河源）
     */
    heyuan("cn-heyuan", "oss-cn-heyuan.aliyuncs.com"),
    /**
     * 西南1（成都）
     */
    chengdu("cn-chengdu", "oss-cn-chengdu.aliyuncs.com"),

    /**
     * 香港
     */
    hongkong("cn-hongkong", "oss-cn-hongkong.aliyuncs.com"),

    /**
     * 美国西部1（硅谷）
     */
    uswest("us-west-1", "oss-us-west-1.aliyuncs.com"),

    /**
     * 美国东部1（弗吉尼亚）
     */
    useast("us-east-1", "oss-us-east-1.aliyuncs.com"),

    /**
     * 亚太东南1（新加坡）
     */
    apsoutheast1("ap-southeast-1", "oss-ap-southeast-1.aliyuncs.com"),

    /**
     * 亚太东南2（悉尼）
     */
    apsoutheast2("ap-southeast-2", "oss-ap-southeast-2.aliyuncs.com"),

    /**
     * 亚太东南3（吉隆坡)
     */
    apsoutheast3("ap-southeast-3", "oss-ap-southeast-3.aliyuncs.com"),

    /**
     * 亚太东南5 (雅加达)
     */
    apsoutheast5("ap-southeast-5", "oss-ap-southeast-5.aliyuncs.com"),

    /**
     * 亚太东北1（日本）
     */
    apnortheast1("ap-northeast-1", "oss-ap-northeast-1.aliyuncs.com"),

    /**
     * 亚太南部1（孟买）
     */
    apsouth1("ap-south-1", "oss-ap-south-1.aliyuncs.com"),

    /**
     * 欧洲中部1（法兰克福）
     */
    eucentral1("eu-central-1", "oss-eu-central-1.aliyuncs.com"),

    /**
     * 英国（伦敦）
     */
    euwest1("eu-west-1", "oss-eu-west-1.aliyuncs.com"),

    /**
     * 中东东部1（迪拜）
     */
    meeast1("me-east-1", "oss-me-east-1.aliyuncs.com");

    OssRegion(String region, String endpoint) {
        this.region = region;
        this.endpoint = endpoint;
    }

    /**
     * 外网endpoint
     */
    private String endpoint;

    /**
     * region
     */
    private String region;
}