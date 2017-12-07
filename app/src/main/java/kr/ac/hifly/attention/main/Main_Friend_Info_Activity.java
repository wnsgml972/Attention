package kr.ac.hifly.attention.main;


import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import hifly.ac.kr.attention.R;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kr.ac.hifly.attention.data.User;

public class Main_Friend_Info_Activity extends AppCompatActivity {
    private ImageView profileView;
    private TextView nameTextView;
    private TextView stateTextView;
    private FloatingActionButton callFab;
    private FloatingActionButton messageFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_friend_info);
        nameTextView = (TextView) findViewById(R.id.main_friend_info_name_textView);
        stateTextView = (TextView) findViewById(R.id.main_friend_info_state_textView);
        profileView = (ImageView) findViewById(R.id.activity_main_friend_profile);

        Glide.with(this).load(R.drawable.main_friend_basic_icon)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into((profileView));

        Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/attention-469ab.appspot.com/o/%EA%B7%B8%EB%A6%BC1.png?alt=media&token=b59f864d-b8c5-4907-9c99-dfac392d5ff1") .apply(RequestOptions.bitmapTransform(new CircleCrop())).thumbnail(0.1f).into(profileView);
        callFab = (FloatingActionButton)findViewById(R.id.main_friend_info_call_fab);
        messageFab = (FloatingActionButton) findViewById(R.id.main_friend_info_message_fab);
        User user = (User)getIntent().getSerializableExtra("object");
        if(user != null){
            nameTextView.setText(user.getName());
            stateTextView.setText(user.getStateMessage());
        }
    }

    public void call(View v){
        Intent intent = new Intent(getApplicationContext(), Main_Friend_Call_Activity.class);
        intent.putExtra("object",(User)getIntent().getSerializableExtra("object"));
        startActivity(intent);
    }

    public void message(View v){
        Intent intent = new Intent(getApplicationContext(), Main_Friend_Message_Activity.class);
        intent.putExtra("object",(User)getIntent().getSerializableExtra("object"));
        startActivity(intent);
    }
}
