package com.ggls.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.guide_container, TipBriefFragment.newInstance())
                .commit();
    }
}