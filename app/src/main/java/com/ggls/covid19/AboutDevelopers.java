package com.ggls.covid19;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AboutDevelopers extends AppCompatActivity{
    private DevelopersBriefFragment devFragment;
    private DevelopersDetailFragment devDetailFragment;
    private Button button;
    private ImageView dev1Profile;
    private ImageView dev2Profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developer);
        button=findViewById(R.id.intro_title);
        dev1Profile=findViewById(R.id.dev1_profile);
        dev2Profile=findViewById(R.id.dev2_profile);
        devFragment=new DevelopersBriefFragment();
        dev1Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devDetailFragment=new DevelopersDetailFragment();
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .addSharedElement(dev1Profile, "dev1_profile_s")
                        .addSharedElement(dev2Profile,"dev2_profile_s")
                        .replace(R.id.dev_container, devDetailFragment)
                        .addToBackStack("enter")
                        .commit();
            }
        });
        dev2Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devDetailFragment=new DevelopersDetailFragment();
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .addSharedElement(dev1Profile, "dev1_profile_s")
                        .addSharedElement(dev2Profile,"dev2_profile_s")
                        .replace(R.id.dev_container, devDetailFragment)
                        .addToBackStack("enter")
                        .commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().popBackStack();
        super.onBackPressed();
    }
}
