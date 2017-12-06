package kr.ac.hifly.attention.main;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import hifly.ac.kr.attention.R;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import kr.ac.hifly.attention.adapter.Main_Configuration_RecyclerView_Adapter;
import kr.ac.hifly.attention.adapter_item.Main_Configuration_RecyclerView_Item;
import kr.ac.hifly.attention.value.Values;

public class Main_Configuration_Fragment extends Fragment {

    private RecyclerView fourth_RecyclerView;
    private Main_Configuration_RecyclerView_Adapter main_configuration_recyclerView_adapter;
    private List<Main_Configuration_RecyclerView_Item> main_Configuration_RecyclerView_Items;
    private ImageView fourth_item_image;
    private ImageView fourth_fragment_profile_Item_Image;
    private int REQEUST_OK = 102;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = (LinearLayout) inflater.inflate(R.layout.fragment_fourth,container,false);
        view = (LinearLayout) inflater.inflate(R.layout.main_configuration_fragment, container, false);

        fourth_item_image = (ImageView) view.findViewById(R.id.fourth_RecyclerView_Item_Image);
        fourth_RecyclerView = (RecyclerView) view.findViewById(R.id.fourth_RecyclerView);

        fourth_fragment_profile_Item_Image = (ImageView) view.findViewById(R.id.fourth_fragment_profile_Item_Image);
        fourth_fragment_profile_Item_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQEUST_OK);
            }
        });

        setRecyclerView();

        return view;
    }





    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setRecyclerView() {

        main_Configuration_RecyclerView_Items = new ArrayList<Main_Configuration_RecyclerView_Item>();
        main_Configuration_RecyclerView_Items.add(new Main_Configuration_RecyclerView_Item("공지사항", R.drawable.main_configuration_item_speaker));
        main_Configuration_RecyclerView_Items.add(new Main_Configuration_RecyclerView_Item("버전정보", R.drawable.main_configuration_item_i));
        main_Configuration_RecyclerView_Items.add(new Main_Configuration_RecyclerView_Item("도움말", R.drawable.main_configuration_item_qna));
        main_Configuration_RecyclerView_Items.add(new Main_Configuration_RecyclerView_Item("개발자", R.drawable.main_configuration_item_developer));
        main_Configuration_RecyclerView_Items.add(new Main_Configuration_RecyclerView_Item("동기화", R.drawable.main_configuration_item_refresh));

        main_configuration_recyclerView_adapter = new Main_Configuration_RecyclerView_Adapter(getContext(), main_Configuration_RecyclerView_Items);
        fourth_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        fourth_RecyclerView.setAdapter(main_configuration_recyclerView_adapter);
    }

}

