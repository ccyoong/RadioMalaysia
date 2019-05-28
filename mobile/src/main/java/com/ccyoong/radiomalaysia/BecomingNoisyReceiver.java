package com.ccyoong.radiomalaysia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;

class BecomingNoisyReceiver extends BroadcastReceiver {
    private IntentFilter noisyIntentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    private boolean registered = false;
    Context context;
    MediaSessionCompat.Token sessionToken;
    private MediaControllerCompat controller;

    public BecomingNoisyReceiver(Context context, MediaSessionCompat.Token sessionToken) {
        this.context = context;
        this.sessionToken = sessionToken;
        try {
            controller = new MediaControllerCompat(context, sessionToken);
        } catch (Exception e) {
        }

    }

    private void register() {
        if (!registered) {
            context.registerReceiver(this, noisyIntentFilter);
            registered = true;
        }
    }

    private void unregister() {
        if (registered) {
            context.unregisterReceiver(this);
            registered = false;
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
            controller.getTransportControls().pause();
        }
    }
}
