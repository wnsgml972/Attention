package kr.ac.hifly.attention.messageCore;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import hifly.ac.kr.attention.R;
import kr.ac.hifly.attention.adapter.Main_Chat_Room_RecyclerView_Adapter;
import kr.ac.hifly.attention.adapter_item.ChatActivity_RecyclerView_Item;
import kr.ac.hifly.attention.adapter_item.Main_Chat_Room_RecyclerView_Item;
import kr.ac.hifly.attention.data.Call;
import kr.ac.hifly.attention.data.User;
import kr.ac.hifly.attention.main.ChatRoomWrapper;
import kr.ac.hifly.attention.main.MainActivity;
import kr.ac.hifly.attention.main.Main_Friend_Call_Receive_Activity;
import kr.ac.hifly.attention.value.Values;
import kr.ac.hifly.attention.voiceCore.Call_Receive_Thread;
import kr.ac.hifly.attention.voiceCore.Call_Thread;

public class MessageService extends Service {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ReceiveThread receiveThread;
    private Messenger mRemote;
    private boolean isCalling = false;
    private boolean isFirst = true;
    private String voiceRoomName;
    private static PowerManager.WakeLock sCpuWakeLock;
    private String myUUID;
    private String myName;
    private MediaPlayer mp;

    private Main_Chat_Room_RecyclerView_Adapter main_chat_room_recyclerView_adapter;
    private List<Main_Chat_Room_RecyclerView_Item> main_chat_room_recyclerView_items;
    private String value;
    private ChatRoomWrapper chatRoomWrapper;
    private Call_Thread call_thread;
    private Call_Receive_Thread call_receive_thread;
    @Override
    public IBinder onBind(Intent intent) {
        return new Messenger(new RemoteHandler()).getBinder();
    }

    // Send message to activity
    public void remoteSendMessage(String data) {
        if (mRemote != null) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = data;
            try {
                mRemote.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void screenOn() {

        if (sCpuWakeLock != null) {
            return;
        }
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        sCpuWakeLock = pm.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "hi");

        sCpuWakeLock.acquire();


        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }

    }

