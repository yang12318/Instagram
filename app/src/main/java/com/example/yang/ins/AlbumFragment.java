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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.yang.ins.Utils.HelloHttp;
import com.example.yang.ins.adapter.AlbumAdapter;
import com.example.yang.ins.bean.Dynamic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;


public class AlbumFragment extends Fragment{
    private List<Dynamic> mDynamicList;
    private RecyclerView recyclerView;
    private AlbumAdapter adapter;
    protected View view;
    private static int Userid = -10;
//    public static AlbumFragment newInstance(String text) {
//        AlbumFragment fragment = new AlbumFragment();
//        Bundle args = new Bundle();
//        args.putString("param", text);
//        fragment.setArguments(args);
//        return fragment;
//    }
    public AlbumFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Userid = bundle.getInt("id");
        }
        if (Userid == -1) {
            MainApplication app = MainApplication.getInstance();
            Map<String, Integer> mapParam = app.mInfoMap;
            for(Map.Entry<String, Integer> item_map:mapParam.entrySet()) {
                if(item_map.getKey().equals("id")) {
                    Userid = item_map.getValue();
                }
            }
        }
        adapter = new AlbumAdapter(R.layout.item_album, mDynamicList);
        initView();
        initData();
        adapter.setNewData(mDynamicList);
        initAdapter();
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.empty_like);
        adapter.setHeaderFooterEmpty(true, true);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_album);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        //recyclerView.addItemDecoration();
    }

    private void initData() {
        mDynamicList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        mDynamicList = new ArrayList<>();
        //map.put("id", id);
        HelloHttp.sendGetRequest("api/user/posts/"+Integer.toString(Userid),map,new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("AlbumFragment", "FAILURE");
                Looper.prepare();
                Toast.makeText(getActivity(), "服务器未响应", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d("AlbumFragment", responseData);
                try {
                    JSONObject jsonObject1 = new JSONObject(responseData);
                    JSONArray jsonArray = jsonObject1.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Dynamic dynamic = new Dynamic();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        dynamic.setId(jsonObject.getInt("post_id"));
                        dynamic.setCount(jsonObject.getInt("photo_num"));
                        dynamic.setPhoto0("http://ktchen.cn"+jsonObject.getString("photo_0"));
                        mDynamicList.add(dynamic);
                    }
                    mHandler.sendEmptyMessageDelayed(1, 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                    String result = null;
                    try {
                        result = new JSONObject(responseData).getString("status");
                        Looper.prepare();
                        Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
       // ImageView multi = (ImageView) getChildAt(0).findViewById(R.id.iv_multi);
//        ImageView multi = (ImageView) adapter.getViewByPosition(recyclerView, position, R.id.iv_multi);
//        int num = mDynamicList.get(position).getCount();
//        if(num > 1)multi.setVisibility(View.VISIBLE);
//        else multi.setVisibility(View.INVISIBLE);
    }

    @SuppressWarnings("unchecked")
    private void initAdapter() {
        //firstAdapter.openLoadAnimation();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                int id = mDynamicList.get(position).getId();
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
        recyclerView.setAdapter(adapter);
    }
    private void setiv(final boolean flag, final int position) {
        if(flag) {
            ImageView multi = (ImageView) adapter.getViewByPosition(recyclerView, position, R.id.iv_multi);
            if (multi != null) {
                multi.setVisibility(View.VISIBLE);
            }
        }
        else {
            ImageView multi = (ImageView) adapter.getViewByPosition(recyclerView, position, R.id.iv_multi);
            if (multi != null) {
                multi.setVisibility(View.INVISIBLE);
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @SuppressLint("CheckResult")
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what == 1)
            {
                adapter.setNewData(mDynamicList);
            }
        }
    };
}
