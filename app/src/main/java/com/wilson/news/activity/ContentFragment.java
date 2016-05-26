package com.wilson.news.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wilson.news.R;
import com.wilson.news.base.BasePager;
import com.wilson.news.base.impl.GovAffairsPager;
import com.wilson.news.base.impl.HomePager;
import com.wilson.news.base.impl.NewsCenterPager;
import com.wilson.news.base.impl.SettingPager;
import com.wilson.news.base.impl.SmartServicePager;

import java.util.ArrayList;

/**
 * 主页内容
 *
 * @author wilson
 */
public class ContentFragment extends BaseFragment {

    @ViewInject(R.id.rg_group)
    private RadioGroup rgGroup;

    @ViewInject(R.id.vp_content)
    private ViewPager mViewPager;

    private ArrayList<BasePager> mPagerList = new ArrayList<>();

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        ViewUtils.inject(this, view); // 注入view和事件
        return view;
    }

    @Override
    public void initData() {
        rgGroup.check(R.id.rb_home);// 默认勾选首页

        // 初始化5个子页面
        mPagerList.add(new HomePager(mActivity));
        mPagerList.add(new NewsCenterPager(mActivity));
        mPagerList.add(new SmartServicePager(mActivity));
        mPagerList.add(new GovAffairsPager(mActivity));
        mPagerList.add(new SettingPager(mActivity));
        mViewPager.setAdapter(new ContentAdapter());

        // 监听RadioGroup的选择事件
        rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        mViewPager.setCurrentItem(0, false);// 去掉切换页面的动画
                        break;
                    case R.id.rb_news:
                        mViewPager.setCurrentItem(1, false);// 设置当前页面
                        break;
                    case R.id.rb_smart:
                        mViewPager.setCurrentItem(2, false);// 设置当前页面
                        break;
                    case R.id.rb_gov:
                        mViewPager.setCurrentItem(3, false);// 设置当前页面
                        break;
                    case R.id.rb_setting:
                        mViewPager.setCurrentItem(4, false);// 设置当前页面
                        break;
                }
            }
        });

        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                mPagerList.get(arg0).initData();// 获取当前被选中的页面, 初始化该页面数据
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
//        mPagerList.get(0).initData();// 初始化首页数据
    }

    class ContentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = mPagerList.get(position);
            container.addView(pager.mRootView);
            // pager.initData();// 初始化数据.... 不要放在此处初始化数据, 否则会预加载下一个页面
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 获取新闻中心页面
     */
    public NewsCenterPager getNewsCenterPager() {
        return (NewsCenterPager) mPagerList.get(1);
    }

}
