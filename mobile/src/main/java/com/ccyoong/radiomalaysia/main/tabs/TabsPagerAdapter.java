package com.ccyoong.radiomalaysia.main.tabs;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ccyoong.radiomalaysia.R;
import com.ccyoong.radiomalaysia.main.tabs.favourite.FavouriteFragment;
import com.ccyoong.radiomalaysia.main.tabs.full.FullStationFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.all, R.string.favourite};
    private final Context mContext;

//    private Map<Integer, Fragment> pages = new HashMap<>();

    public TabsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        Fragment frag = null;
//        if (pages.get(position) == null) {
        switch (position) {
            case 0:
                frag = new FullStationFragment();
                break;
            case 1:
                frag = new FavouriteFragment();
                break;

        }
//            pages.put(position, frag);
//        } else {
//            frag = pages.get(position);
//        }
        return frag;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }


}