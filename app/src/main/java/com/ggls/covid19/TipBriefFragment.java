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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TipBriefFragment extends Fragment {

    public TipBriefFragment() {
        // Required empty public constructor
    }

    public static TipBriefFragment newInstance() {
        return new TipBriefFragment();
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
        return inflater.inflate(R.layout.fragment_tip_brief, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CardView card0=view.findViewById(R.id.wash_hand_card);
        CardView card1=view.findViewById(R.id.drink_water_card);
        CardView card2=view.findViewById(R.id.dance_card);
        CardView card3=view.findViewById(R.id.mask_card);
        CardView card4=view.findViewById(R.id.stay_home_card);
        card0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipDetailFragment tipDetailFragment=TipDetailFragment.newInstance(0);
                ImageView articleImage=view.findViewById(R.id.wash_hand_img);
                getFragmentManager()
                        .beginTransaction()
                        .addSharedElement(articleImage, ViewCompat.getTransitionName(articleImage))
                        .addToBackStack(TipBriefFragment.class.getSimpleName())
                        .replace(R.id.guide_container,tipDetailFragment)
                        .commit();
            }
        });
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipDetailFragment tipDetailFragment=TipDetailFragment.newInstance(1);
                ImageView articleImage=view.findViewById(R.id.drink_water_img);
                getFragmentManager()
                        .beginTransaction()
                        .addSharedElement(articleImage, ViewCompat.getTransitionName(articleImage))
                        .addToBackStack(TipBriefFragment.class.getSimpleName())
                        .replace(R.id.guide_container,tipDetailFragment)
                        .commit();
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipDetailFragment tipDetailFragment=TipDetailFragment.newInstance(2);
                ImageView articleImage=view.findViewById(R.id.dance_img);
                getFragmentManager()
                        .beginTransaction()
                        .addSharedElement(articleImage, ViewCompat.getTransitionName(articleImage))
                        .addToBackStack(TipBriefFragment.class.getSimpleName())
                        .replace(R.id.guide_container,tipDetailFragment)
                        .commit();
            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipDetailFragment tipDetailFragment=TipDetailFragment.newInstance(3);
                ImageView articleImage=view.findViewById(R.id.mask_img);
                getFragmentManager()
                        .beginTransaction()
                        .addSharedElement(articleImage, ViewCompat.getTransitionName(articleImage))
                        .addToBackStack(TipBriefFragment.class.getSimpleName())
                        .replace(R.id.guide_container,tipDetailFragment)
                        .commit();
            }
        });
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipDetailFragment tipDetailFragment=TipDetailFragment.newInstance(4);
                ImageView articleImage=view.findViewById(R.id.stay_home_img);
                getFragmentManager()
                        .beginTransaction()
                        .addSharedElement(articleImage, ViewCompat.getTransitionName(articleImage))
                        .addToBackStack(TipBriefFragment.class.getSimpleName())
                        .replace(R.id.guide_container,tipDetailFragment)
                        .commit();
            }
        });

    }
}