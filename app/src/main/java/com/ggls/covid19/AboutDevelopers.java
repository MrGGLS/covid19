package com.ggls.covid19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.ImageView;

public class AboutDevelopers extends AppCompatActivity {
    DeveloperOneBriefFragment developerOneBriefFragment;
    DeveloperOneDetailsFragment developerOneDetailsFragment;
    CardView developOneBriefCard;
//    CardView developTwoBriefCard;
    ImageView developOneProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developer);
        developerOneBriefFragment=new DeveloperOneBriefFragment();
        developOneProfile=findViewById(R.id.developer_one_profile);
        developerOneDetailsFragment=new DeveloperOneDetailsFragment();
        developOneBriefCard=findViewById(R.id.developer_one);
//        developTwoBriefCard=findViewById(R.id.developer_two);
        developOneBriefCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}