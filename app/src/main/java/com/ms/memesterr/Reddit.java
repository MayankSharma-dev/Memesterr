package com.ms.memesterr;

import android.Manifest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DatabaseReference;
import com.player.autoplayer.AutoPlayerManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class Reddit extends Fragment {


    private static final String TAG = "Reddit";
    private static final String VolleyRedditTAG = "reddit";

    private RecyclerView recyclerView;
    private RecyclerView_Reddit adapter;
    private ArrayList<Model_Reddit> modelReddit = new ArrayList<>();
    private RequestQueue queue;


    private boolean isNullAdded = false;

    private Context context;

    private boolean first = true;

    private final SaveImage save = new SaveImage();

    private DatabaseReference imageDatabase;

    private ShimmerFrameLayout shimmer;
    String link = "https://ms-reddit-meme.azurewebsites.net/7";



    public Reddit() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(">>>>>Reddit", "onCreateView");

        Log.d(">>>>>>Fragment1", "thread = " + Thread.currentThread().getName());

        View view = inflater.inflate(R.layout.fragment_reddit, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_reddit);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        imageDatabase = Database_Singleton.getInstance().getImageDatabase();

        shimmer = view.findViewById(R.id.shimmer);

        context = getContext();
        Context con = getActivity();

//        queue = VolleySingleton_Reddit.getInstance(con).getsRequestQueue();
        queue = Volley.newRequestQueue(con,new OkHttpStack());
//        queue = Volley.newRequestQueue(con);
//        AndroidNetworking.initialize(context);


        shimmer.startShimmer();
        shimmer.setVisibility(View.VISIBLE);

        AutoPlayerManager autoPlayerManager = new AutoPlayerManager(this);
        autoPlayerManager.setAutoPlayerId(R.id.post_video);
        autoPlayerManager.setUseController(true);
        autoPlayerManager.attachRecyclerView(recyclerView);
        autoPlayerManager.setup();
        //loadData();
        Log.d(">>>>>>>>>>>>>>ThreadCount before",""+Thread.activeCount());
        startAsyncTask();
        Log.d(">>>>>>>>>>>>>>ThreadCount after",""+Thread.activeCount());


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == modelReddit.size() - 1) {
                    //bottom of list
                    //loadData();
                    startAsyncTask();
                    Log.d(">>>>>>>>>>>>>>ThreadCount",""+Thread.activeCount());
                }
            }
        });

        return view;
    }


    private void initAdapter() {


        Log.d(">>>>>>>", ">>>init1");
        Log.d(">>>>>>initFunc", "thread = " + Thread.currentThread().getName());
        if (modelReddit.isEmpty()) {
            Log.d(">>>>>>>>>>>>>", "EmptyList");
        } else {
            adapter = new RecyclerView_Reddit(modelReddit, context);
            recyclerView.setAdapter(adapter);

            // Interface
            adapter.setOnItemClickListener(new Reddit_Interface() {
                @Override
                public void onItemClick_R(Bitmap bitmap) {

                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
                        } else {
                            save.dataSave(bitmap, context);
                        }
                    } else {
                        save.dataSave(bitmap, context);
                    }

                }

                @Override
                public void onItemPosition_R(int position) {
                    String post_url = modelReddit.get(position).getPost_link();
                    save.ShareIntent(post_url, context);
                }

                @Override
                public void onItemBulk_R(int position, Bitmap bitmap) {
                    String bitString = BitMapToString(bitmap);
                    String title = modelReddit.get(position).getTitle();
                    String id = imageDatabase.push().getKey();
                    ImageModel imageModel = new ImageModel(bitString, id, title);
                    imageDatabase.child("Memes").child(id).setValue(imageModel);
                    Toast.makeText(context, "Bookmark added", Toast.LENGTH_SHORT).show();
                }
            });
            //// \\

            shimmer.stopShimmer();
            shimmer.setVisibility(View.GONE);
            first = false;
            Log.d(">>>>>>>", ">>>shimmer.stop");
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Write Permission Given \n Please save again.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Write Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
    private void loadData() {
        Log.d(">>>>>>>>>Reddit","LoadData");

        if (!first && !isNullAdded) {

            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    Model_Reddit addingNull = new Model_Reddit(0);
                    modelReddit.add(addingNull);
                    adapter.notifyItemInserted(modelReddit.size() - 1);
                    isNullAdded = true;
                    Log.d(TAG, "null added");
                }
            });
        }



