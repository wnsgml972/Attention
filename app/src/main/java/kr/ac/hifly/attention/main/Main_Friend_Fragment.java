package kr.ac.hifly.attention.main;


import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hifly.ac.kr.attention.R;
import kr.ac.hifly.attention.data.User;

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
        adapter.addUser(null);
        adapter.addUser(new User(0, "최용석", "좋은 하루~","5b2fecb7ab1149288fd18618220a2ed3"));
        adapter.addUser(null);
        for(int i=0; i<10; i++) {
            adapter.addUser(new User(0, "최용석--", "좋은 하루~"));
        }
        adapter.addUser(new User(0, "김준희", "오지구요 지리구요 고요고요 고요한밤이구요~"));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }
}
