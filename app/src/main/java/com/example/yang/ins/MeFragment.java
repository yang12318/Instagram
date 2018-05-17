package com.example.yang.ins;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MeFragment extends Fragment {
   private Button btn_revise;
   private TabLayout tabLayout;
   private Context context;
   protected View view;
   private TextView tv_concern, tv_follow;
   private LinearLayout ll_concern, ll_follow;
   public static MeFragment newInstance(String param1) {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public MeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_me, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tb_me);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");
        Bundle bundle = getArguments();
        tv_concern = (TextView) view.findViewById(R.id.me_concern);
        tv_follow = (TextView) view.findViewById(R.id.me_follow);
        ll_concern = (LinearLayout) view.findViewById(R.id.ll_concern);
        ll_follow = (LinearLayout) view.findViewById(R.id.ll_follow);
        ll_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FollowActivity.class);
                startActivity(intent);
            }
        });
        ll_concern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ConcernActivity.class);
                startActivity(intent);
            }
        });
        btn_revise = (Button) view.findViewById(R.id.me_revise);
        btn_revise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReviseActivity.class);
                startActivity(intent);
            }
        });
        tabLayout = (TabLayout) view.findViewById(R.id.tab_me);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.album));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.dynamic));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.like));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.collect));
        tabLayout.setSelectedTabIndicatorColor(Color.BLACK);
        tabLayout.setSelectedTabIndicatorHeight(1);
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.user_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.it_revise:
                intent = new Intent(getActivity(), ReviseActivity.class);
                startActivity(intent);
                break;
            case R.id.it_exit:
                //加入注销账号的逻辑
                //加入清除存储的逻辑
                intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.it_change:
                intent = new Intent(getActivity(), ChangeCodeActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}



