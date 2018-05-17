package com.example.yang.ins;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class UserActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        tabLayout= (TabLayout)findViewById(R.id.tab_user);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.album));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.dynamic));

    }
}
