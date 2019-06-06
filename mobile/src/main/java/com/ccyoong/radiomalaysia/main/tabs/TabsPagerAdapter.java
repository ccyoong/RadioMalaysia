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
import com.ccyoong.radiomalaysia.main.tabs.recent.RecentFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.all, R.string.favourite, R.string.recent};
    private final Context mContext;

    public TabsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        Fragment frag = null;

        switch (position) {
            case 0:
                frag = new FullStationFragment();
                break;
            case 1:
                frag = new FavouriteFragment();
                break;
            case 2:
                frag = new RecentFragment();
                break;
        }

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