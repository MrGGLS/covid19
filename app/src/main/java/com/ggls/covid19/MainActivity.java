package com.ggls.covid19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity{
    public static final String EXTRA_MESSAGE = "com.ggls.covid19.MESSAGE";
    FloatingActionButton seeMore;
    CardView toQr;
    CardView toEPMap;
    CardView toGuide;
    boolean isOpen=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seeMore = findViewById(R.id.see_more);

        toQr=findViewById(R.id.to_qr);
        toQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,QRCodeActivity.class);
                showPopUpMenu(seeMore);
                startActivity(intent);
            }
        });
        toEPMap=findViewById(R.id.to_ep_map);
        toEPMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,EPMapActivity.class);
                showPopUpMenu(seeMore);
                startActivity(intent);
            }
        });
        toGuide=findViewById(R.id.to_guide);
        toGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,GuideActivity.class);
                showPopUpMenu(seeMore);
                startActivity(intent);
            }
        });

        seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpMenu(v);
            }
        });
    }

    public void showPopUpMenu(View v){
        if(!isOpen){
            toQr.setVisibility(View.VISIBLE);
            toEPMap.setVisibility(View.VISIBLE);
            toGuide.setVisibility(View.VISIBLE);
            seeMore.animate().rotation(-45);
            toQr.animate().translationY(-getResources().getDimension(R.dimen.dp_300)).translationX(-getResources().getDimension(R.dimen.dp_50)).alpha(1f);
            toEPMap.animate().translationY(-getResources().getDimension(R.dimen.dp_200)).translationX(-getResources().getDimension(R.dimen.dp_50)).alpha(1f);
            toGuide.animate().translationY(-getResources().getDimension(R.dimen.dp_100)).translationX(-getResources().getDimension(R.dimen.dp_50)).alpha(1f);
        }else{
            seeMore.animate().rotation(0);
            toQr.animate().translationY(0).translationX(0).alpha(0f);
            toEPMap.animate().translationY(0).translationX(0).alpha(0f);
            toGuide.animate().translationY(0).translationX(0).alpha(0f);
//            toQr.setVisibility(View.INVISIBLE);
//            toEPMap.setVisibility(View.INVISIBLE);
//            toGuide.setVisibility(View.INVISIBLE);
        }
        isOpen=!isOpen;
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

}