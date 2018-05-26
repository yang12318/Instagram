package com.example.yang.ins;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
    FragmentStatePagerAdapter mAdapter;
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
        //FragmentManager man = AboutFragment.this.getChildFragmentManager();
        //init();
        flist=new ArrayList<Fragment>();
        flist.add(mAboutFollowFragment);
        flist.add(mAboutMeFragment);
        Title = new ArrayList<>();
        Title.add("已关注");
        Title.add("你");
        mTabLayout.addTab(mTabLayout.newTab().setText(Title.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(Title.get(1)));
        mAdapter = new FragmentAdapter(getChildFragmentManager(),Title,flist);
        //mAdapter= new FragmentAdapter(man,flist);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
//        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                mViewPager.setCurrentItem(tab.getPosition());
//                Log.e("TAG", "tab position:" + tab.getPosition());
//                FragmentManager fm = AboutFragment.this.getChildFragmentManager();
//                //开启事务
//                android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
//                Intent intent;
//                switch (tab.getPosition()) {
//                    case 0: {
//                        //mAlbumFragment = AlbumFragment.newInstance("相册", userId);
//                        if (mAboutFollowFragment == null) {
//                            mAboutFollowFragment = new AboutFollowFragment();
//                            Bundle bundle = new Bundle();
//                            bundle.putInt("id", -1);
//                            mAboutFollowFragment.setArguments(bundle);
//                        }
//                        transaction = fm.beginTransaction();
//                        transaction.replace(R.id.viewpager, mAboutFollowFragment);
//                        transaction.commit();
//                        break;
//                    }
//                    case 1: {
//                        if (mAboutMeFragment == null) {
//                            mAboutMeFragment = new AboutMeFragment();
//                            Bundle bundle = new Bundle();
//                            bundle.putInt("id", -1);
//                            mAboutMeFragment.setArguments(bundle);
//                        }
//                        transaction = fm.beginTransaction();
//                        transaction.replace(R.id.viewpager, mAboutMeFragment);
//                        transaction.commit();
//                        break;
//                    }
//                    default:
//                        break;
//                }
//                //mTabLayout.getTabAt(0).select();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                setDefaultFragment();
//            }
//        });
        Bundle bundle = getArguments();
        return view;
    }
    private void setDefaultFragment() {
        FragmentManager fm = getChildFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        mAboutFollowFragment = new AboutFollowFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", -1);
        mAboutFollowFragment.setArguments(bundle);
        transaction.replace(R.id.viewpager, mAboutFollowFragment);
        transaction.commit();
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
            return fragment;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            AboutFollowFragment fragment = (AboutFollowFragment) super.instantiateItem(container, position);
            fragment.updateArguments(list_Title.get(position));
            return fragment;
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
        //private List<Fragment> fragmentList;

//        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
//            super(fm);
//            this.fragmentList = fragmentList;
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return fragmentList.get(position);
//        }
//        @Override
//        public int getCount() {
//            return fragmentList.size();
//        }
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            //super.destroyItem(container, position, object);
//        }
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return title[position];
//        }
//    }
}