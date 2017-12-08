package kr.ac.hifly.attention.main;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
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


    public RequestManager mGlideRequestManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGlideRequestManager = Glide.with(this);
    }

    //////////////////////////////////////// 프사 바꾸기
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
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        try{

            mGlideRequestManager.load(data.getData()).apply(RequestOptions.bitmapTransform(new CircleCrop())).thumbnail(0.1f).into(fourth_fragment_profile_Item_Image);

            ////////////////성원 파이어베이스 올리기
            FirebaseStorage storage = FirebaseStorage.getInstance("gs://attention-469ab.appspot.com");

            StorageReference storageRef = storage.getReference();
            //StorageReference Profile_Image = storageRef.child(fourth_fragment_profile_Item_Image.toString());
            StorageReference Profile_Image = storageRef.child(Values.myUUID + "/profile/profile.jpg");

           // Drawable d = fourth_fragment_profile_Item_Image.getDrawable();


            fourth_fragment_profile_Item_Image.setDrawingCacheEnabled(true);
            fourth_fragment_profile_Item_Image.buildDrawingCache();
//          Bitmap bitmap = fourth_fragment_profile_Item_Image.getDrawingCache();
            Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),data.getData() );
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] profile_data = baos.toByteArray();

            UploadTask uploadTask = Profile_Image.putBytes(profile_data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });


            //////////////////////////////////////////////////

//            // Get the data from an ImageView as bytes
//            imageView.setDrawingCacheEnabled(true);
//            imageView.buildDrawingCache();
//            Bitmap bitmap = imageView.getDrawingCache();
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            byte[] data = baos.toByteArray();
//
//            UploadTask uploadTask = mountainsRef.putBytes(data);
//            uploadTask.addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle unsuccessful uploads
//                }
//            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                }
//            });

        }catch (Exception e) {
            Log.e("test", e.getMessage());
        }
    }
    ////////////////////////프사 바꾸기





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

