package com.ccyoong.radiomalaysia;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment {


    private TextView stationIdTextView;

    public PlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_player, container, false);

        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PlayerFragmentArgs args = PlayerFragmentArgs.fromBundle(getArguments());
        stationIdTextView = view.findViewById(R.id.stationIdTextView);
        stationIdTextView.setText(args.getStationId());
    }

}
