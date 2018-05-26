package com.example.yang.ins;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.yang.ins.Utils.HelloHttp;
import com.example.yang.ins.adapter.Info1Adapter;
import com.example.yang.ins.bean.Info1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class AboutMeFragment extends Fragment{
    private List<Info1> mInfoList;
    private RecyclerView recyclerView;
    private EasyRefreshLayout easyRefreshLayout;
    private View view;
    private String mtd_id;
    //private Info1Adapter adapter;
//    public static AboutMeFragment newInstance(int number) {
//        Bundle args = new Bundle();
//        args.putInt("123",number);
//        AboutMeFragment fragment = new AboutMeFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
    public static AboutMeFragment newInstance(String mtd_id) {
        AboutMeFragment f = new AboutMeFragment();
        Bundle b = new Bundle();
        b.putString("id", mtd_id);
        f.setArguments(b);
        return f;
    }

    public void updateArguments(String mtd_id) {
        this.mtd_id = mtd_id;
        Bundle args = getArguments();
        if (args != null) {
            args.putString("id", mtd_id);
        }
    }
    public AboutMeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mtd_id = arguments.getString("id");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about_me, container, false);
        Bundle bundle = getArguments();
        return view;
    }


}
