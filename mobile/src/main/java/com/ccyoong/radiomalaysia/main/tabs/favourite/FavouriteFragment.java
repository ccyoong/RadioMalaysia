package com.ccyoong.radiomalaysia.main.tabs.favourite;


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
import com.ccyoong.radiomalaysia.StationController;
import com.ccyoong.radiomalaysia.data.FavStation;
import com.ccyoong.radiomalaysia.data.Station;
import com.ccyoong.radiomalaysia.main.MainActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends Fragment {


    private LinearLayout favLinearLayout;
    private ImageView exoIcon;


    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_full_station, container, false);
        favLinearLayout = rootView.findViewById(R.id.mainLinearLayout);
        exoIcon = getActivity().findViewById(R.id.cuz_exo_icon);
        constructLayout();
        return rootView;
    }

    private void constructLayout() {

        final List<FavStation> allFavStation = StationController.getFavStations();

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        LinearLayout.LayoutParams outerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        outerLayoutParams.setMargins(10, 5, 0, 5);

        int itemHeight = (int) (metrics.heightPixels * 0.15);

        LinearLayout.LayoutParams iconLayoutParams = new LinearLayout.LayoutParams((int) (metrics.widthPixels * 0.25), itemHeight, 3);

        LinearLayout.LayoutParams infoLayoutParams = new LinearLayout.LayoutParams(metrics.widthPixels - (int) (metrics.widthPixels * 0.15), itemHeight, 7);

        for (int i = 0; i < allFavStation.size(); i++) {
            FavStation selectedStation = allFavStation.get(i);
            Station station = StationController.getStationById(selectedStation.getStationId());

            // Outer Linear Layout
            LinearLayout outerLayout = new LinearLayout(getContext());
            outerLayout.setOrientation(LinearLayout.HORIZONTAL);
            outerLayout.setLayoutParams(outerLayoutParams);
            outerLayout.setClickable(true);
            outerLayout.setFocusableInTouchMode(true);
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            outerLayout.setBackgroundResource(outValue.resourceId);
            outerLayout.setId(i);
            outerLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        v.setBackgroundColor(0xFF7947FF);
                        FavStation selectedStation = allFavStation.get(v.getId());
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.play(StationController.getStationById(selectedStation.getStationId()));
                    } else {
                        v.setBackgroundColor(0);
                    }
                }
            });

            favLinearLayout.addView(outerLayout);

            // Station Icon
            Drawable img = getResources().getDrawable(getResources().getIdentifier("_" + station.getId(), "drawable", getContext().getPackageName()));
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
            stationTitle.setText(station.getName());
            stationTitle.setPadding(30, 10, 10, 10);
            stationTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            infoLayout.addView(stationTitle);

            // Station Details
            TextView stationDtl = new TextView(getContext());
            stationDtl.setText(station.getSlogan());
            stationDtl.setPadding(30, 10, 10, 10);
            stationDtl.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            infoLayout.addView(stationDtl);

        }

        // add blank for the scrolling

        LinearLayout.LayoutParams blankLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight, 1);
        blankLayoutParams.setMargins(10, 5, 0, 5);


        LinearLayout blankLayout = new LinearLayout(getContext());
        blankLayout.setOrientation(LinearLayout.HORIZONTAL);
        blankLayout.setLayoutParams(blankLayoutParams);
        favLinearLayout.addView(blankLayout);

    }

}
