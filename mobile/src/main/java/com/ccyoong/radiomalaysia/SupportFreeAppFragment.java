package com.ccyoong.radiomalaysia;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class SupportFreeAppFragment extends Fragment {

    Button shareFreeAppButton;

    public SupportFreeAppFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support_free_app, container, false);
        shareFreeAppButton = view.findViewById(R.id.share_free_app_button);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        shareFreeAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(getShareFreeAppIntent());
            }
        });

        // no app can handle to intent
        if (null == getShareFreeAppIntent().resolveActivity(getActivity().getPackageManager())) {
            shareFreeAppButton.setVisibility(View.GONE);
        }
    }


    private Intent getShareFreeAppIntent() {
        return ShareCompat.IntentBuilder.
                from(getActivity()).setText("share free app")
                .setType("text/plain")
                .getIntent();
    }
}
