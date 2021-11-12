package com.ggls.covid19;

import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class DeveloperOneBriefFragment extends Fragment implements View.OnClickListener {
    ImageView profile_one;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.developer_one_brief,container,false);
        return view;
    }

    @Override
    public void onClick(View v) {
        setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.image_transform));
        setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.explode));

        // Create new fragment to add (Fragment B)
        Fragment fragment = new DeveloperOneDetailsFragment();
        fragment.setSharedElementEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.image_transform));
        fragment.setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.explode));

        // Our shared element (in Fragment A)
        profile_one  = (ImageView) getActivity().findViewById(R.id.developer_one_profile);

        // Add Fragment B
        FragmentTransaction ft = getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("transaction")
                .addSharedElement(profile_one, "profile_one");
        ft.commit();
    }
}
