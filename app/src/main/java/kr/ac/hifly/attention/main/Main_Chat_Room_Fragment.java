package kr.ac.hifly.attention.main;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hifly.ac.kr.attention.R;
import kr.ac.hifly.attention.Interface.TestCallback;
import kr.ac.hifly.attention.adapter.Main_Chat_Room_RecyclerView_Adapter;
import kr.ac.hifly.attention.adapter_item.ChatActivity_RecyclerView_Item;
import kr.ac.hifly.attention.adapter_item.Main_Chat_Room_RecyclerView_Item;

public class Main_Chat_Room_Fragment extends Fragment implements View.OnClickListener{
    private TextView null_second;
    private Main_Chat_Room_RecyclerView_Adapter second_recyclerView_adapter;
    private List<Main_Chat_Room_RecyclerView_Item> second_recyclerView_items;
    private RecyclerView second_RecyclerView;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); //firebase 접속
    private DatabaseReference databaseReference = firebaseDatabase.getReference();  //firebase json tree 접근
    private DatabaseReference chat = firebaseDatabase.getReference("chat");
    private DatabaseReference chatRoom = firebaseDatabase.getReference("ChatRoom");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_chat_room_fragment,container,false);
        second_RecyclerView = (RecyclerView)view.findViewById(R.id.second_RecyclerView);
        setRecyclerView();
        null_second = (TextView)view.findViewById(R.id.null_second_item);
        if(second_recyclerView_items.size()==0){
            null_second.setVisibility(view.VISIBLE);
        }else {
            null_second.setVisibility(view.INVISIBLE);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("asd");
        getContext().registerReceiver(broadcastReceiver, intentFilter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    private void setRecyclerView(){
        second_recyclerView_items = new ArrayList<Main_Chat_Room_RecyclerView_Item>();

        second_recyclerView_adapter = new Main_Chat_Room_RecyclerView_Adapter(getContext(),second_recyclerView_items,testCallback);
        second_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        second_RecyclerView.setAdapter(second_recyclerView_adapter);
    }

    @Override
    public void onClick(View view) {
    }
    private TestCallback testCallback = new TestCallback() {
        @Override
        public void test(ChatActivity_RecyclerView_Item item, int position) {
            second_recyclerView_items.get(position).setLast_content(item.getChat_content());
            second_recyclerView_items.get(position).setTime(item.getTime());
            Log.e("time",item.getTime());
            second_recyclerView_adapter.notifyDataSetChanged();
        }



    };

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("asd")){
                String getName = intent.getStringExtra("name");
                Log.e("잘옴!",getName);
                null_second.setVisibility(getView().INVISIBLE);
                second_recyclerView_items.add(new Main_Chat_Room_RecyclerView_Item(getName,"",""));
                second_recyclerView_adapter.notifyDataSetChanged();
                chat.child(getName).removeValue();    // chat 지우는 코드
                chatRoom.child(getName).removeValue();
                long now  = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat setformat = new SimpleDateFormat("HH:mm");
                String time  = setformat.format(date);
                ChatActivity_RecyclerView_Item item = new ChatActivity_RecyclerView_Item(getName,"반갑습니다",time,0);
                ChatActivity_RecyclerView_Item item2 = new ChatActivity_RecyclerView_Item(getName,"앞으로 잘 부탁 드립니다.",time,0);

                chat.child(getName).push().setValue(item);
                chat.child(getName).push().setValue(item2);

                chatRoom.child(getName).setValue(item);

                second_recyclerView_adapter.notifyDataSetChanged();
            }
        }
    };

}