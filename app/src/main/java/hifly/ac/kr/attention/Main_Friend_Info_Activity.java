package hifly.ac.kr.attention;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Main_Friend_Info_Activity extends AppCompatActivity {

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
