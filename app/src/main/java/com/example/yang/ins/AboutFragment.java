package com.example.yang.ins;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
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
    FragmentPagerAdapter mAdapter;
    ArrayList<Fragment> flist;
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
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        FragmentManager man = AboutFragment.this.getFragmentManager();
        initFragment();
        flist=new ArrayList<Fragment>();
        flist.add(mAboutFollowFragment);
        flist.add(mAboutMeFragment);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mAdapter= new FragmentAdapter(man,flist);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
        Bundle bundle = getArguments();
        return view;
    }
    private void initFragment() {
        mAboutFollowFragment=new AboutFollowFragment();
        mAboutMeFragment=new AboutMeFragment();
    }
    public class FragmentAdapter extends FragmentPagerAdapter {

        private String [] title = {"已关注","你"};
        private List<Fragment> fragmentList;

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
        @Override
        public int getCount() {
            return fragmentList.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }
}