//        String dataurl = "http://ms-reddit-meme.azurewebsites.net/5";
        String dataUrl = "https://ms-reddit-meme.azurewebsites.net/7";
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, dataUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                if(!response.has("memes")){
                    Log.d(">>>>>>>>Reddit","!json memes");
                    loadData();
                }
                else {

                    try {
                        int scrollPosition = 0;
                        JSONArray jsonArray = response.getJSONArray("memes");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String subreddit = jsonObject.getString("subreddit");
                            String title = jsonObject.getString("title");
                            String author = jsonObject.getString("author");
                            String url = jsonObject.getString("url");
                            String ups = jsonObject.getString("ups");
                            String post_url = jsonObject.getString("post_link");
                            String preview = "";

                            if (jsonObject.getJSONArray("image_previews").length() != 0) {
                                JSONArray array = jsonObject.getJSONArray("image_previews");
                                if (array.length() != 0) {
                                    for (int j = array.length() - 1; j > array.length() - 2; j--) {
                                        preview = array.getString(j);
                                        Log.d("preview", "" + preview);
                                    }
                                }
                            }

                            int type = 0;

                            if (!url.contains(".jpg") && !url.contains(".png") && !url.contains(".gif")) {
                                type = 2;
                            } else {
                                type = 1;

                            }

                            Model_Reddit model_reddit = new Model_Reddit(ups, subreddit, title, url, author, post_url, type, preview);

                            if (isNullAdded) {
                                modelReddit.remove(modelReddit.size() - 1);
                                scrollPosition = modelReddit.size();
                                adapter.notifyItemRemoved(scrollPosition);
                                Log.d(TAG, "null removed ");
                                isNullAdded = false;
                            }
                            modelReddit.add(model_reddit);
                        }

                        if (!first) {

                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(">>>>Notify", "");
                                    adapter.notifyItemInserted(modelReddit.size());
                                    Log.d(TAG, "Notify item inserted");
                                }
                            });
                        }

                        if (first) {
                            initAdapter();
                        }

                    } catch (JSONException e) {
                        Log.d(">>>>>Catch", "" + e);
//                        loadData();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(">>>>>>>>>>>>>Reddit ErrorResponse", "" + error);
                if(first){
                    loadData();
                }
            }
        });

        objectRequest.setTag(VolleyTAG);

        objectRequest.setRetryPolicy(new DefaultRetryPolicy(8000,
                5000, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(objectRequest);

    }*/



    public void updatedData() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                Log.d(">>>>Notify", "");
                adapter.notifyItemInserted(modelReddit.size());
                Log.d(TAG, "Notify item inserted");
            }
        });
    }

    public void addNull() {
        if (!first && !isNullAdded) {
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    Model_Reddit addingNull = new Model_Reddit(0);
                    modelReddit.add(addingNull);
                    adapter.notifyItemInserted(modelReddit.size() - 1);
                    isNullAdded = true;
                    Log.d(TAG, ">>>>>>>null added");
                }
            });
        }
    }

    private void removeNull(){
        if (isNullAdded) {
            modelReddit.remove(modelReddit.size() - 1);
            int scrollPosition = modelReddit.size();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyItemRemoved(scrollPosition);
                }
            });

            Log.d(TAG, "null removed ");
            isNullAdded = false;
//            recyclerView.post(new Runnable() {
//                @Override
//                public void run() {
//                    modelReddit.remove(modelReddit.size() - 1);
//                    int scrollPosition = modelReddit.size();
//                    adapter.notifyItemRemoved(scrollPosition);
//                    Log.d(TAG, "null removed ");
//                    isNullAdded = false;
//                }
//            });
        }
    }


    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public void startAsyncTask() {
        LoadingData loadingData = new LoadingData(this);
        loadingData.execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        queue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
        Log.d("Reddit", "onDestroy");

    }


    private static class LoadingData extends AsyncTask<Void, Void, Boolean> {

        private final WeakReference<Reddit> redditWeakReference;

        LoadingData(Reddit activity) {
            redditWeakReference = new WeakReference<Reddit>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d(">>>>>>>>>>>onPreExecute", "");

            Log.d(">>>>>>onPreExecute async", "thread = " + Thread.currentThread().getName());

            Reddit reference = redditWeakReference.get();

            if (!reference.first) {
                reference.addNull();
                Log.d(">>>>>>>>>>>onPreExecute", "null");
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.d(">>>>>>>>>>>onBackground", "");

            getData();

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Log.d(">>>>>>>>>>>onPost", "");

            Reddit reference = redditWeakReference.get();

                if (reference.first) {
                    reference.initAdapter();
                } else {
                    reference.updatedData();
                }
        }

        private boolean getData() {

            Reddit reference = redditWeakReference.get();

            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://ms-reddit-meme.azurewebsites.net/7", new JSONObject(), future, future);
            request.setTag(VolleyRedditTAG);
            request.getPriority();

            request.setRetryPolicy(new DefaultRetryPolicy(8000,
                    5000, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            reference.queue.add(request);

            try {
                JSONObject response = future.get();// this will block
                if (!response.has("memes")) {
                    Log.d(">>>>>>>>Reddit", "!json memes");
                    getData();
                } else {
                    JSONArray jsonArray = response.getJSONArray("memes");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String subreddit = jsonObject.getString("subreddit");
                        String title = jsonObject.getString("title");
                        String author = jsonObject.getString("author");
                        String url = jsonObject.getString("url");
                        String ups = jsonObject.getString("ups");
                        String post_url = jsonObject.getString("post_link");
                        String preview = "";

                        if (jsonObject.getJSONArray("image_previews").length() != 0) {
                            JSONArray array = jsonObject.getJSONArray("image_previews");
                            if (array.length() != 0) {
                                for (int j = array.length() - 1; j > array.length() - 2; j--) {
                                    preview = array.getString(j);
//                                            Log.d("preview", "" + preview);
                                }
                            }
                        }
                        Log.d(">>>>>load", "model_reddit previous");

                        int type;

                        if (!url.contains(".jpg") && !url.contains(".png") && !url.contains(".gif")) {
                            type = 2;
                        } else {
                            type = 1;
                        }

                        Model_Reddit model_reddit = new Model_Reddit(ups, subreddit, title, url, author, post_url, type, preview);
                        Log.d(">>>>>load", "model_reddit");

                        if (reference.isNullAdded) {
                            reference.removeNull();
                        }
                        reference.modelReddit.add(model_reddit);
                        Log.d(">>>>>>inside volley", "thread = " + Thread.currentThread().getName());
                    }
                }

            } catch (InterruptedException e) {
                Log.d(">>>>>>>>>>>>> Sync1", "" + e);
                // exception handling
            } catch (ExecutionException e) {
                Log.d(">>>>>>>>>>>>> Sync2", "" + e);
                // exception handling
            } catch (JSONException e) {
                Log.d(">>>>>>>>>>>>> Sync3", "" + e);
                throw new RuntimeException(e);
            }

            return true;
        }

    }



}