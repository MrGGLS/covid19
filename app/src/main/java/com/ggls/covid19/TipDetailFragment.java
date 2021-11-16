package com.ggls.covid19;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TipDetailFragment extends Fragment {
    private static int imageId;
    public TipDetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TipDetailFragment newInstance(int imageID) {
        imageId=imageID;
        return new TipDetailFragment();
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
        return inflater.inflate(R.layout.fragment_tip_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView titleImage=view.findViewById(R.id.article_image);
        TextView articleTitle=view.findViewById(R.id.article_title);
        TextView articleText=view.findViewById(R.id.article_text);
        switch (imageId){
            case 0:
                titleImage.setImageResource(R.drawable.washhand);
                ViewCompat.setTransitionName(titleImage,"washhand_tran");
                articleTitle.setText("如何正确地洗手？");
                articleText.setText(R.string.washhand_article);
                break;
            case 1:
                titleImage.setImageResource(R.drawable.drinkwater);
                ViewCompat.setTransitionName(titleImage,"drinkwater_tran");
                articleTitle.setText("如何正确地喝水？");
                articleText.setText(R.string.drinkwater_article);
                break;
            case 2:
                titleImage.setImageResource(R.drawable.dance);
                ViewCompat.setTransitionName(titleImage,"dance_tran");
                articleTitle.setText("如何正确地跑步？");
                articleText.setText(R.string.dance_article);
                break;
            case 3:
                titleImage.setImageResource(R.drawable.mask);
                ViewCompat.setTransitionName(titleImage,"mask_tran");
                articleTitle.setText("如何正确地戴口罩？");
                articleText.setText(R.string.mask_article);
                break;
            case 4:
                titleImage.setImageResource(R.drawable.stayhome);
                ViewCompat.setTransitionName(titleImage,"stayhome_tran");
                articleTitle.setText("如何正确地远离人群？");
                articleText.setText(R.string.stayhome_article);
                break;
        }
    }
}