package com.example.yang.ins;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class AboutFragment extends Fragment{
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private AboutFollowFragment mAboutFollowFragment;
    private AboutMeFragment mAboutMeFragment;
    private List<String> Title;
    private FragmentStatePagerAdapter mAdapter;
    private List<Fragment> flist;
    public static AboutFragment newInstance(String param1) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public AboutFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        if(container.getTag()==null){
            view = inflater.inflate(R.layout.fragment_about, container, false);
            init();
            container.setTag(view);
        }else{
            view = (View) container.getTag();
        }
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        flist=new ArrayList<Fragment>();
        flist.add(mAboutFollowFragment);
        flist.add(mAboutMeFragment);
        Title = new ArrayList<>();
        Title.add("已关注");
        Title.add("你");
        mTabLayout.addTab(mTabLayout.newTab().setText(Title.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(Title.get(1)));
        mAdapter = new FragmentAdapter(getChildFragmentManager(),Title,flist);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
        Bundle bundle = getArguments();
        return view;
    }
    private void init() {
        if(mAboutFollowFragment == null) {
            mAboutFollowFragment=new AboutFollowFragment();
        }
        if(mAboutMeFragment == null) {
            mAboutMeFragment=new AboutMeFragment();
        }
    }
    public class FragmentAdapter extends FragmentStatePagerAdapter {

        //fragment列表
        private List<Fragment> list;
        //tab名的列表
        private List<String> list_Title;

        public FragmentAdapter(FragmentManager fm, List<String> list_Title, List<Fragment> list) {
            super(fm);
            this.list_Title = list_Title;
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            AboutFollowFragment fragment = AboutFollowFragment.newInstance(list_Title.get(position));
            AboutMeFragment fragment2 = AboutMeFragment.newInstance(list_Title.get(position));
            return list.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return list_Title.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return list_Title.get(position);
        }
    }
}