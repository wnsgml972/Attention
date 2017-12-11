package kr.ac.hifly.attention.main;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import java.util.List;

import hifly.ac.kr.attention.R;
import kr.ac.hifly.attention.Interface.TestCallback;
import kr.ac.hifly.attention.adapter.Main_Chat_Room_RecyclerView_Adapter;
import kr.ac.hifly.attention.adapter_item.ChatActivity_RecyclerView_Item;
import kr.ac.hifly.attention.adapter_item.Main_Chat_Room_RecyclerView_Item;
import kr.ac.hifly.attention.value.Values;

public class Main_Chat_Room_Fragment extends Fragment implements View.OnClickListener {

    private TextView null_second;
    private Main_Chat_Room_RecyclerView_Adapter second_recyclerView_adapter;
    private List<Main_Chat_Room_RecyclerView_Item> second_recyclerView_items;
    private RecyclerView second_RecyclerView;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); //firebase 접속
    private DatabaseReference databaseReference = firebaseDatabase.getReference();  //firebase json tree 접근
    private DatabaseReference chat = firebaseDatabase.getReference("chat");
    private DatabaseReference chatRoom = firebaseDatabase.getReference("ChatRoom");

    private Messenger messenger;
    private ChatRoomWrapper chatRoomWrapper;
    private String myUUID;
    private String value;
    private String innerValue;
    private String value_chat_room_name;
    private StringBuilder real_chat_room_name = new StringBuilder("");

    public void setMessenger(Messenger messenger){
        this.messenger = messenger;
            Log.i("setmessage", "불림" + messenger.toString());
            Message message = new Message();
            chatRoomWrapper = new ChatRoomWrapper(second_recyclerView_adapter, second_recyclerView_items, null_second);

            message.what = Values.CHAT_ROOM;
            message.obj = chatRoomWrapper;

            try {
            messenger.send(message);

        } catch (Exception e) {
            e.getStackTrace();
        }
    }


    private class RemoteHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_chat_room_fragment, container, false);
        second_RecyclerView = (RecyclerView) view.findViewById(R.id.chat_Room_RecyclerView);
        setRecyclerView();

        null_second = (TextView) view.findViewById(R.id.null_second_item);
        if (second_recyclerView_items.size() == 0) {
            null_second.setVisibility(view.VISIBLE);
        } else {
            null_second.setVisibility(view.INVISIBLE);
        }

        // 채팅창 방 업데이트
        if(second_recyclerView_items.isEmpty()){

            myUUID = getActivity().getSharedPreferences(Values.userInfo, Context.MODE_PRIVATE).getString(Values.userUUID, "null");

            databaseReference.child(Values.USER).child(myUUID).child("friends").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        value = snapshot.getValue(String.class);
                        Log.i("123456", "for문 value : " + value);
                        if (value != null && !value.equals("null")) {
                            innerValue = value;  // innerValue는 내가 가지고 있는 채팅방 이름

                            databaseReference.child("ChatRoom").child(innerValue).limitToLast(1).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot,String s) {

                                    ChatActivity_RecyclerView_Item item = dataSnapshot.getValue(ChatActivity_RecyclerView_Item.class);
                                    value_chat_room_name = dataSnapshot.getRef().getParent().getKey();
                                    Log.i("123456", "getKey : " + dataSnapshot.getKey());
                                    if (item == null || item.getSender_name() == null) {
                                        Log.i("123456", "item null");
                                        return;
                                    }
                                    Log.i("123456", "이름은?" + item.getSender_name());
                                    Log.i("123456", "방이름 : " + value_chat_room_name);
                                    int kk = 0;
                                    for(int i=0; i<second_recyclerView_items.size(); i++){
                                        if(second_recyclerView_items.get(i).getChatRoomName().equals(value_chat_room_name)){
                                            Log.i("123456", "들어옴들어옴");
                                            for(int aa = MainActivity.users.size(); aa >= 0; aa--)
                                            {
                                                if(value_chat_room_name.contains(MainActivity.users.get(i).getUuid())){
                                                    real_chat_room_name.append(MainActivity.users.get(i).getName() + " ");
                                                }
                                            }

                                            second_recyclerView_items.set(i,new Main_Chat_Room_RecyclerView_Item(real_chat_room_name.toString(), item.getChat_content(), item.getTime(), value_chat_room_name));
                                            kk = 1;
                                        }
                                    }
                                    if(kk == 0){
                                        second_recyclerView_items.add(new Main_Chat_Room_RecyclerView_Item(real_chat_room_name.toString(), item.getChat_content(), item.getTime(), value_chat_room_name));
                                    }

                                    if (second_recyclerView_items.size() != 0) {
                                        chatRoomWrapper.getNullTextView().setVisibility(chatRoomWrapper.getNullTextView().INVISIBLE);
                                    }
                                    null_second.setVisibility(null_second.INVISIBLE);
                                    second_recyclerView_adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setRecyclerView() {
        second_recyclerView_items = new ArrayList<Main_Chat_Room_RecyclerView_Item>();

        second_recyclerView_adapter = new Main_Chat_Room_RecyclerView_Adapter(getContext(), second_recyclerView_items, testCallback);
        second_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        second_RecyclerView.setAdapter(second_recyclerView_adapter);


    }

    @Override
    public void onClick(View view) {
        Log.i(Values.TAG, "룸 번호 클릭 리스너");
    }

    private TestCallback testCallback = new TestCallback() {
        @Override
        public void test(ChatActivity_RecyclerView_Item item, int position) {
            second_recyclerView_items.get(position).setLast_content(item.getChat_content());
            second_recyclerView_items.get(position).setTime(item.getTime());
            Log.e("time", item.getTime());
            second_recyclerView_adapter.notifyDataSetChanged();
        }
    };
}