    // Service handler
    private class RemoteHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            String message = null;
            switch (msg.what) {
                case 0:
                    // Register activity hander
                    mRemote = (Messenger) msg.obj;
                    break;
                case Values.START_CALL:
                    Log.i(Values.TAG,"전화 들옴~~~~~~~~~~~~~~~~~~");
                    try {
                        mp.prepare();
                        mp.seekTo(0);
                        mp.start();
                    }catch (Exception e){
                        e.getStackTrace();
                    }
                    break;
                case Values.REFUSE_CALL:
                    Log.i(Values.TAG,message + " ");
                    databaseReference.child(Values.USER).child(myUUID).child(Values.VOICE).child(Values.VOICE).setValue("null");
                    databaseReference.child(Values.USER).child(myUUID).child(Values.VOICE).child(Values.VOICE_CALLER).setValue("null");
                    databaseReference.child(Values.VOICE).child(voiceRoomName).child(myUUID).child(Values.VOICE_CALL_STATE).setValue(Values.REFUSE);
                    message = (String) msg.obj;
                    isCalling = false;
                    break;
                case Values.RECEIVE_CALL:
                    message = (String) msg.obj;//ip
                    String messages[] = message.split(" ");
                    String userIP = messages[0];
                    final int userPort = Integer.parseInt(messages[1]);
                    Log.i(Values.TAG,userIP + " !!@!@!@ " + userPort);
                    if (!isCalling) {
                        isCalling = true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Socket socket = new Socket("www.google.com", 80);
                                    String ipAddress = socket.getLocalAddress().toString();
                                    ipAddress = ipAddress.substring(1);
                                    Call call = new Call(myUUID, Values.RECEIVE, ipAddress, userPort);
                                    databaseReference.child(Values.VOICE).child(voiceRoomName).child(myUUID).setValue(call);
                                }catch (Exception e){
                                    e.getStackTrace();
                                }
                            }
                        }).start();
                        mp.release();
                        screenOn();
                        call_thread = new Call_Thread(userIP,userPort);
                        call_receive_thread = new Call_Receive_Thread(userIP,userPort);
                        call_thread.start();
                        call_receive_thread.start();
                    }
                    break;
                case Values.END_CALL:
                    Log.i(Values.TAG,message + " ");
                    isCalling = false;
                    if (voiceRoomName != null && !voiceRoomName.equals("null")) {
                        databaseReference.child(Values.USER).child(myUUID).child(Values.VOICE).removeValue();
                        databaseReference.child(Values.VOICE).child(voiceRoomName).child(myUUID).removeValue();
                    }
                    if(call_thread != null){
                        call_thread.interrupt();
                    }
                    if(call_receive_thread != null){
                        call_receive_thread.interrupt();
                    }
                    break;
                case Values.CHAT_ROOM:
                    Log.i("123456", "들어옴");
                    chatRoomWrapper = (ChatRoomWrapper) msg.obj;
                    main_chat_room_recyclerView_adapter = chatRoomWrapper.getAdapter();
                    main_chat_room_recyclerView_items = chatRoomWrapper.getItems();

                    databaseReference.child(Values.USER).child(myUUID).child("friends").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                value = snapshot.getValue(String.class);
                                if (value != null && !value.equals("null")) {

                                    databaseReference.child("ChatRoom").child(value).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Log.i("123456", value);
                                            ChatActivity_RecyclerView_Item item = dataSnapshot.getValue(ChatActivity_RecyclerView_Item.class);
//                                            main_chat_room_recyclerView_items.add(new Main_Chat_Room_RecyclerView_Item(item.getSender_name(), item.getChat_content(), item.getTime(), value));
                                            main_chat_room_recyclerView_items.add(new Main_Chat_Room_RecyclerView_Item("", "", "", value));
                                            main_chat_room_recyclerView_adapter.notifyDataSetChanged();
                                            if (main_chat_room_recyclerView_items.size() != 0) {
                                                chatRoomWrapper.getNullTextView().setVisibility(chatRoomWrapper.getNullTextView().INVISIBLE);
                                            }
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
                    break;
                default:
                    remoteSendMessage("TEST");
                    break;
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        myUUID = getSharedPreferences(Values.userInfo, Context.MODE_PRIVATE).getString(Values.userUUID, "null");
        if (receiveThread == null) {
            receiveThread = new ReceiveThread();
            receiveThread.start();
            Log.i(Values.TAG, "ReceiveThread Start!!");
        }
        Log.i(Values.TAG, "MessageService Start!!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mp = MediaPlayer.create(this, R.raw.ioi);
        startForeground(1, new Notification());

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiveThread != null)
            receiveThread.interrupt();
    }

    class ReceiveThread extends Thread {
        public void run() {
            try {
                if (!myUUID.equals(null)) {
                    databaseReference.child(Values.USER).child(myUUID).child(Values.VOICE).child(Values.VOICE_ROOM).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            voiceRoomName = dataSnapshot.getValue(String.class);
                            if (voiceRoomName != null)
                                Log.i(Values.TAG, voiceRoomName + " " + dataSnapshot.getKey() + "@@@@@@@@@" + databaseReference.child(Values.USER).child(myUUID).child(Values.VOICE).getKey() + " " + databaseReference.child(Values.USER).child(myUUID).child(Values.VOICE).child(Values.VOICE_CALLER).getKey());
                            if (voiceRoomName != null && !voiceRoomName.equals("null")) {//not doing voice chat;
                                databaseReference.child(Values.USER).child(myUUID).child(Values.VOICE).child(Values.VOICE_CALLER).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String caller = dataSnapshot.getValue(String.class);
                                        Log.i(Values.TAG,"들어옴!!!");
                                        if (caller != null)
                                            Log.i(Values.TAG, caller + " " + dataSnapshot.getKey() + "@@@@@@@@@");
                                        if (caller != null && !caller.equals("null")) {
                                            ArrayList<User> users =  MainActivity.users;
                                            for(int i=0; i< users.size(); i++){
                                                if(users.get(i).getUuid().equals(caller)){
                                                    Intent intent = new Intent(getApplicationContext(), Main_Friend_Call_Receive_Activity.class);
                                                    intent.putExtra("name", users.get(i).getName());
                                                    intent.putExtra(Values.VOICE_ROOM, voiceRoomName);
                                                    Log.i(Values.TAG, "name : " + users.get(i).getName());
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    try {
                                                        mp.prepare();
                                                        mp.seekTo(0);
                                                        mp.start();
                                                    }catch (Exception e){
                                                        e.getStackTrace();
                                                    }
                                                    return;
                                                }
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            } catch (Exception e) {
                e.getStackTrace();
                return;
            }
           /* while (true) {
                try {
                    String message=null;
                    if (message.startsWith("callToMe")) {
                        String messages[] = message.split(" ");//1 : name 2: ip
                        Intent intent = new Intent(getApplicationContext(), Main_Friend_Call_Receive_Activity.class);
                        intent.putExtra("name", messages[1]);
                        intent.putExtra("ip", messages[2]);
                        Log.i(Values.TAG, "name : " + messages[1] + " ip : " + messages[2]);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);

                    }
                } catch (Exception e) {
                    return;
                }
            }*/
        }
    }
}
