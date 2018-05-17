package com.example.yang.ins;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class Forget2Activity extends AppCompatActivity {

    private String email = null;
    private EditText et_pass, et_repass, et_code;
    private Button btn_code;
    private ImageButton ib_back, ib_finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget2);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        ib_back = (ImageButton) findViewById(R.id.ib_forget2_back);
        ib_finish = (ImageButton) findViewById(R.id.ib_forget2_go);
        //btn_code = (Button)
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ib_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        et_code = (EditText) findViewById(R.id.et_code);
        et_pass = (EditText) findViewById(R.id.et_newcode);
        et_repass = (EditText) findViewById(R.id.et_confirm);
        Drawable db_code=getResources().getDrawable(R.drawable.password);
        db_code.setBounds(0,0,75,75);
        et_code.setCompoundDrawables(db_code,null,null,null);
        Drawable db_pass=getResources().getDrawable(R.drawable.password);
        db_pass.setBounds(0,0,75,75);
        et_pass.setCompoundDrawables(db_pass,null,null,null);
        Drawable db_repass=getResources().getDrawable(R.drawable.password);
        db_repass.setBounds(0,0,75,75);
        et_repass.setCompoundDrawables(db_repass,null,null,null);
        //btn_code.setOn
    }
}
