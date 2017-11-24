package kr.ac.hifly.attention.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import hifly.ac.kr.attention.R;
import kr.ac.hifly.attention.data.User;
import kr.ac.hifly.attention.voiceCore.Call_Service;

/**
 * Created by CYSN on 2017-11-12.
 */

public class Main_Friend_Call_Activity extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionButton call_endFab;
    private SoundPool sound;
    private int soundId;
    private TextView textView;

    private User user;

    private Intent intent;

    private Messenger messenger;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            messenger = new Messenger(service);
            if(messenger != null){
                Message message = new Message();
                message.what = 0;
                message.obj = new Messenger(new RemoteHandler());
                try {
                    messenger.send(message);
                }catch (Exception e){
                    e.getStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private class RemoteHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    @Override
    public void onBackPressed() {
        unbindService(serviceConnection);
        stopService(intent);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_friend_call);
        textView = (TextView) findViewById(R.id.main_friend_call_textview);
        call_endFab = (FloatingActionButton) findViewById(R.id.main_friend_call_end_fab);
        user = (User)getIntent().getSerializableExtra("object");
        if(user != null){
            textView.setText(user.getName() + "에게 전화 거는중...");
        }
        call_endFab.setOnClickListener(this);
        intent = new Intent(this, Call_Service.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    @Override
    public void onClick(View view) {
        if(view == call_endFab){
            onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onStart() {
        super.onStart();
      /*  AudioAttributes audioAttributes;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            sound = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(8).build();
        }
        else {
            sound = new SoundPool(8, AudioManager.STREAM_NOTIFICATION, 0);
        }

        soundId = sound.load(this, R.raw.ioi, 1);
        sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                sound.play(soundId, 1f, 1f, 0, 0, 1f);
            }
        });*/

    }

    @Override
    protected void onStop() {
        super.onStop();

     /* soundId = 0;
        sound.release();
        sound = null;*/
    }
}
