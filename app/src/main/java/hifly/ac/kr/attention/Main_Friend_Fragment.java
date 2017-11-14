package hifly.ac.kr.attention;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by CYSN on 2017-11-09.
 */

public class Main_Friend_Fragment extends Fragment {
    private RecyclerView recyclerView;
    public Main_Friend_Fragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_friend, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.main_friend_recyclerview);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Handler handler = new MainActivity.MyHandler((MainActivity)getActivity());

        Main_Friend_Recycler_Adapter adapter = new Main_Friend_Recycler_Adapter(getActivity().getApplicationContext(),handler);
        for(int i=0; i<10; i++) {
            adapter.addUser(new User(0, "최용석", "좋은 하루~"));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }
}
