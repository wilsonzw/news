package com.wilson.news.base.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wilson.news.activity.LeftMenuFragment;
import com.wilson.news.activity.MainActivity;
import com.wilson.news.base.BaseMenuDetailPager;
import com.wilson.news.base.BasePager;
import com.wilson.news.base.menudetail.InteractMenuDetailPager;
import com.wilson.news.base.menudetail.NewsMenuDetailPager;
import com.wilson.news.base.menudetail.PhotoMenuDetailPager;
import com.wilson.news.base.menudetail.TopicMenuDetailPager;
import com.wilson.news.domain.NewsData;
import com.wilson.news.global.GlobalContants;
import com.wilson.news.utils.CacheUtils;

import java.util.ArrayList;

/**
 * 新闻中心
 *
 * @author wilson
 */
public class NewsCenterPager extends BasePager {
    private ArrayList<BaseMenuDetailPager> mPagers;// 4个菜单详情页的集合
    private NewsData mNewsData;

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tvTitle.setText("新闻");
        setSlidingMenuEnable(true);// 打开侧边栏
        String cache = CacheUtils.getCache(GlobalContants.CATEGORIES_URL, mActivity);
        if (!TextUtils.isEmpty(cache)) {// 如果缓存存在,直接解析数据, 无需访问网路
            parseData(cache);
        }
        getDataFromServer();// 不管有没有缓存, 都获取最新数据, 保证数据最新
    }

    /**
     * 从服务器获取数据
     */
    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        // 使用xutils发送请求
        utils.send(HttpMethod.GET, GlobalContants.CATEGORIES_URL, new RequestCallBack<String>() {

            // 访问成功, 在主线程运行
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                String result = (String) responseInfo.result;
                parseData(result);
                // 设置缓存
                CacheUtils.setCache(GlobalContants.CATEGORIES_URL, result, mActivity);
            }

            // 访问失败, 在主线程运行
            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }

        });
    }

    /**
     * 解析网络数据
     *
     * @param result 网络请求的数据
     */
    protected void parseData(String result) {
        Gson gson = new Gson();
        mNewsData = gson.fromJson(result, NewsData.class);
        // 刷新测边栏的数据
        MainActivity mainUi = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainUi.getLeftMenuFragment();
        leftMenuFragment.setMenuData(mNewsData);

        // 准备4个菜单详情页
        mPagers = new ArrayList<>();
        mPagers.add(new NewsMenuDetailPager(mActivity, mNewsData.data.get(0).children));
        mPagers.add(new TopicMenuDetailPager(mActivity));
        mPagers.add(new PhotoMenuDetailPager(mActivity, btnPhoto));
        mPagers.add(new InteractMenuDetailPager(mActivity));

        setCurrentMenuDetailPager(0);// 设置菜单详情页-新闻为默认当前页
    }

    /**
     * 设置当前菜单详情页
     */
    public void setCurrentMenuDetailPager(int position) {
        BaseMenuDetailPager pager = mPagers.get(position);// 获取当前要显示的菜单详情页
        flContent.removeAllViews();// 清除之前的布局
        flContent.addView(pager.mRootView);// 将菜单详情页的布局设置给帧布局

        // 设置当前页的标题
        NewsData.NewsMenuData menuData = mNewsData.data.get(position);
        tvTitle.setText(menuData.title);
        pager.initData();// 初始化当前页面的数据
        if (pager instanceof PhotoMenuDetailPager) {
            btnPhoto.setVisibility(View.VISIBLE);
        } else {
            btnPhoto.setVisibility(View.GONE);
        }
    }

}
