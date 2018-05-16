package com.example.yang.ins;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.ins.Utils.DateUtil;
import com.example.yang.ins.Utils.HelloHttp;
import com.example.yang.ins.Utils.MD5Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_login;
    private EditText et_user, et_password;
    private TextView tv_register, tv_forget;
    private RelativeLayout relativeLayout;
    private AnimationDrawable anim;
    //private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_forget = (TextView) findViewById(R.id.tv_forget);
        tv_register = (TextView) findViewById(R.id.tv_register);
        et_password = (EditText) findViewById(R.id.et_passwordInput);
        et_user = (EditText) findViewById(R.id.et_usernameInput);
        //checkBox = (CheckBox) findViewById(R.id.checkbox);
        et_user.setText("instagram@mail.com");
        et_password.setText("instagram123");
        et_user.addTextChangedListener(new JumpTextWatcher(et_user, et_password));
        btn_login.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        container.setBackgroundResource(R.drawable.animation_list);
        anim = (AnimationDrawable) container.getBackground();
        anim.setEnterFadeDuration(6000);
        anim.setExitFadeDuration(2000);
        anim.start();
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_login) {
            final String email = et_user.getText().toString();
            String password = et_password.getText().toString();
            if(email.length() <= 0 || email == null) {
                showToast("您还未填写邮箱或您的全名");
                return;
            }
            if(password.length() <= 0 || password == null) {
                showToast("您还未填写密码");
                return;
            }
            password = MD5Util.encode(password);
            Map<String, Object> map = new HashMap<>();
            map.put("account", email);
            final String finalPassword = password;
            HelloHttp.sendFirstPostRequest("api/user/checkout", map, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("LoginActivity", "FAILURE");
                    showToast("服务器错误");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.d("LoginActivity", responseData);
                    String result = null;
                    try {
                        result = new JSONObject(responseData).getString("status");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(result.equals("Exist")) {
                        Map<String, Object> map = new HashMap<>();
                        if(isEmail(email)) {
                            map.put("login_type", "email");
                            map.put("email", email);
                        }
                        else {
                            map.put("login_type", "username");
                            map.put("username", email);
                        }
                        map.put("password", finalPassword);
                        HelloHttp.sendFirstPostRequest("api/user/login", map, new okhttp3.Callback() {

                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("LoginActivity", "FAILURE");
                                Looper.prepare();
                                showToast("服务器错误");
                                Looper.loop();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseData = response.body().string();
                                Log.d("LoginActivity", responseData);
                                String result = null;
                                String Authorization = null;
                                int id = -10;
                                try {
                                    result = new JSONObject(responseData).getString("status");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("LoginActivity", "JSONException");
                                }
                                if(result.equals("Success")) {
                                    Looper.prepare();
                                    showToast("登录成功");
                                    try {
                                        showToast("12345678");
                                        JSONObject jsonObject = new JSONObject(responseData);
                                        Authorization = jsonObject.getString("Authorization");
                                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                                        jsonObject = jsonArray.getJSONObject(0);
                                        id = jsonObject.getInt("id");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e("LoginActivity", "JSONException");
                                    }
                                    Log.e("LoginActivity", Integer.toString(id));
                                    SharedPreferences mShared;
                                    mShared = LoginActivity.this.getSharedPreferences("share", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = mShared.edit();
                                    editor.putString("Authorization", Authorization);
                                    editor.putString("Date", DateUtil.getNowDateTime("yyyyMMddHHmmss"));
                                    editor.putInt("id", id);
                                    editor.commit();
                                    MainApplication application = MainApplication.getInstance();
                                    application.mInfoMap.put("id", id);
                                    showToast("跳转");
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    Looper.loop();
                                }
                                else {
                                    Looper.prepare();
                                    showToast(result);
                                    Looper.loop();
                                }
                            }
                        });
                    }
                    else if(result.equals("NotExist")) {
                        Looper.prepare();
                        showToast("该账号未注册");
                        Looper.loop();
                        return;
                    }
                    else {
                        Looper.prepare();
                        showToast(result);
                        Looper.loop();
                        return;
                    }
                }
            });
        }
        else if(v.getId() == R.id.tv_register) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.tv_forget) {
            Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
            startActivity(intent);
        }
    }
    private class JumpTextWatcher implements TextWatcher {
        private EditText mThisView = null;
        private View mNextView = null;

        public JumpTextWatcher(EditText vThis, View vNext) {
            super();
            mThisView = vThis;
            if(vNext != null) {
                mNextView = vNext;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String str= s.toString();
            if (str.indexOf("\r") >= 0 || str.indexOf("\n") >= 0) {      //发现输入回车或换行
                mThisView.setText(str.replace("\r", "").replace("\n", ""));
                if (mNextView != null) {
                    mNextView.requestFocus();
                    if (mNextView instanceof EditText) {         //让光标自动移动到编辑框的文本末尾
                        EditText et = (EditText) mNextView;
                        et.setSelection(et.getText().length());
                    }
                }
            }
        }
    }

    public void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public static boolean isEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}
