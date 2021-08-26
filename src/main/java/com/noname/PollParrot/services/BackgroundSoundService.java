/*
 * A class to play background music
 * from: https://stackoverflow.com/questions/46838443/play-music-with-background-service
 */

package com.noname.PollParrot.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.noname.PollParrot.R;

public class BackgroundSoundService extends Service {

    private static final String TAG = "BackgroundSoundService";
    MediaPlayer player;
    int length;

    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "onBind()" );
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, R.raw.ukulele);
        player.setLooping(true); // Set looping
        player.setVolume(0.25f,0.25f);
        Log.i(TAG, "onCreate() , service started...");

    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        //from: https://stackoverflow.com/questions/3855151/how-to-resume-the-mediaplayer
        if (intent.getAction().equals("PLAY")) {
            player.seekTo(length);
            player.start();
        }
        if (intent.getAction().equals("PAUSE")) {
            player.pause();
            length = player.getCurrentPosition();
        }
        return Service.START_STICKY;
    }

    public IBinder onUnBind(Intent arg0) {
        Log.i(TAG, "onUnBind()");
        return null;
    }
    public void onStop() {
        Log.i(TAG, "onStop()");
    }
    public void onPause() {
        if(player!=null && player.isPlaying()){
            player.pause();
        }
        Log.i(TAG, "onPause()");
    }
    @Override
    public void onDestroy() {
        player.stop();
        player.release();
        Log.i(TAG, "onCreate() , service stopped...");
    }

    @Override
    public void onLowMemory() {
        Log.i(TAG, "onLowMemory()");
    }
}