package com.example.yang.ins;

import android.content.Intent;
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
        //btn_code.setOn
    }
}
