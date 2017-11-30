package kr.ac.hifly.attention.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import hifly.ac.kr.attention.R;
import kr.ac.hifly.attention.data.User;
import kr.ac.hifly.attention.messageCore.MessageService;
import kr.ac.hifly.attention.value.Values;



public class Main_Friend_Call_Receive_Activity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton call_refuseFab;
    private FloatingActionButton call_receiveFab;

    private TextView textView;
    private MediaPlayer mp;

    private Messenger messenger;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messenger = new Messenger(iBinder);
            if (messenger != null) {
                Message message = new Message();
                message.what = 0;
                message.obj = new Messenger(new RemoteHandler());
                try {
                    messenger.send(message);
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private class RemoteHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    /*
    FLAG_KEEP_SCREEN_ON : Screen 을 켜진 상태로 유지
    FLAG_DISMISS_KEYGUARD : Keyguard를 해지
    FLAG_TURN_SCREEN_ON : Screen On
    FLAG_SHOW_WHEN_LOCKED : Lock 화면 위로 실행
    */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_friend_call_receive);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);


        String name = getIntent().getStringExtra("name");
        String ip = getIntent().getStringExtra("ip");

        textView = (TextView) findViewById(R.id.main_friend_call_receive_textview);
        call_refuseFab = (FloatingActionButton) findViewById(R.id.main_friend_call_refuse_fab);
        call_receiveFab = (FloatingActionButton) findViewById(R.id.main_friend_call_receive_fab);

        if (name != null) {
            textView.setText(name + "에게 전화 왔습니다...");
        }
        Intent intent = new Intent(this, MessageService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View view) {
        Message message = new Message();

        if (view == call_refuseFab) {//call refuse
            message.what = Values.REFUSE_CALL;
            message.obj = Values.REFUSE;
            try {
                messenger.send(message);
            } catch (Exception e) {
                e.getStackTrace();
            }
            onBackPressed();

        } else if (view == call_receiveFab) {//call receive
            message.what = Values.RECEIVE_CALL;
            message.obj = Values.RECEIVE;
            try {
                messenger.send(message);
            } catch (Exception e) {
                e.getStackTrace();
            }
            call_receiveFab.setVisibility(View.GONE);
            TranslateAnimation ani = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
            ani.setFillAfter(true); // 애니메이션 후 이동한좌표에
            ani.setDuration(1000); //지속시간
            call_refuseFab.startAnimation(ani);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            mp = MediaPlayer.create(this, R.raw.ioi);
            mp.seekTo(0);
            mp.start();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mp.release();
        mp = null;

    }
}
