package com.supermusic.rodger.supermusicbox;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;


public class MainActivity extends Activity implements View.OnClickListener{

    private Button mBtnStart,mBtnPause,mBtnStop,mBtnMediaRepeat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v("SuperMusicBox","MainActivity onCreat 1");
        mBtnStart = (Button) findViewById(R.id.btnStart);
        mBtnPause = (Button) findViewById(R.id.btnPause);
        mBtnStop = (Button) findViewById(R.id.btnStop);
        mBtnMediaRepeat = (ToggleButton) findViewById(R.id.btnMediaRepeat);

        mBtnStart.setOnClickListener(this);
        mBtnPause.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
        mBtnMediaRepeat.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onClick(View v) {

        Intent it;
        switch (v.getId()) {
            case R.id.btnStart:

                Log.v("SuperMusicBox","onClick");
                it = new Intent(MainActivity.this, MediaPlayerService.class);
                it.setAction(MediaPlayerService.ACTION_PLAY);
                startService(it);
                break;
            case R.id.btnPause:
                it = new Intent(MainActivity.this, MediaPlayerService.class);
                it.setAction(MediaPlayerService.ACTION_PAUSE);
                startService(it);
                break;
            case R.id.btnStop:
                it = new Intent(MainActivity.this, MediaPlayerService.class);
                stopService(it);
                break;
            case R.id.btnMediaRepeat:
                it = new Intent(MainActivity.this, MediaPlayerService.class);
                it.setAction(MediaPlayerService.ACTION_SET_REPEAT);
                startService(it);
                break;
        }
    }


}
