package com.ggls.covid19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class AboutDevelopers extends AppCompatActivity{
    private CardView developerOne;
    private CardView developerTwo;
    private int preX;
    private int preY;
    private int startTop;
    private int startLeft;
    private int startRight;
    private int startBottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developer);
        developerOne=findViewById(R.id.developer_one);
        developerOne.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            int action=event.getAction();
            int x=(int)event.getX();
            int y=(int)event.getY();
            int offsetX=0;
            int offsetY=0;
            switch (action){
                case MotionEvent.ACTION_DOWN:
                    startTop=v.getTop();
                    startLeft=v.getLeft();
                    startRight=v.getRight();
                    startBottom=v.getBottom();
                    preX=x;
                    preY=y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    offsetX=x-preX;
                    offsetY=y-preY;
                    if(Math.abs(offsetX)>40)
                        offsetX=offsetX>0?40:-40;
                    if(Math.abs(offsetY)>40)
                        offsetY=offsetY>0?40:-40;
                    v.layout(startLeft+offsetX,startTop+offsetY,startRight+offsetX,startBottom+offsetY);
                    break;
                case MotionEvent.ACTION_UP:
                    v.layout(startLeft,startTop,startRight,startBottom);
                    break;
            }
            return true;
        }});
        developerTwo=findViewById(R.id.developer_two);
        developerTwo.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action=event.getAction();
                int x=(int)event.getX();
                int y=(int)event.getY();
                int offsetX=0;
                int offsetY=0;
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        startTop=v.getTop();
                        startLeft=v.getLeft();
                        startRight=v.getRight();
                        startBottom=v.getBottom();
                        preX=x;
                        preY=y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        offsetX=x-preX;
                        offsetY=y-preY;
                        if(Math.abs(offsetX)>40)
                            offsetX=offsetX>0?40:-40;
                        if(Math.abs(offsetY)>40)
                            offsetY=offsetY>0?40:-40;
                        v.layout(startLeft+offsetX,startTop+offsetY,startRight+offsetX,startBottom+offsetY);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.layout(startLeft,startTop,startRight,startBottom);
                        break;
                }
                return true;
            }});
    }
}