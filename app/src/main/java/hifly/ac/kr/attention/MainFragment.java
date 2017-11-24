package hifly.ac.kr.attention;

/**
 * Created by hscom-018 on 2017-11-22.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import java.util.Vector;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private Animation animation;
    private Animation.AnimationListener animationListener;

    public MainFragment() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_intro, container, false);
        initAnimation();

        animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext() ,R.anim.alpha);
        animation.setAnimationListener(animationListener);

        view.startAnimation(animation);
        return view;
    }

    public void initAnimation() {
        animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
                getActivity().finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

    }
}
