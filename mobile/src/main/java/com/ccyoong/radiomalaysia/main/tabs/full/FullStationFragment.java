package com.ccyoong.radiomalaysia.main.tabs.full;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.ccyoong.radiomalaysia.R;
import com.ccyoong.radiomalaysia.Station;
import com.ccyoong.radiomalaysia.StationController;
import com.ccyoong.radiomalaysia.main.MainActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FullStationFragment extends Fragment {

    LinearLayout mainLinearLayout;

    public FullStationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_full_station, container, false);

        mainLinearLayout = rootView.findViewById(R.id.mainLinearLayout);
        constructLayout();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void constructLayout() {

        final List<Station> allStation = StationController.getStations();

        LinearLayout ll = null;
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        float dp = 100f;
        float fpixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
        int pixels = Math.round(fpixels);
        LinearLayout.LayoutParams llimp = new LinearLayout.LayoutParams(pixels, pixels);

        for (int i = 0; i < allStation.size(); i++) {
            if (i % 4 == 0) {
                ll = new LinearLayout(getContext());
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setLayoutParams(llp);
                mainLinearLayout.addView(ll);
            }
            ImageButton imageButton = new ImageButton(getContext());
            Drawable img = getResources().getDrawable(getResources().getIdentifier("_" + allStation.get(i).getId(), "drawable", getContext().getPackageName()));
            imageButton.setImageDrawable(img);
            imageButton.setLayoutParams(llimp);
            imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageButton.setId(i);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.play(allStation.get(view.getId()));
                }
            });
            ll.addView(imageButton);
        }


    }
}
