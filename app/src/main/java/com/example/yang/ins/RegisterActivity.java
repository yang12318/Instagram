package com.example.yang.ins;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.yang.ins.Utils.HelloHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_next;
    private EditText et_email, et_name;
    private RelativeLayout relativeLayout;
    private AnimationDrawable anim;
    private Button btn_login;
    //private ImageView iv_username,iv_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn_login = (Button) findViewById(R.id.tv_login);
        btn_next = (Button) findViewById(R.id.btn_next);
        et_email = (EditText) findViewById(R.id.et_emailInput);
        et_name = (EditText) findViewById(R.id.et_nameInput);
        et_email.addTextChangedListener(new JumpTextWatcher(et_email, et_name));
        RelativeLayout container = (RelativeLayout) findViewById(R.id.register_container);
        container.setBackgroundResource(R.drawable.animation_list);
        anim = (AnimationDrawable) container.getBackground();
        anim.setEnterFadeDuration(6000);
        anim.setExitFadeDuration(2000);
        anim.start();
        /*et_username.setOnFocusChangeListener(new userOnFocusChanageListener());
        et_password.setOnFocusChangeListener(new userOnFocusChanageListener());
        et_nickname.setOnFocusChangeListener(new userOnFocusChanageListener());
        iv_username = (ImageView) findViewById(R.id.iv_delUser) ;
        iv_nickname = (ImageView) findViewById(R.id.iv_delName);
        iv_password = (ImageView) findViewById(R.id.iv_delPass);*/
        //btn_next.setOnClickListener(this);
        /*iv_password.setOnClickListener(this);
        iv_nickname.setOnClickListener(this);
        iv_username.setOnClickListener(this);
        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str1 =  et_username.getText().toString();
                if(str1.length()>0){
                    iv_username.setVisibility(View.VISIBLE);
                } else {
                    iv_username.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str1 =  et_password.getText().toString();
                if(str1.length()>0){
                    iv_password.setVisibility(View.VISIBLE);
                } else {
                    iv_password.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str1 =  et_nickname.getText().toString();
                if(str1.length()>0){
                    iv_nickname.setVisibility(View.VISIBLE);
                } else {
                    iv_nickname.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_next) {
            final String email = et_email.getText().toString();
            final String nickname = et_name.getText().toString();
            if(email.length() <= 0 || email == null) {
                showToast("您的邮箱未填写");
                return;
            }
            if(nickname.length() <= 0 || nickname == null) {
                showToast("您的昵称未填写");
                return;
            }
            if(nickname.length() < 3 || nickname.length() > 15) {
                showToast("昵称长度应为3-15");
                return;
            }
            if(email.length() < 5 || email.length() > 30 || !isEmail(email)) {
                showToast("邮箱格式非法，请检查");
                return;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("account", email);
            HelloHttp.sendFirstPostRequest("api/user/checkout", map, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("RegisterActivity", "FAILURE");
                    Looper.prepare();
                    showToast("服务器错误");
                    Looper.loop();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.d("RegisterActivity", responseData);
                    String result = null;
                    try {
                        result = new JSONObject(responseData).getString("status");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(result.equals("UnknownError")) {
                        Looper.prepare();
                        showToast(result);
                        Looper.loop();
                        return;
                    }
                    else if(result.equals("Exist")) {
                        Looper.prepare();
                        showToast("该邮箱已注册");
                        Looper.loop();
                        return;
                    }
                    else if(result.equals("NotExist")) {
                        Intent intent = new Intent(RegisterActivity.this, Register2Activity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("nickname", nickname);
                        startActivity(intent);
                    }
                }
            });
        }
        else if(v.getId() == R.id.btn_login) {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
    /*
    private class userOnFocusChanageListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if ((v.getId() == et_username.getId()) & (et_username.getText().length() > 0)) {
                iv_username.setVisibility(View.VISIBLE);
            } else iv_username.setVisibility(View.INVISIBLE);
            if ((v.getId() == et_password.getId()) & (et_password.getText().length() > 0)) {
                iv_password.setVisibility(View.VISIBLE);
            } else iv_password.setVisibility(View.INVISIBLE);
            if ((v.getId() == et_nickname.getId()) & (et_nickname.getText().length() > 0)) {
                iv_nickname.setVisibility(View.VISIBLE);
            } else iv_nickname.setVisibility(View.INVISIBLE);
        }
    }*/

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
            String str = s.toString();
            if(str.indexOf("\r") >= 0 || str.indexOf("\n") >= 0) {      //发现输入回车或换行
                mThisView.setText(str.replace("\r", "").replace("\n", ""));
                if(mNextView != null) {
                    mNextView.requestFocus();
                    if(mNextView instanceof EditText) {         //让光标自动移动到编辑框的文本末尾
                        EditText et = (EditText) mNextView;
                        et.setSelection(et.getText().length());
                    }
                }
            }
        }
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
