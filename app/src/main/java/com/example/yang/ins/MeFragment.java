package com.example.yang.ins;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yang.ins.Utils.HelloHttp;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

public class MeFragment extends Fragment {

   private Button btn_revise;
   private TabLayout tabLayout;
   private Context context;
   protected View view;
   private TextView tv_concern, tv_follow, tv_dynamic, tv_username, tv_nickname, tv_introduction;
   private CircleImageView civ;
   private String username, nickname, src, birthday, address, introduction = null;
   private int gender = 3, follow_num = 0, concern_num = 0, posts = 0;
   private LinearLayout ll_concern, ll_follow;
   private int UserId = 0;
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
        tv_dynamic = (TextView) view.findViewById(R.id.me_dynamic);
        tv_username = (TextView) view.findViewById(R.id.tv_me);
        tv_nickname = (TextView) view.findViewById(R.id.me_nickname);
        tv_introduction = (TextView) view.findViewById(R.id.tv_bio);
        ll_concern = (LinearLayout) view.findViewById(R.id.ll_concern);
        ll_follow = (LinearLayout) view.findViewById(R.id.ll_follow);
        civ = (CircleImageView) view.findViewById(R.id.me_image);
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
        MainApplication app = MainApplication.getInstance();
        Map<String, Integer> mapParam = app.mInfoMap;
        for(Map.Entry<String, Integer> item_map:mapParam.entrySet()) {
            if(item_map.getKey() == "id") {
                UserId = item_map.getValue();
            }
        }
        if(UserId == 0) {
            Toast.makeText(getActivity(), "全局内存中保存的信息为空", Toast.LENGTH_SHORT).show();
        }
        else {
            Map<String, Object> map = new HashMap<>();
            HelloHttp.sendGetRequest("api/user/detail/"+Integer.toString(UserId), map, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("UserActivity", "FAILURE");
                    Looper.prepare();
                    Toast.makeText(getActivity(), "服务器未响应", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.d("MeFragment", responseData);
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        username = jsonObject.getString("username");
                        nickname = jsonObject.getString("nickname");
                        gender = jsonObject.getInt("gender");
                        birthday = jsonObject.getString("birthday");
                        follow_num = jsonObject.getInt("following_num");
                        concern_num = jsonObject.getInt("followed_num");
                        src = jsonObject.getString("profile_picture");
                        src = "http://ktchen.cn" + src;
                        Log.d("MeFragment", src);
                        //address = jsonObject.getString("address");
                        //introduction = jsonObject.getString("introduction");
                        //posts = jsonObject.getInt("posts");
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
        }
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

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what == 1)
            {
                tv_concern.setText(Integer.toString(concern_num));
                tv_follow.setText(Integer.toString(follow_num));
                tv_nickname.setText(nickname);
                tv_username.setText(username);
                if(introduction == null) {
                    introduction = "这个人很懒，还没有填写个人简介";
                }
                tv_introduction.setText(introduction);
                tv_dynamic.setText(Integer.toString(posts));
                Glide.with(getActivity()).load(src).into(civ);
            }
        }
    };
}



