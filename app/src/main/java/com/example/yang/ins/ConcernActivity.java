package com.example.yang.ins;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.yang.ins.Utils.HelloHttp;
import com.example.yang.ins.adapter.ConcernPersonAdapter;
import com.example.yang.ins.bean.Person;

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

public class ConcernActivity extends AppCompatActivity {

    private ImageButton ib_back;
    private List<Person> list;
    private RecyclerView recyclerView;
    private ConcernPersonAdapter adapter;
    private EasyRefreshLayout easyRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concern);
        ib_back = (ImageButton) findViewById(R.id.ib_concern_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        adapter = new ConcernPersonAdapter(R.layout.item_concern, list);
        initView();
        initData();
        adapter.setNewData(list);
        initAdapter();
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.empty_list);
        adapter.setHeaderFooterEmpty(true, true);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_concern);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        easyRefreshLayout = (EasyRefreshLayout) findViewById(R.id.easylayout);
        easyRefreshLayout.setLoadMoreModel(LoadModel.NONE);
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                //不具备加载更多功能
            }

            @Override
            public void onRefreshing() {
                initData();
                initAdapter();
                easyRefreshLayout.loadMoreComplete(new EasyRefreshLayout.Event() {
                    @Override
                    public void complete() {
                        adapter.setNewData(list);
                        easyRefreshLayout.refreshComplete();
                    }
                }, 500);
            }
        });
    }

    private void initData() {
        Map<String, Object> map = new HashMap<>();
        list = new ArrayList<>();
        HelloHttp.sendGetRequest("api/user/lookme", map, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ConcernActivity", "FAILURE");
                Looper.prepare();
                Toast.makeText(ConcernActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d("ConcernActivity", responseData);
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(responseData);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Person person = new Person();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        person.setId(jsonObject.getInt("id"));
                        person.setName(jsonObject.getString("username"));
                        person.setNickname(jsonObject.getString("nickname"));
                        person.setSrc(jsonObject.getString("profile_picture"));
                        list.add(person);
                    }
                    mHandler.sendEmptyMessageDelayed(1, 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                    try {
                        String result = null;
                        result = new JSONObject(responseData).getString("status");
                        Looper.prepare();
                        Toast.makeText(ConcernActivity.this, result, Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    private void initAdapter() {
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.concern_follow) {

                }
                else if(view.getId() == R.id.concern_head || view.getId() == R.id.concern_nickname || view.getId() == R.id.concern_username) {
                    int userId = list.get(position).getId();
                    Intent intent = new Intent(ConcernActivity.this, UserActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what == 1)
            {
                adapter.setNewData(list);
            }
        }
    };


}
