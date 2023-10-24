package com.ms.memesterr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Bookmarks extends AppCompatActivity {

    DatabaseReference databaseReference;

    private final String KEY_RECYCLER_STATE = "recycler_state";
    RecyclerView recyclerView;
    ImageView blank;

    ArrayList<ImageModel> imageModels = new ArrayList<>();
    Bookmarks_Adapter adapter;

    SaveImage saveImage = new SaveImage();
    Context context;
    private ProgressBar progressBar;



    @Override
    protected void onStart() {
        super.onStart();

        Log.d(">>>>>>>>>>>>>>>","onStart");



        /*
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    imageModels.clear();
                    for(DataSnapshot data : snapshot.getChildren()){
                        ImageModel model = data.getValue(ImageModel.class);
                        imageModels.add(model);
                    }
                    Log.d("snapshot size:",""+imageModels.size());
                    textView.setVisibility(View.INVISIBLE);
//                Bookmarks_Adapter adapter = new Bookmarks_Adapter(Bookmarks.this,imageModels);
                    adapter = new Bookmarks_Adapter(Bookmarks.this,imageModels);
                    recyclerView.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        */

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(">>>>>>>>>>>>Bookmark","onResume");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState!=null){
            Log.d(">>>>>>>>>","not Empty");
            Parcelable listState = savedInstanceState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState); // Restore data found in the Bundle
        }else{
            Log.d(">>>>>>>>>","Empty");
            // No saved data, get data from remote
        }

        setContentView(R.layout.activity_bookmarks);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setSubtitle("Bookmarks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        Log.d(">>>>>>>>>>>>>>>","onCreate");

        recyclerView = findViewById(R.id.recyclerview_bookmark);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Bookmarks.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        blank = findViewById(R.id.bookmark_txt);

//        textView.setVisibility(View.VISIBLE);

        context = Bookmarks.this;

        progressBar = findViewById(R.id.bookmark_progress);
        progressBar.setVisibility(View.VISIBLE);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + auth.getCurrentUser().getUid() + "/Memes");




        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {

                    if (snapshot.exists()) {

                        imageModels.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            ImageModel model = data.getValue(ImageModel.class);
                            imageModels.add(model);
                        }
                        progressBar.setVisibility(View.GONE);
                        Log.d("snapshot size:", "" + imageModels.size());
                        blank.setVisibility(View.INVISIBLE);
                        adapter = new Bookmarks_Adapter(Bookmarks.this, imageModels);
                        recyclerView.setAdapter(adapter);

                        if(adapter!=null){
                            adapter.setOnItemClicked(new Bookmark_Interface() {
                                @Override
                                public void onItemClick(Bitmap bitmap) {
                                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                                        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
                                        } else {
                                            saveImage.dataSave(bitmap, context);
                                        }
                                    } else {
                                        saveImage.dataSave(bitmap, context);
                                    }
                                    Log.d(">>>>>>>>>>>Bookmark","Download");
                                }

                                @Override
                                public void itemPosition(int position) {
                                    String refId= imageModels.get(position).getId();
                                    databaseReference.child(refId).removeValue();
                                    imageModels.remove(position);
                                    adapter.notifyItemRemoved(position);
                                }
                            });
                        }
                    }


                } else {
                    blank.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
//        outState.putParcelable(KEY_RECYCLER_STATE, listState);
//
//        // Call superclass to save any view hierarchy.
//        super.onSaveInstanceState(outState);
//    }

}