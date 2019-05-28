package com.ccyoong.radiomalaysia;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a MediaBrowser through a service. It exposes the media library to a browsing
 * client, through the onGetRoot and onLoadChildren methods. It also creates a MediaSession and
 * exposes it through its MediaSession.Token, which allows the client to create a MediaController
 * that connects to and send control commands to the MediaSession remotely. This is useful for
 * user interfaces that need to interact with your media session, like Android Auto. You can
 * (should) also use the same service from your app's UI, which gives a seamless playback
 * experience to the user.
 * <p>
 * To implement a MediaBrowserService, you need to:
 *
 * <ul>
 *
 * <li> Extend {@link MediaBrowserServiceCompat}, implementing the media browsing
 * related methods {@link MediaBrowserServiceCompat#onGetRoot} and
 * {@link MediaBrowserServiceCompat#onLoadChildren};
 * <li> In onCreate, start a new {@link MediaSessionCompat} and notify its parent
 * with the session's token {@link MediaBrowserServiceCompat#setSessionToken};
 *
 * <li> Set a callback on the {@link MediaSessionCompat#setCallback(MediaSessionCompat.Callback)}.
 * The callback will receive all the user's actions, like play, pause, etc;
 *
 * <li> Handle all the actual music playing using any method your app prefers (for example,
 * {@link android.media.MediaPlayer})
 *
 * <li> Update playbackState, "now playing" metadata and queue, using MediaSession proper methods
 * {@link MediaSessionCompat#setPlaybackState(android.support.v4.media.session.PlaybackStateCompat)}
 * {@link MediaSessionCompat#setMetadata(android.support.v4.media.MediaMetadataCompat)} and
 * {@link MediaSessionCompat#setQueue(List)})
 *
 * <li> Declare and export the service in AndroidManifest with an intent receiver for the action
 * android.media.browse.MediaBrowserService
 *
 * </ul>
 * <p>
 * To make your app compatible with Android Auto, you also need to:
 *
 * <ul>
 *
 * <li> Declare a meta-data tag in AndroidManifest.xml linking to a xml resource
 * with a &lt;automotiveApp&gt; root element. For a media app, this must include
 * an &lt;uses name="media"/&gt; element as a child.
 * For example, in AndroidManifest.xml:
 * &lt;meta-data android:name="com.google.android.gms.car.application"
 * android:resource="@xml/automotive_app_desc"/&gt;
 * And in res/values/automotive_app_desc.xml:
 * &lt;automotiveApp&gt;
 * &lt;uses name="media"/&gt;
 * &lt;/automotiveApp&gt;
 *
 * </ul>
 */
public class MyMusicService extends MediaBrowserServiceCompat implements AudioManager.OnAudioFocusChangeListener {
    /**
     * Declares that ContentStyle is supported
     */
    public static final String CONTENT_STYLE_SUPPORTED = "android.media.browse.CONTENT_STYLE_SUPPORTED";

    /**
     * Bundle extra indicating the presentation hint for playable media items.
     */
    public static final String CONTENT_STYLE_PLAYABLE_HINT = "android.media.browse.CONTENT_STYLE_PLAYABLE_HINT";

    /**
     * Bundle extra indicating the presentation hint for browsable media items.
     */
    public static final String CONTENT_STYLE_BROWSABLE_HINT = "android.media.browse.CONTENT_STYLE_BROWSABLE_HINT";

    /**
     * Specifies the corresponding items should be presented as lists.
     */
    public static final int CONTENT_STYLE_LIST_ITEM_HINT_VALUE = 1;

    /**
     * Specifies that the corresponding items should be presented as grids.
     */
    public static final int CONTENT_STYLE_GRID_ITEM_HINT_VALUE = 2;


    private MediaSessionCompat mSession;
    private AudioManager audioManager;
    private AudioFocusRequest audioFocusRequest;

    private PlayerHandler playerHandler;

    private BroadcastReceiver connectedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getStringExtra("media_connection_status").equals("media_connected")) {
                playerHandler.stopPlaying();
            }

        }
    };
    private BroadcastReceiver onAudioNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
                playerHandler.stopPlaying();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        Intent sessionIntent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        PendingIntent sessionActivityPendingIntent = PendingIntent.getActivity(this, 0, sessionIntent, 0);

        mSession = new MediaSessionCompat(this, "MyMusicService");
        setSessionToken(mSession.getSessionToken());
        mSession.setCallback(new MediaSessionCallback());
        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        // cuz
        mSession.setSessionActivity(sessionActivityPendingIntent);
        mSession.setActive(true);
        playerHandler = new PlayerHandler(this, mSession);
        playerHandler.createPlayer();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        registerReceiver(connectedReceiver, new IntentFilter("com.google.android.gms.car.media.STATUS"));
        registerReceiver(onAudioNoisyReceiver, new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY));

    }

    @Override
    public void onDestroy() {
        unregisterReceiver(connectedReceiver);
        unregisterReceiver(onAudioNoisyReceiver);
        playerHandler.releasePlayer();
        mSession.release();
    }

    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName,
                                 int clientUid,
                                 Bundle rootHints) {
        Bundle extras = new Bundle();
        extras.putBoolean(CONTENT_STYLE_SUPPORTED, true);
        extras.putInt(CONTENT_STYLE_BROWSABLE_HINT, CONTENT_STYLE_GRID_ITEM_HINT_VALUE);
        extras.putInt(CONTENT_STYLE_PLAYABLE_HINT, CONTENT_STYLE_LIST_ITEM_HINT_VALUE);
        return new BrowserRoot("root", extras);
    }

    @Override
    public void onLoadChildren(@NonNull final String parentMediaId,
                               @NonNull final Result<List<MediaItem>> result) {


        List<Station> stations = StationController.getStations();
        List<MediaItem> miList = new ArrayList<>();

        for (Station station : stations) {
            MediaDescriptionCompat.Builder bob = new MediaDescriptionCompat.Builder();
            bob.setDescription(station.getSlogan());
            bob.setMediaId(station.getId());
            bob.setMediaUri(Uri.parse(station.getOnlineUrl()));
            bob.setTitle(station.getName());

            int id = getResources().getIdentifier("_" + station.getId(), "drawable", getPackageName());
            if (id != 0x00000000) {
                bob.setIconBitmap(BitmapFactory.decodeResource(getResources(), id));
            }
            MediaItem mi = new MediaItem(bob.build(), MediaItem.FLAG_PLAYABLE);
            miList.add(mi);
        }

        result.sendResult(miList);
    }

    private int getAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setOnAudioFocusChangeListener(this)
                    .setAcceptsDelayedFocusGain(false)
                    .build();

            return audioManager.requestAudioFocus(audioFocusRequest);
        } else {
            return audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
    }

    private void abandonAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (audioFocusRequest != null) {
                audioManager.abandonAudioFocusRequest(audioFocusRequest);
            }
        } else {
            audioManager.abandonAudioFocus(this);
        }
    }


    private final class MediaSessionCallback extends MediaSessionCompat.Callback {

        String lastMediaId = "";


        private void play(Station station) {
            if (getAudioFocus() == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                mSession.setActive(true);
                lastMediaId = station.getId();
                playerHandler.startPlaying(station);
            }
        }


        @Override
        public void onPlay() {
            playerHandler.startPlaying(StationController.getStationById(lastMediaId));
        }

        @Override
        public void onSkipToQueueItem(long queueId) {
            System.out.println("onSkipToQueueItem");
        }

        @Override
        public void onSeekTo(long position) {
            System.out.println("onSeekTo");
        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
            play(StationController.getStationById(mediaId));
        }

        @Override
        public void onPause() {
            playerHandler.stopPlaying();
        }

        @Override
        public void onStop() {
            abandonAudioFocus();
            mSession.setActive(false);
            playerHandler.stopPlaying();
            stopSelf();
        }

        @Override
        public void onSkipToNext() {
            play(StationController.getNextStation(lastMediaId));
        }

        @Override
        public void onSkipToPrevious() {
            play(StationController.getPreviousStation(lastMediaId));
        }

        @Override
        public void onCustomAction(String action, Bundle extras) {
            System.out.println("onCustomAction");
        }

        @Override
        public void onPlayFromSearch(final String query, final Bundle extras) {
            System.out.println("onPlayFromSearch");
        }
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                playerHandler.continuePlaying();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                playerHandler.pausePlaying();
                break;
        }
    }
}
