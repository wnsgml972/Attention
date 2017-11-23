package hifly.ac.kr.attention;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by CYSN on 2017-11-12.
 */

public class Main_Friend_Call_Activity extends AppCompatActivity {
    private FloatingActionButton call_endFab;
    private SoundPool sound;
    private int soundId;
    private TextView textView;
    private MediaPlayer mp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_friend_call);
        textView = (TextView) findViewById(R.id.main_friend_info_calling_textview);
        call_endFab = (FloatingActionButton) findViewById(R.id.main_friend_info_call_end_fab);
        User user = (User)getIntent().getSerializableExtra("object");
        if(user != null){
            textView.setText(user.getName() + "에게 전화 거는중...");
        }

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
      try {
          mp = MediaPlayer.create(this, R.raw.ioi);
          mp.seekTo(0);
          mp.start();
      }
      catch (Exception e){

      }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mp.release();
        mp = null;
     /* soundId = 0;
        sound.release();
        sound = null;*/
    }

    public void callEnd(View view) {
        onBackPressed();
    }
}
