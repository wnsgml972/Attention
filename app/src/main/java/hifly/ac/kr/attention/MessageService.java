package hifly.ac.kr.attention;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;

import java.io.DataInputStream;
import java.net.Socket;

/**
 * Created by CYSN on 2017-11-14.
 */

public class MessageService extends Service {
    private final String SERVER_IP = "192.168.0.1";
    private final int SERVER_PORT = 10035;
    private Socket socket;
    private DataInputStream dis;
    @Override
    public ComponentName startService(Intent service) {
        return super.startService(service);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }
    class ReceiveThread extends Thread{
        public void run(){
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                dis = new DataInputStream(socket.getInputStream());
            }catch (Exception e){
                e.getStackTrace();
            }
            while(true){
                String message = dis.readUTF();

            }
        }
    }
}
