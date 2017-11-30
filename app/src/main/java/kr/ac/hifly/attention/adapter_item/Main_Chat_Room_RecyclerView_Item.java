package kr.ac.hifly.attention.adapter_item;



public class Main_Chat_Room_RecyclerView_Item {
    private String name;
    private String last_content;
    private String time;

    public Main_Chat_Room_RecyclerView_Item(String name, String last_content, String time) {
        this.name = name;
        this.last_content = last_content;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_content() {
        return last_content;
    }

    public void setLast_content(String last_content) {
        this.last_content = last_content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
