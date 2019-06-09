package com.ccyoong.radiomalaysia.main;

import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.ccyoong.radiomalaysia.PlayerHandler;
import com.ccyoong.radiomalaysia.R;
import com.ccyoong.radiomalaysia.Station;
import com.ccyoong.radiomalaysia.StationController;
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
    private String lastMediaId = "melody";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataBindingUtil.setContentView(this, R.layout.activity_main);

        nextButton = findViewById(R.id.cuz_exo_next);
        prevButton = findViewById(R.id.cuz_exo_prev);

        TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(tabsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


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
        playerHandler.startPlaying(StationController.getStationById(lastMediaId));
    }

    @Override
    protected void onStart() {
        super.onStart();
        nextButton.setEnabled(true);
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
    }

    public void play(Station station) {
        mSession.setActive(true);
        lastMediaId = station.getId();
        playerHandler.startPlaying(station);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSession.release();
        nextButton.setOnClickListener(null);
        prevButton.setOnClickListener(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        playerHandler.releasePlayer();
    }

}
