package com.wilson.news.global;

/**
 * 定义全局参数
 *
 * @author wilson
 */
public class GlobalContants {
    public static final String SERVER_URL = "http://172.26.143.1:8080/zhbj";
    //	public static final String SERVER_URL ="http://zhihuibj.sinaapp.com/zhbj";
    public static final String CATEGORIES_URL = SERVER_URL + "/categories.json";// 获取分类信息的接口
    public static final String PHOTOS_URL = SERVER_URL + "/photos/photos_1.json";// 获取组图信息的接口
}
