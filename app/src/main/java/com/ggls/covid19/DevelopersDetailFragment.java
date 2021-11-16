package com.ggls.covid19;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class DevelopersDetailFragment extends Fragment {
    private CardView developerOne;
    private CardView developerTwo;
    private int preX;
    private int preY;
    private int startTop;
    private int startLeft;
    private int startRight;
    private int startBottom;
    public DevelopersDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transition transition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.image_transform);
        setSharedElementEnterTransition(transition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_developers_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        ImageView dev1ProfileS = view.findViewById(R.id.dev1_profile_s);
        ViewCompat.setTransitionName(dev1ProfileS, "dev1_profile");
        ImageView dev2ProfileS = view.findViewById(R.id.dev2_profile_s);
        ViewCompat.setTransitionName(dev2ProfileS, "dev2_profile");

        developerOne=getActivity().findViewById(R.id.developer_one);
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
        developerTwo=getActivity().findViewById(R.id.developer_two);
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