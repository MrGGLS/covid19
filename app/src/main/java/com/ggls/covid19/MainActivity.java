package com.ggls.covid19;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ggls.covid19.ui.login.LoginActivity;
import com.ggls.covid19.ui.login.LoginViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.ggls.covid19.MESSAGE";
    FloatingActionButton seeMore;
    CardView toQr;
    CardView toEPMap;
    CardView toGuide;
    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seeMore = findViewById(R.id.see_more);

        toQr = findViewById(R.id.to_qr);
        toQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QRCodeActivity.class);
                showPopUpMenu(seeMore);
                startActivity(intent);
            }
        });
        toEPMap = findViewById(R.id.to_ep_map);
        toEPMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EPMapActivity.class);
                showPopUpMenu(seeMore);
                startActivity(intent);
            }
        });
        toGuide = findViewById(R.id.to_guide);
        toGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GuideActivity.class);
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

    public void showPopUpMenu(View v) {
        if (!isOpen) {
            toQr.setVisibility(View.VISIBLE);
            toEPMap.setVisibility(View.VISIBLE);
            toGuide.setVisibility(View.VISIBLE);
            seeMore.animate().rotation(-45);
            toQr.animate().translationY(-getResources().getDimension(R.dimen.dp_300)).translationX(-getResources().getDimension(R.dimen.dp_50)).alpha(1f);
            toEPMap.animate().translationY(-getResources().getDimension(R.dimen.dp_200)).translationX(-getResources().getDimension(R.dimen.dp_50)).alpha(1f);
            toGuide.animate().translationY(-getResources().getDimension(R.dimen.dp_100)).translationX(-getResources().getDimension(R.dimen.dp_50)).alpha(1f);
        } else {
            seeMore.animate().rotation(0);
            toQr.animate().translationY(0).translationX(0).alpha(0f);
            toEPMap.animate().translationY(0).translationX(0).alpha(0f);
            toGuide.animate().translationY(0).translationX(0).alpha(0f);
//            toQr.setVisibility(View.INVISIBLE);
//            toEPMap.setVisibility(View.INVISIBLE);
//            toGuide.setVisibility(View.INVISIBLE);
        }
        isOpen = !isOpen;
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "about developers")
                .setIcon(R.drawable.developer_menu_icon)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0,2,1,"log out")
                .setIcon(R.drawable.logout_menu_icon)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case 1:
                intent = new Intent(MainActivity.this, AboutDevelopers.class);
                startActivity(intent);
                break;
            case 2:
                MaterialAlertDialogBuilder dialogBuilder=new MaterialAlertDialogBuilder(MainActivity.this);
                dialogBuilder.setTitle(getResources().getString(R.string.logout_title))
                        .setMessage(getResources().getString(R.string.logout_text))
                        .setNegativeButton(getResources().getString(R.string.logout_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.logout_accept), (dialog, which) -> {
                            LoginViewModel.logout();
                            backToOriginalActivity();
                        }).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void backToOriginalActivity(){
        Intent intent = new Intent(MainActivity.this, OriginalActivity.class);
        startActivity(intent);
        finish();
    }
}