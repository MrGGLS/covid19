package com.ggls.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.ggls.covid19.ui.login.LoginActivity;

import java.sql.Connection;

import kotlinx.coroutines.internal.ThreadSafeHeap;

public class OriginalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_original2);
        Button sign_in_bt = findViewById(R.id.sign_in_button);
        Button sign_up_bt = findViewById(R.id.sign_up_button);
        sign_in_bt.setOnClickListener(v -> {
            Intent intent = new Intent(OriginalActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        sign_up_bt.setOnClickListener(v -> {
            Intent intent = new Intent(OriginalActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}