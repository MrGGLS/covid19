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
        Log.i("tag_of_remote_database", "开始连接数据库");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Log.e("tag_of_remote_database", e.toString());
                    }

                    Connection conn = MySQLConnection.getConnection();
                    if (conn == null) {
                        Log.i("tag_of_remote_database", "连接失败");
                    } else {
                        Log.i("tag_of_remote_database", "连接成功");
                    }
                }
            }
        });
        thread.start();
        Button sign_in_bt = findViewById(R.id.sign_in_button);
        Button sign_up_bt = findViewById(R.id.sign_up_button);
        sign_in_bt.setOnClickListener(v -> {
            Intent intent = new Intent(OriginalActivity.this, MainActivity.class);
            startActivity(intent);
        });
        sign_up_bt.setOnClickListener(v -> {
            Intent intent = new Intent(OriginalActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}