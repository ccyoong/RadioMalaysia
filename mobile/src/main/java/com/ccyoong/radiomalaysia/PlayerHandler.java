package com.ccyoong.radiomalaysia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class PlayerHandler extends Player.DefaultEventListener {

    Context context;

    MediaSessionCompat session;
    private SimpleExoPlayer player = null;


    private PlayerHandler() {
    }

    PlayerHandler(Context context, MediaSessionCompat session) {
        this.context = context;
        this.session = session;
    }

    public void createPlayer() {
        setPlaybackState(PlaybackStateCompat.STATE_STOPPED);
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(context),
                new DefaultTrackSelector(),
                new DefaultLoadControl());
        player.addListener(this);
    }

    public SimpleExoPlayer getPlayer(){
        if(this.player==null){
            createPlayer();
        }
        return this.player;
    }

    public void startPlaying(Station station) {
        DefaultHttpDataSourceFactory defaultHttpDataSourceFactory =
                new DefaultHttpDataSourceFactory("ua");

        Uri uri = Uri.parse(station.getOnlineUrl());
        MediaSource mediaSource = new ExtractorMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(uri);
        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
        builder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, station.getName());
        builder.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, station.getName());
        builder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, "ALBUM");
        builder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, station.getSlogan());
        builder.putString(MediaMetadataCompat.METADATA_KEY_GENRE, "GENRE");
        int id = context.getResources().getIdentifier("_" + station.getId(), "drawable", context.getPackageName());
        if (id != 0x00000000) {
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), id);
            builder.putBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, bm);
            builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bm);
        }
        session.setMetadata(builder.build());
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
    }


    private void setPlaybackState(int state) {
        PlaybackStateCompat.Builder builder = new PlaybackStateCompat.Builder();
        builder.setState(state, 0, 0f);
        builder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS | PlaybackStateCompat.ACTION_SKIP_TO_NEXT);
        session.setPlaybackState(builder.build());
    }

    public void continuePlaying() {
        player.setPlayWhenReady(true);
    }

    public void pausePlaying() {
        player.setPlayWhenReady(false);
    }

    public void stopPlaying() {
        player.stop();
    }

    public void releasePlayer() {
        setPlaybackState(PlaybackStateCompat.STATE_STOPPED);
        player.removeListener(this);
        player.release();
        player = null;
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        switch (playbackState) {

            case Player.STATE_READY:

                if (playWhenReady) {
                    setPlaybackState(PlaybackStateCompat.STATE_PLAYING);
                } else {
                    setPlaybackState(PlaybackStateCompat.STATE_PAUSED);
                }
                break;

            case Player.STATE_BUFFERING:
                setPlaybackState(PlaybackStateCompat.STATE_BUFFERING);
                break;

            case Player.STATE_ENDED:
                setPlaybackState(PlaybackStateCompat.STATE_STOPPED);
                break;

            case Player.STATE_IDLE:
                setPlaybackState(PlaybackStateCompat.STATE_PAUSED);
                break;
            default:
                setPlaybackState(PlaybackStateCompat.STATE_NONE);
                break;
        }
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        if (isLoading) {
            setPlaybackState(PlaybackStateCompat.STATE_BUFFERING);
        } else {
            setPlaybackState(PlaybackStateCompat.STATE_PLAYING);
        }
    }
}
