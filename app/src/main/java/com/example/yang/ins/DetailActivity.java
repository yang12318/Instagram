package com.example.yang.ins;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class DetailActivity extends AppCompatActivity {
    private int postId = 0;
    private ImageButton ib_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        postId = intent.getIntExtra("id", 0);
        ib_back = (ImageButton) findViewById(R.id.ib_detail_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
