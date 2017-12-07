package kr.ac.hifly.attention.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import hifly.ac.kr.attention.R;
import kr.ac.hifly.attention.Interface.TestCallback;
import kr.ac.hifly.attention.adapter_item.ChatActivity_RecyclerView_Item;
import kr.ac.hifly.attention.adapter_item.Main_Chat_Room_RecyclerView_Item;
import kr.ac.hifly.attention.main.Main_Chat_Room_Fragment;
import kr.ac.hifly.attention.main.Main_Friend_Message_Activity;

/**
 * Created by User on 2017-10-14.
 */

public class Main_Chat_Room_RecyclerView_Adapter extends RecyclerView.Adapter<Main_Chat_Room_RecyclerView_Adapter.ViewHolder> {

    private Context context;
    private TestCallback testCallback;
    private List<Main_Chat_Room_RecyclerView_Item> main_chat_room_recyclerView_items;
    private RecyclerView second_RecyclerView;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); //firebase 접속
    private DatabaseReference databaseReference = firebaseDatabase.getReference();  //firebase json tree 접근
    private DatabaseReference chat = firebaseDatabase.getReference("chat");
    private DatabaseReference chatRoom = firebaseDatabase.getReference("ChatRoom");
    private Main_Chat_Room_RecyclerView_Adapter main_chat_room_recyclerView_adapter = this;

    public Main_Chat_Room_RecyclerView_Adapter(Context context, List<Main_Chat_Room_RecyclerView_Item> main_chat_room_recyclerView_items, TestCallback testCallback) {
        this.context = context;
        this.main_chat_room_recyclerView_items = main_chat_room_recyclerView_items;
        this.testCallback = testCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_chat_room_recyclerview_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(main_chat_room_recyclerView_items.get(position).getName());
        holder.content.setText(main_chat_room_recyclerView_items.get(position).getLast_content());
        holder.time.setText(main_chat_room_recyclerView_items.get(position).getTime());
/*
        chatRoom.child(main_chat_room_recyclerView_items.get(position).getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Log.e("asd어탭터 성공 ㅋ", "---------");
                    ChatActivity_RecyclerView_Item chatActivity_recyclerView_item = dataSnapshot.getValue(ChatActivity_RecyclerView_Item.class);
                    testCallback.test(chatActivity_recyclerView_item, position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return main_chat_room_recyclerView_items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public TextView content;
        public TextView time;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.second_RecyclerView_Item_Name);
            content = itemView.findViewById(R.id.second_RecyclerView_Item_Content);
            time = itemView.findViewById(R.id.second_RecyclerView_Item_Time);
            linearLayout = itemView.findViewById(R.id.second_RecyclerView_item_setClick);
            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.second_RecyclerView_item_setClick:
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        int position = getAdapterPosition();
                        Intent intent = new Intent(view.getContext(), Main_Friend_Message_Activity.class);
                        intent.putExtra("name", main_chat_room_recyclerView_items.get(position).getName());
                        Log.e("second_fragment_name", main_chat_room_recyclerView_items.get(position).getName());
                        view.getContext().startActivity(intent);
                        break;
                    }
            }
        }
    }
}

