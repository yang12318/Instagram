package com.example.yang.ins;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.yang.ins.Utils.HelloHttp;
import com.example.yang.ins.adapter.FollowPersonAdapter;
import com.example.yang.ins.bean.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Response;

public class FollowActivity extends AppCompatActivity {
    private int userid;
    private ImageButton btn_back;
    private List<Person> list;
    private RecyclerView recyclerView;
    private FollowPersonAdapter adapter;
    private EasyRefreshLayout easyRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        Intent intent = getIntent();
        userid = intent.getIntExtra("user_id", 0);
        btn_back = (ImageButton) findViewById(R.id.ib_follow_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        adapter = new FollowPersonAdapter(R.layout.item_follow, list);
        initView();
        initData();
        adapter.setNewData(list);
        initAdapter();
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.empty_list);
        adapter.setHeaderFooterEmpty(true, true);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_follow);
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
        map.put("user_id", userid);
        list = new ArrayList<>();
        HelloHttp.sendGetRequest("api/user/friends", map, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("FollowActivity", "FAILURE");
                Looper.prepare();
                Toast.makeText(FollowActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d("FollowActivity", responseData);
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(responseData);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        final Person person = new Person();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        person.setId(jsonObject.getInt("id"));
                        person.setName(jsonObject.getString("username"));
                        person.setNickname(jsonObject.getString("nickname"));
                        person.setSrc(jsonObject.getString("profile_picture"));
                        person.setIsFollowed(jsonObject.getBoolean("is_guanzhu"));
//                        Map<String, Object> map2 = new HashMap<>();
//                        HelloHttp.sendGetRequest("api/user/checkfollow/"+Integer.toString(person.getId()), map2, new okhttp3.Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//                                Log.e("FollowActivity", "FAILURE");
//                                Looper.prepare();
//                                Toast.makeText(FollowActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
//                                Looper.loop();
//                            }
//
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
//                                String responseData = response.body().string();
//                                Log.d("FollowActivity", responseData);
//                                String result = null;
//                                try {
//                                    result = new JSONObject(responseData).getString("status");
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                if(result.equals("Yes")) {
//                                    //这个人你关注了
//                                    person.setIsFollowed(true);
//                                }
//                                else if(result.equals("No")) {
//                                    //这个人你没关注
//                                    person.setIsFollowed(false);
//                                }
//                                else if(result.equals("UnknownError")) {
//                                    person.setIsFollowed(false);
//                                    Looper.prepare();
//                                    Toast.makeText(FollowActivity.this, result, Toast.LENGTH_SHORT).show();
//                                    Looper.loop();
//                                }
//                                else {
//                                    person.setIsFollowed(false);
//                                    Looper.prepare();
//                                    Toast.makeText(FollowActivity.this, result, Toast.LENGTH_SHORT).show();
//                                    Looper.loop();
//                                }
//                            }
//                        });
                        list.add(person);
                    }
                    mHandler.sendEmptyMessageDelayed(1, 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                    try {
                        String result = null;
                        result = new JSONObject(responseData).getString("status");
                        Looper.prepare();
                        Toast.makeText(FollowActivity.this, result, Toast.LENGTH_SHORT).show();
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
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemChildClick(final BaseQuickAdapter adapter, final View view, final int position) {
                if (view.getId() == R.id.follow_cancel) {
                    int id = list.get(position).getId();
                    boolean flag = list.get(position).getIsFollowed();
                    Map<String, Object> map = new HashMap<>();
                    map.put("pk", id);
                    if(flag) {
                        //已经关注了
//                        AlertDialog.Builder builder = new AlertDialog.Builder(FollowActivity.this);
//                        builder.setTitle("确认");
//                        builder.setMessage("确认要取消关注吗？");
//                        builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        });
//                        AlertDialog alertDialog = builder.create();
//                        alertDialog.show();
                        changeStyle(false, position);
                        HelloHttp.sendDeleteRequest("api/user/followyou", map, new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("FollowActivity", "FAILURE");
                                changeStyle(true, position);
                                Looper.prepare();
                                Toast.makeText(FollowActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }

                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseData = response.body().string();
                                Log.d("FollowActivity", responseData);
                                String result = null;
                                try {
                                    result = new JSONObject(responseData).getString("status");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    changeStyle(true, position);
                                }
                                if(result.equals("Success")) {
                                    Looper.prepare();
                                    Toast.makeText(FollowActivity.this, "已取消关注", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                                else {
                                    changeStyle(true, position);
                                    if(result.equals("UnknownError")) {
                                        Looper.prepare();
                                        Toast.makeText(FollowActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                    else {
                                        Looper.prepare();
                                        Toast.makeText(FollowActivity.this, result, Toast.LENGTH_SHORT ).show();
                                        Looper.loop();
                                    }
                                }
                            }
                        });
                    }
                    else {
                        //没有关注
                        changeStyle(true, position);
                        HelloHttp.sendPostRequest("api/user/followyou", map, new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("FollowActivity", "FAILURE");
                                changeStyle(false, position);
                                Looper.prepare();
                                Toast.makeText(FollowActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }

                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseData = response.body().string();
                                Log.d("FollowActivity", responseData);
                                String result = null;
                                try {
                                    result = new JSONObject(responseData).getString("status");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    changeStyle(false, position);
                                }
                                if(result != null && result.equals("Success")) {
                                    Looper.prepare();
                                    Toast.makeText(FollowActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                                else {
                                    changeStyle(false, position);
                                    if(result.equals("UnknownError")) {
                                        Looper.prepare();
                                        Toast.makeText(FollowActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                    else if(result.equals("Failure")) {
                                        Looper.prepare();
                                        Toast.makeText(FollowActivity.this, "错误：重复的关注请求，已取消关注", Toast.LENGTH_SHORT).show();
                                        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                                        Looper.loop();
                                    }
                                    else {
                                        Looper.prepare();
                                        Toast.makeText(FollowActivity.this, result, Toast.LENGTH_SHORT ).show();
                                        Looper.loop();
                                    }
                                }
                            }
                        });
                    }
                }
                else if(view.getId() == R.id.follow_head || view.getId() == R.id.follow_nickname || view.getId() == R.id.follow_username) {
                    int userId = list.get(position).getId();
                    Intent intent = new Intent(FollowActivity.this, UserActivity.class);
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

    private void changeStyle(final boolean flag, final int position) {
        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                if(flag) {
                    list.get(position).setIsFollowed(true);
                    Button btn = (Button) adapter.getViewByPosition(recyclerView, position, R.id.follow_cancel);
                    btn.setText("关注中");
                    btn.setTextColor(Color.BLACK);
                    btn.setBackground(getResources().getDrawable(R.drawable.buttonshape2));
                }
                else {
                    list.get(position).setIsFollowed(false);
                    Button btn = (Button) adapter.getViewByPosition(recyclerView, position, R.id.follow_cancel);
                    btn.setText("关注");
                    btn.setTextColor(Color.WHITE);
                    btn.setBackground(getResources().getDrawable(R.drawable.buttonshape3));
                }
            }
        });
    }
}
