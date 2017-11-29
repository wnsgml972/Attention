package kr.ac.hifly.attention.data;

import java.io.Serializable;

import hifly.ac.kr.attention.R;



public class User implements Serializable{
    private int icon;
    private String uuid;//유저간 고유 ID
    private String name;
    private String stateMessage;

    public User(int icon, String name, String stateMessage) {
        if(icon == 0)
            this.icon = R.drawable.main_friend_basic_icon;
        else
            this.icon = icon;
        this.name = name;
        this.stateMessage = stateMessage;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStateMessage() {
        return stateMessage;
    }

    public void setStateMessage(String stateMessage) {
        this.stateMessage = stateMessage;
    }
}
