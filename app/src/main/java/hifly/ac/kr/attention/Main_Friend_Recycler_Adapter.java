package hifly.ac.kr.attention;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CYSN on 2017-11-09.
 */

public class Main_Friend_Recycler_Adapter extends RecyclerView.Adapter<Main_Friend_Recycler_Adapter.Main_Friend_ViewHolder>{
    private ArrayList<User> arrayList;
    private Context context;
    public Main_Friend_Recycler_Adapter(Context context){
        this.context = context;
        arrayList = new ArrayList<User>();
    }
    public void addUser(User user){
        arrayList.add(user);
        notifyItemInserted(arrayList.size()-1);
    }
    @Override
    public Main_Friend_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_main_friend_item, parent, false);
        Main_Friend_ViewHolder viewHolder = new Main_Friend_ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Main_Friend_ViewHolder holder, int position) {
        User user = arrayList.get(position);
        holder.imageView.setImageResource(user.getIcon());
        holder.nameTextView.setText(user.getName());
        holder.stateTextView.setText(user.getStateMessage());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Main_Friend_ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView nameTextView;
        TextView stateTextView;
        public Main_Friend_ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.main_friend_user_icon_imageView);
            nameTextView = (TextView) itemView.findViewById(R.id.main_friend_user_name_textView);
            stateTextView = (TextView) itemView.findViewById(R.id.main_friend_user_state_textView);
        }
    }
}
