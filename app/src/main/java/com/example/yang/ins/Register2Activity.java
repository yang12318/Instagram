package com.example.yang.ins;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.yang.ins.Utils.HelloHttp;
import com.example.yang.ins.Utils.MD5Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class Register2Activity extends AppCompatActivity {

    private EditText et_password,et_username;
    private Button btn_finish;
    private RelativeLayout relativeLayout;
    private AnimationDrawable anim;
    private String email, nickname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        nickname = intent.getStringExtra("nickname");
        btn_finish = (Button) findViewById(R.id.btn_finish);
        et_password = (EditText) findViewById(R.id.et_passInput);
        et_username = (EditText) findViewById(R.id.et_userInput);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.register2_container);
        container.setBackgroundResource(R.drawable.animation_list);
        anim = (AnimationDrawable) container.getBackground();
        anim.setEnterFadeDuration(6000);
        anim.setExitFadeDuration(2000);
        anim.start();
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = et_password.getText().toString();
                final String username = et_username.getText().toString();
                if (username.length() <= 0 || username == null) {
                    showToast("未填写全名");
                    return;
                }
                if (password.length() <= 0 || password == null) {
                    showToast("未填写密码");
                    return;
                }
                if (username.length() < 2 || username.length() > 15) {
                    showToast("全名格式不合法，长度应为2-15");
                    return;
                }
                if (password.length() < 6) {
                    showToast("密码长度过短，请换用更复杂的密码");
                    return;
                }
                if (password.length() > 18) {
                    showToast("密码长度过长");
                    return;
                }
                final Map<String, Object> map = new HashMap<>();
                map.put("account", username);
                password = MD5Util.encode(password);
                final String finalPassword = password;
                HelloHttp.sendFirstPostRequest("api/user/checkout", map, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("Register2Activity", "FAILURE");
                        showToast("服务器错误！");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d("Register2Activity", responseData);
                        String result = null;
                        try {
                            result = new JSONObject(responseData).getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("Exist")) {
                            showToast("重名！");
                            return;
                        } else if (result.equals("NotExist")) {
                            Map<String, Object> map2 = new HashMap<>();
                            map2.put("username", username);
                            map2.put("email", email);
                            map2.put("nickname", nickname);
                            map2.put("password", finalPassword);
                            HelloHttp.sendFirstPostRequest("api/user/register", map, new okhttp3.Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.e("Register2Activity", "FAILURE");
                                    showToast("服务器错误！");
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String responseData = response.body().string();
                                    Log.d("Register2Activity", responseData);
                                    String result = null;
                                    try {
                                        result = new JSONObject(responseData).getString("status");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (result.equals("Success")) {
                                        showToast("注册成功，请前往邮箱激活账号");
                                        Intent intent1 = new Intent(Register2Activity.this, LoginActivity.class);
                                        startActivity(intent1);
                                    } else {
                                        showToast(result);
                                    }
                                }
                            });
                        } else {
                            showToast(result);
                        }
                    }
                });
            }
        });
    }

    private void showToast(String s) {
        Looper.prepare();
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }
}
