package com.ggls.covid19;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AboutDevelopers extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developer);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.dev_container,new DevelopersBriefFragment())
                .commit();
    }
}
