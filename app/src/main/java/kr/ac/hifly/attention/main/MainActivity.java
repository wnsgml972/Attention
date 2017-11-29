package kr.ac.hifly.attention.main;


import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import hifly.ac.kr.attention.R;
import kr.ac.hifly.attention.data.VoiceTest;
import kr.ac.hifly.attention.messageCore.MessageService;

public class MainActivity extends AppCompatActivity {

    private final MyHandler mHandler = new MyHandler(this);
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Main_Friend_Fragment mainFragment = new Main_Friend_Fragment();
    private Main_Friend_Fragment mainFragment2 = new Main_Friend_Fragment();
    private Main_Friend_Fragment mainFragment3 = new Main_Friend_Fragment();
    private Button voiceBtn;

    private Intent serviceIntent;   //@@
    private Messenger messenger;    //@@
    private ServiceConnection connection = new ServiceConnection() {            //@@
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
            messenger = null;
        }
    };
    private class RemoteHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 0:
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), "service Message!", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    }
    public void testSend(){
        Message message = new Message();
        message.what = 1;
        message.obj = "ff";
        //messenger.send(message);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewPager();

        serviceIntent = new Intent(this, MessageService.class);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
        startService(serviceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private String getUUID(){
        SharedPreferences preferences = getSharedPreferences("uuid",MODE_PRIVATE);
        return preferences.getString("uuid", null);
    }

                public class PageAdapter extends FragmentPagerAdapter {
                    public PageAdapter(FragmentManager manager){
                        super(manager);
                    }

                    @Override
                    public android.support.v4.app.Fragment getItem(int position) {
                        switch (position){
                            case 0:
                                return mainFragment;
                            case 1:
                                return mainFragment2;
                case 2:
                    return mainFragment3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.main_frame_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.main_tabLayout);
        voiceBtn = (Button) findViewById(R.id.voiceBtn);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.people);
        tabLayout.getTabAt(1).setIcon(R.drawable.chat);
        tabLayout.getTabAt(2).setIcon(R.drawable.emsy);

        int selectedColor = ContextCompat.getColor(getApplicationContext(), R.color.color_Black);
        int unSelectedColor = ContextCompat.getColor(getApplicationContext(), R.color.color_Gray);

        tabLayout.getTabAt(0).getIcon().setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(unSelectedColor, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(unSelectedColor, PorterDuff.Mode.SRC_IN);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selectedColor = ContextCompat.getColor(getApplicationContext(), R.color.color_Black);
                tab.getIcon().setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int selectedColor = ContextCompat.getColor(getApplicationContext(), R.color.color_Gray);
                tab.getIcon().setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    public void voiceActivity(View v){
        Intent intent = new Intent(this, VoiceTest.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        overridePendingTransition(0,0); //@@

    }



    // 핸들러 객체 만들기  @@
    public static class MyHandler extends Handler {
        public static final int CHANGE_FRIEND_INFO = 1;
        public static final int SAY_BYE = 0;
        public static final int SAY_WORD = 2;
        private final WeakReference<MainActivity> mWeakActivity;


        public MyHandler(MainActivity activtiy) {
            mWeakActivity = new WeakReference<MainActivity>(activtiy);

        }
        @Override
        public void handleMessage(Message msg) {
            int message = msg.getData().getInt("message");
            switch (message) {
                case CHANGE_FRIEND_INFO:
                    Intent intent = new Intent(mWeakActivity.get().getApplicationContext(), Main_Friend_Info_Activity.class);
                    intent.putExtra("object", msg.getData().getSerializable("object"));
                    mWeakActivity.get().startActivity(intent);
                    break;
                case SAY_BYE:
                    break;
                case SAY_WORD:
                    break;
                default:
                    super.handleMessage(msg);

            }
        }
    }

}
