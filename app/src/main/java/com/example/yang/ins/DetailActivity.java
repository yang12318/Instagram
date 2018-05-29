package com.example.yang.ins;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yang.ins.Utils.HelloHttp;
import com.example.yang.ins.bean.Dynamic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;
import pub.devrel.easypermissions.EasyPermissions;

public class DetailActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, BGANinePhotoLayout.Delegate{
    private int postId = 0;
    private ImageButton ib_back, ib_menu, ib_like, ib_collect, ib_comment;
    private CircleImageView ci_head;
    private TextView tv_username, tv_introduction, tv_review, tv_time;
    private TextSwitcher tv_like;
    final Dynamic dynamic = new Dynamic();
    BGANinePhotoLayout ninePhotoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        postId = intent.getIntExtra("id", 0);
        ci_head = (CircleImageView) findViewById(R.id.ci_head);
        ib_back = (ImageButton) findViewById(R.id.ib_detail_back);
        ib_menu = (ImageButton) findViewById(R.id.ib_menu);
        ib_like = (ImageButton) findViewById(R.id.ib_like);
        ib_comment = (ImageButton) findViewById(R.id.ib_comment);
        ib_collect = (ImageButton) findViewById(R.id.ib_collect);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_introduction = (TextView) findViewById(R.id.tv_detail) ;
        tv_review = (TextView) findViewById(R.id.tv_comment);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_like = (TextSwitcher) findViewById(R.id.tv_like);
        ninePhotoLayout = (BGANinePhotoLayout) findViewById(R.id.npl_item_moment_photos);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        initData();             //向服务器请求数据
        ninePhotoLayout.setDelegate(DetailActivity.this);
    }

    private void initData() {
        Map<String, Object> map = new HashMap<>();
        HelloHttp.sendGetRequest("api/post/brief/" + Integer.toString(postId), map, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("DetailActivity", "FAILURE");
                Toast.makeText(DetailActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d("DetailActivity", responseData);
                try {
                    final JSONObject jsonObject = new JSONObject(responseData);
                    dynamic.setUsername(jsonObject.getString("username"));
                    dynamic.setIntroduction(jsonObject.getString("introduction"));
                    dynamic.setPub_time(jsonObject.getString("Pub_time"));
                    dynamic.setSrc(jsonObject.getString("profile_picture"));
                    dynamic.setLikes_num(jsonObject.getInt("likes_num"));
                    dynamic.setCom_num(jsonObject.getInt("com_num"));
                    dynamic.setIs_collect(jsonObject.getBoolean("is_shoucang"));
                    dynamic.setIs_like(jsonObject.getBoolean("is_dianzan"));
                    dynamic.setId(jsonObject.getInt("post_id"));
                    dynamic.setUserId(jsonObject.getInt("user_id"));
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("postid", postId);
                    HelloHttp.sendGetRequest("api/photoList", map2, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("DetailActivity", "FAILURE");
                            Toast.makeText(DetailActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            Log.d("DetailActivity", responseData);
                            try {
                                JSONObject jsonObject1 = new JSONObject(responseData);
                                JSONArray jsonArray = jsonObject1.getJSONArray("result");
                                ArrayList<String> arrayList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    arrayList.add("http://ktchen.cn"+jsonObject.getString("photo"));
                                }
                                dynamic.setPhotos(arrayList);
                                mHandler.sendEmptyMessageDelayed(1, 0);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                String result = null;
                                try {
                                    result = new JSONObject(responseData).getString("status");
                                    Looper.prepare();
                                    Toast.makeText(DetailActivity.this, result, Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    String result = null;
                    try {
                        result = new JSONObject(responseData).getString("status");
                        Looper.prepare();
                        Toast.makeText(DetailActivity.this, result, Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what == 1)
            {
                if(TextUtils.isEmpty(dynamic.getIntroduction())) {
                    tv_introduction.setVisibility(View.GONE);
                }
                else {
                    tv_introduction.setVisibility(View.VISIBLE);
                    tv_introduction.setText(dynamic.getIntroduction());
                }
                tv_like.setText(dynamic.getLikes_num()+"次赞");
                tv_time.setText(dynamic.getPub_time());
                tv_review.setText("查看全部"+dynamic.getCom_num()+"条评论");
                tv_username.setText(dynamic.getUsername());
                Glide.with(DetailActivity.this).load("http://ktchen.cn"+dynamic.getSrc()).into(ci_head);
                ib_like.setImageResource(dynamic.isIs_like() ? R.drawable.like1 : R.drawable.like2);
                ib_collect.setImageResource(dynamic.isIs_collect() ? R.drawable.collect1 : R.drawable.collect2);
                ninePhotoLayout.setData(dynamic.getPhotos());
            }
        }
    };

    @Override
    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
