package com.testnativemodule;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by i8e4 on 12/13/17.
 */

public class Alarm extends WakefulBroadcastReceiver {
    public static final String ACTION_START_ALARM = "ACTION_START_ALARM";

    MediaPlayer mMediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (ACTION_START_ALARM.equals(intent.getAction())) {
                Log.e("AlarmManagaer", "Action fired successfully");
                Toast.makeText(context, "Alarm...", Toast.LENGTH_LONG).show();
                try {
                    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.setDataSource(context, uri);
                    final AudioManager audioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
                    if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
                        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                        mMediaPlayer.setLooping(true);
                        mMediaPlayer.prepare();
                        mMediaPlayer.start();
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        mMediaPlayer.stop();
                                    }
                                },
                                30000);
                    }
                } catch (Exception e) {
                    try {
                        throw e;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }
}
