package kr.ac.hifly.attention.messageCore;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.UUID;

import kr.ac.hifly.attention.main.Main_Friend_Call_Receive_Activity;
import kr.ac.hifly.attention.value.Values;
import kr.ac.hifly.attention.voiceCore.Call_Receive_Thread;

public class MessageService extends Service {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ReceiveThread receiveThread;
    private Messenger mRemote;
    private boolean isCalling = false;
    private static PowerManager.WakeLock sCpuWakeLock;

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
                case Values.REFUSE_CALL:
                    message = (String) msg.obj;
                    break;
                case Values.RECEIVE_CALL:
                    message = (String) msg.obj;
                    if (!isCalling) {
                        isCalling = true;
                        screenOn();
                        Call_Receive_Thread call_receive_thread = new Call_Receive_Thread();
                        call_receive_thread.start();
                    }
                    break;
                case Values.END_CALL:
                    isCalling = false;
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
        if (receiveThread == null) {
            receiveThread = new ReceiveThread();
            receiveThread.start();
            Log.i(Values.TAG, "ReceiveThread Start!!");
        }
        Log.i(Values.TAG, "MessageService Start!!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
        public void run()  {
            try {


                //databaseReference.child("");
            } catch (Exception e) {
                e.getStackTrace();
                return;
            }
            while (true) {
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
            }
        }
    }
}
