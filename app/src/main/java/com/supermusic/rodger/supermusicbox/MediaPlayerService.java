package com.supermusic.rodger.supermusicbox;

import android.app.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import android.util.Log;

/**
 * Created by reborn on 2017/2/7.
 */

public class MediaPlayerService extends Service implements OnPreparedListener, OnErrorListener, OnCompletionListener ,OnAudioFocusChangeListener {

    public static final String
    ACTION_PLAY = "tw.android.mediaplayer.action.PLAY";

    public static final String ACTION_PAUSE = "tw.android.mediaplayer.actionl.PAUSE";
    public static final String ACTION_SET_REPEAT = "tw.android.mediaplayer.action.SET_REPEAT";

    private MediaPlayer mMediaPlayer = null;

    private boolean mbIsInitial = true;

    // 0 : none , 1 : Loop one , 2 : Loop all.
    private int iRepeatType = 0;



    @Override
    public void onCreate() {
        super.onCreate();

        Log.v("SuperMusicBox","onCreat 0");
        mMediaPlayer = new MediaPlayer();

        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.v("SuperMusicBox","onDestroy");

        mMediaPlayer.release();
        mMediaPlayer = null;

        stopForeground(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals(ACTION_PLAY)) {

            if (mbIsInitial) {

                Log.v("SuperMusicBox","onStartCommand  1");

                startPlay();
                mbIsInitial = false;

                Log.v("SuperMusicBox","onStartCommand  2");
            }else {

                Log.v("SuperMusicBox","onStartCommand  3");
                startPlay();
                Log.v("SuperMusicBox","onStartCommand 4");
            }
        }else if (intent.getAction().equals(ACTION_PAUSE)) {
            mMediaPlayer.pause();
        }else if (intent.getAction().equals(ACTION_SET_REPEAT)) {

            iRepeatType++;
            if (iRepeatType >= 3)
                iRepeatType = 0;

            if (iRepeatType == 1)
                mMediaPlayer.setLooping(true);
            else
                mMediaPlayer.setLooping(false);

            Log.v("SuperMusicBox","loop " + mMediaPlayer.isLooping());
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    // Called when the end of a media source is reached during playback.
    // mp :	MediaPlayer: the MediaPlayer that reached the end of the file
    @Override
    public void onCompletion(MediaPlayer mp) {

        Log.v("SuperMusicBox","onCompletion 0");
        Log.v("SuperMusicBox","mp " + mp.isLooping());

        stopForeground(true);
        mbIsInitial = true;
        Log.v("SuperMusicBox","onCompletion 1");
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.release();
        mp = null;

        Toast.makeText(MediaPlayerService.this,"Erro,Stop play",Toast.LENGTH_LONG).show();

        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        Log.v("SuperMusicBox","onPrepared 0");
        AudioManager audioMgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int r = audioMgr.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (r != AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
            mp.setVolume(0.1f, 0.1f);

        mp.start();

        Intent it = new Intent(getApplicationContext(), MainActivity.class);

        PendingIntent penIt = PendingIntent.getActivity(
                getApplicationContext(), 0, it,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Notification noti = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setTicker("Play Music")
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Playing")
                .setContentIntent(penIt)
                .build();

        startForeground(1, noti);

        Toast.makeText(MediaPlayerService.this, "Start Play", Toast.LENGTH_LONG)
                .show();

        Log.v("SuperMusicBox","onPrepared 1");
    }
    @Override
    public void onAudioFocusChange(int focusChange) {

        if (mMediaPlayer == null)
            return;

        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:

                Log.v("SuperMusicBox","AUDIOFOCUS_GAIN");
                mMediaPlayer.setVolume(0.8f, 0.8f);
                mMediaPlayer.start();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:

                Log.v("SuperMusicBox","AUDIOFOCUS_LOSS");
                stopSelf();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:

                Log.v("SuperMusicBox","AUDIOFOCUS_LOSS_TRANSIENT");
                if (mMediaPlayer.isPlaying())
                    mMediaPlayer.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:

                Log.v("SuperMusicBox","AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                if (mMediaPlayer.isPlaying())
                    mMediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }

    private void startPlay () {

        Log.v("SuperMusicBox","startPlay 1");

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.butterflies);

        try {

            mMediaPlayer.setDataSource(this,uri);
            mMediaPlayer.prepareAsync();
        }catch (Exception e) {

            Toast.makeText(MediaPlayerService.this,"Music files are wrong!",Toast.LENGTH_LONG).show();
        }

    };
}
