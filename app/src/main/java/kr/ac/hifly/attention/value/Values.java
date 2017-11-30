package kr.ac.hifly.attention.value;

import android.media.AudioFormat;

/**
 * Created by CYSN on 2017-11-22.
 */

// 정수 interface
public class Values {

    public static final String TAG = "KKKK";
    public static final String SERVER_IP = "223.194.156.168";

    public static final String userInfo = "userInfo";
    public static final String userUUID = "uuid";
    public static final String userName = "name";
    public static final String userTel = "tel";


    public static final int SERVER_PORT = 10035;
    public static int SERVER_UDP_PORT = 10036;
    public static final int RECORDING_RATE = 44100;
    //private final int RECORDING_RATE = 8000;
    public static final int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    //private final int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_STEREO;
    public static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    //private final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_8BIT;

    public static final int USER_VIEW = 0;
    public static final int NORMAL_VIEW = 1;

    public static final int REFUSE_CALL = 1;
    public static final String REFUSE = "refuse";
    public static final int RECEIVE_CALL = 2;
    public static final String RECEIVE = "receive";
    public static final int END_CALL = 3;

    public static String SetProfilePhoto= "profile_photo";
}
