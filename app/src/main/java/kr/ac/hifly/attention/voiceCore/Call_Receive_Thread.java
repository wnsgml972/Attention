package kr.ac.hifly.attention.voiceCore;

import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;

import kr.ac.hifly.attention.value.Values;


public class Call_Receive_Thread extends Thread{
    private String connect_IP = "223.194.154.62";
    private InetSocketAddress inetSocketAddress;
    private DatagramSocket datagramSocket;
    private AudioRecord audioRecord;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private byte audioBuffer[];
    private int BUFFER_SIZE;
    public void run(){
        try {
            socket = new Socket(Values.SERVER_IP, Values.SERVER_PORT);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());


            String message = dis.readUTF();
            if(!message.equals(null))
                connect_IP = message;
            while(true){
                String startMessage = dis.readUTF();
                if(startMessage.equals("start"))
                    break;
            }
            inetSocketAddress = new InetSocketAddress(connect_IP,Values.SERVER_PORT);
            datagramSocket = new DatagramSocket();
            initAudioSetting();
            audioRecord.startRecording();
        }catch (Exception e){
            e.getStackTrace();
        }
        while(true) {
            try {
                int read = audioRecord.read(audioBuffer, 0, audioBuffer.length);
                //Log.i(TAG, "sending");

                datagramSocket.send(new DatagramPacket(audioBuffer, 0, read, inetSocketAddress));
            } catch (Exception e) {
                Log.i(Values.TAG, "Voice Error");
                e.getStackTrace();
                return;
            }
        }
    }
    public void sendMessage(String message){
        try {
            if(dos != null)
                dos.writeUTF(message);
        }catch (Exception e){
            e.getStackTrace();
        }
    }

    public void initAudioSetting() {
        BUFFER_SIZE = AudioRecord.getMinBufferSize(Values.RECORDING_RATE, Values.AUDIO_CHANNEL, Values.AUDIO_FORMAT);
        audioBuffer = new byte[BUFFER_SIZE];

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, Values.RECORDING_RATE, Values.AUDIO_CHANNEL, Values.AUDIO_FORMAT, BUFFER_SIZE);
    }

}
