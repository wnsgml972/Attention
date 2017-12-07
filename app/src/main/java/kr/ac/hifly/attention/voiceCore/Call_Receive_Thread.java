package kr.ac.hifly.attention.voiceCore;

import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
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
    private String connect_IP;
    private InetSocketAddress inetSocketAddress;
    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;
    private AudioTrack mAudioTrack;
    private byte audioBuffer[];
    private int BUFFER_SIZE;

    Call_Receive_Thread(String userIP){
        this.connect_IP = userIP;
    }
    public void run(){
        try {
            inetSocketAddress = new InetSocketAddress(connect_IP,Values.SERVER_PORT);
            datagramSocket = new DatagramSocket();
            initAudioSetting();
            mAudioTrack.play();
        }catch (Exception e){
            e.getStackTrace();
        }
        while(true) {
            try {
                datagramPacket = new DatagramPacket(audioBuffer, 0, BUFFER_SIZE, inetSocketAddress);
                datagramSocket.receive(datagramPacket);

                mAudioTrack.write(audioBuffer,0,audioBuffer.length);

                //int read = audioRecord.read(audioBuffer, 0, audioBuffer.length);



            } catch (Exception e) {
                Log.i(Values.TAG, "Voice Error");
                e.getStackTrace();
                return;
            }
        }
    }

    public void initAudioSetting() {
        BUFFER_SIZE = AudioRecord.getMinBufferSize(Values.RECORDING_RATE, Values.AUDIO_CHANNEL, Values.AUDIO_FORMAT);
        audioBuffer = new byte[BUFFER_SIZE];
        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,Values.RECORDING_RATE ,Values.AUDIO_CHANNEL, Values.AUDIO_FORMAT, BUFFER_SIZE, AudioTrack.MODE_STREAM);
    }

}
