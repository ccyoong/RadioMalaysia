package com.ccyoong.radiomalaysia.main;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ccyoong.radiomalaysia.PlayerHandler;
import com.ccyoong.radiomalaysia.R;
import com.ccyoong.radiomalaysia.StationController;
import com.ccyoong.radiomalaysia.data.Station;
import com.ccyoong.radiomalaysia.main.tabs.TabsPagerAdapter;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

//    NavController nc;

    private PlayerHandler playerHandler;
    private MediaSessionCompat mSession;
    private PlayerControlView playerControlView;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private ImageButton playButton;
    private ImageButton pauseButton;
    private String lastMediaId = "melody";

    private ImageView exoIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataBindingUtil.setContentView(this, R.layout.activity_main);

        nextButton = findViewById(R.id.cuz_exo_next);
        prevButton = findViewById(R.id.cuz_exo_prev);
        playButton = findViewById(R.id.cuz_exo_play);
        playButton.setVisibility(View.GONE);
        pauseButton = findViewById(R.id.cuz_exo_pause);
        exoIcon = findViewById(R.id.cuz_exo_icon);

        TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(this, getSupportFragmentManager());
        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(tabsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                if (viewPager.getCurrentItem() == 1) {
                    Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + viewPager.getCurrentItem());
                    getSupportFragmentManager().beginTransaction().detach(page).attach(page).commit();
                }

            }
        });


        playerControlView = findViewById(R.id.player_control_view);
        playerControlView.setShowShuffleButton(false);
        playerControlView.setShowMultiWindowTimeBar(false);


//        nc = Navigation.findNavController(this, R.id.mainNavHostFragment);
//        NavigationUI.setupActionBarWithNavController(this, nc);


        mSession = new MediaSessionCompat(this, "MyMusicService");
        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        // cuz
        mSession.setActive(true);
        playerHandler = new PlayerHandler(this, mSession);
        playerControlView.setPlayer(playerHandler.getPlayer());
        play(StationController.getStationById(lastMediaId));

    }

    private void changeExoIcon(String mediaId) {
        Drawable icon = getResources().getDrawable(getResources().getIdentifier("_" + mediaId, "drawable", getPackageName()));
        exoIcon.setImageDrawable(icon);

    }

    @Override
    protected void onStart() {
        super.onStart();
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play(StationController.getNextStation(lastMediaId));
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play(StationController.getPreviousStation(lastMediaId));
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play(StationController.getStationById(lastMediaId));
                pauseButton.setVisibility(View.VISIBLE);
                playButton.setVisibility(View.GONE);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerHandler.pausePlaying();
                playButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.GONE);
            }
        });
    }

    public void play(Station station) {
        mSession.setActive(true);
        lastMediaId = station.getId();
        playerHandler.startPlaying(station);
        changeExoIcon(lastMediaId);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSession.release();
        nextButton.setOnClickListener(null);
        prevButton.setOnClickListener(null);
        playButton.setOnClickListener(null);
        pauseButton.setOnClickListener(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        playerHandler.releasePlayer();
    }

}
