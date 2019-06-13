package com.ccyoong.radiomalaysia.main.tabs.full;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        LinearLayout.LayoutParams outerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        outerLayoutParams.setMargins(0, 5, 0, 5);

        int itemHeight = (int) (metrics.heightPixels * 0.15);

        LinearLayout.LayoutParams iconLayoutParams = new LinearLayout.LayoutParams((int) (metrics.widthPixels * 0.25), itemHeight, 3);

        LinearLayout.LayoutParams infoLayoutParams = new LinearLayout.LayoutParams(metrics.widthPixels - (int) (metrics.widthPixels * 0.15), itemHeight, 7);

        for (int i = 0; i < allStation.size(); i++) {

            // Outer Linear Layout
            LinearLayout outerLayout = new LinearLayout(getContext());
            outerLayout.setOrientation(LinearLayout.HORIZONTAL);
            outerLayout.setLayoutParams(outerLayoutParams);
            outerLayout.setClickable(true);
            outerLayout.setId(i);
            outerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.play(allStation.get(view.getId()));

                }
            });
            /*outerLayout.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                            v.invalidate();
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            v.getBackground().clearColorFilter();
                            v.invalidate();
                            break;
                        }
                    }
                    return false;
                }

            });*/
            mainLinearLayout.addView(outerLayout);

            // Station Icon
            Drawable img = getResources().getDrawable(getResources().getIdentifier("_" + allStation.get(i).getId(), "drawable", getContext().getPackageName()));
            ImageView icon = new ImageView(getContext());
            icon.setImageDrawable(img);
            icon.setLayoutParams(iconLayoutParams);
            icon.setScaleType(ImageView.ScaleType.FIT_CENTER);

            outerLayout.addView(icon);

            // Linear Layout to put info
            LinearLayout infoLayout = new LinearLayout(getContext());
            infoLayout.setOrientation(LinearLayout.VERTICAL);
            infoLayout.setLayoutParams(infoLayoutParams);
            outerLayout.addView(infoLayout);

            // Station Title
            TextView stationTitle = new TextView(getContext());
            stationTitle.setText(allStation.get(i).getName());
            stationTitle.setPadding(30, 10, 10, 10);
            stationTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            infoLayout.addView(stationTitle);
        }

    }
}
