package com.supermusic.rodger.supermusicbox;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;
import android.net.Uri;


public class MainActivity extends Activity implements View.OnClickListener{

    private Button mBtnStart,mBtnPause,mBtnStop,mBtnMediaRepeat,mBtnFile;

    static final int REQUEST_READWRITE_STORAGE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Log.v("SuperMusicBox","onActivityResult");
        // 有選擇檔案
        if ( resultCode == RESULT_OK )
        {
            // 取得檔案的 Uri
            Uri uri = data.getData();

            Log.v("SuperMusicBox","uri " + uri.toString());
        }
        else
        {

            Log.v("SuperMusicBox","Wrong Result " + resultCode);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request the permission to user in Android 6.0 or later.
        int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                      new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                      REQUEST_READWRITE_STORAGE);
        }

        Log.v("SuperMusicBox","MainActivity onCreat 1");
        mBtnStart = (Button) findViewById(R.id.btnStart);
        mBtnPause = (Button) findViewById(R.id.btnPause);
        mBtnStop = (Button) findViewById(R.id.btnStop);
        mBtnFile = (Button) findViewById(R.id.btnFile);
        mBtnMediaRepeat = (ToggleButton) findViewById(R.id.btnMediaRepeat);

        mBtnStart.setOnClickListener(this);
        mBtnPause.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
        mBtnFile.setOnClickListener(this);
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
            case R.id.btnFile:

                Log.v("SuperMusicBox","onClick btnFile");

                break;
        }
    }


}
