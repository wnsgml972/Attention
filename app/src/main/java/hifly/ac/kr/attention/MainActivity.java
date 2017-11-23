package hifly.ac.kr.attention;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private final MyHandler mHandler = new MyHandler(this);
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Main_Friend_Fragment mainFragment = new Main_Friend_Fragment();
    private Main_Friend_Fragment mainFragment2 = new Main_Friend_Fragment();
    private Main_Friend_Fragment mainFragment3 = new Main_Friend_Fragment();
    private Button voiceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewPager();
    }

    private void initViewPager(){
        viewPager = (ViewPager) findViewById(R.id.main_frame_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.main_tabLayout);
        voiceBtn = (Button) findViewById(R.id.voiceBtn);
        TabLayout.Tab friendTab = tabLayout.newTab();
        friendTab.setIcon(R.drawable.people);
        tabLayout.addTab(friendTab);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.chat));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.emsy));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);
        //tabLayout.setupWithViewPager(viewPager);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        int selectedColor = ContextCompat.getColor(getApplicationContext(), R.color.color_Black);
        int unSelectedColor = ContextCompat.getColor(getApplicationContext(), R.color.color_Gray);

        tabLayout.getTabAt(0).getIcon().setColorFilter(selectedColor , PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(unSelectedColor, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(unSelectedColor, PorterDuff.Mode.SRC_IN);

        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        int selectedColor = ContextCompat.getColor(getApplicationContext(), R.color.color_Black);
                        tab.getIcon().setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        int unSelectedColor = ContextCompat.getColor(getApplicationContext(), R.color.color_Gray);
                        tab.getIcon().setColorFilter(unSelectedColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                }
        );
    }

    public class PageAdapter extends FragmentStatePagerAdapter {
        public PageAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
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
    }

    public void voiceActivity(View v){
        Intent intent = new Intent(this, VoiceTest.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }



    // 핸들러 객체 만들기
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
