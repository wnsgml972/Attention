package kr.ac.hifly.attention.adapter_item;



public class ChatActivity_RecyclerView_Item {
    private String chat_name;
    private String chat_content;
    private String time;
    private int itemViewType;

    //빈 생성자 파이어베이스에 데이터 넣기 전에 꼭 필요!
    public ChatActivity_RecyclerView_Item() {

    }
    
    public ChatActivity_RecyclerView_Item(String chat_name, String chat_content, String time, int itemViewType) {
        this.chat_name = chat_name;
        this.chat_content = chat_content;
        this.time = time;
        this.itemViewType = itemViewType;
    }
    public String getChat_name() {
        return chat_name;
    }

    public void setChat_name(String chat_name) {
        this.chat_name = chat_name;
    }

    public String getChat_content() {
        return chat_content;
    }

    public void setChat_content(String chat_content) {
        this.chat_content = chat_content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getItemViewType() {
        return itemViewType;
    }

    public void setItemViewType(int itemViewType) {
        this.itemViewType = itemViewType;
    }


}
