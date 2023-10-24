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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;



public class Twitter extends Fragment {

    private static final String TAG = "Twitter";
    private static final String VolleyTwitterTAG = "twitter";
    private RecyclerView recyclerView;
    public RequestQueue requestQueue;
    private RecyclerVIew_Twitter adapter;
    private ArrayList<Model_Twitter> modelTwitters = new ArrayList<>();
    private Context context;
    private SaveImage saveImage;
    private boolean isNullAdded = false;
    private boolean first = true;

    private DatabaseReference image_database;

    private ShimmerFrameLayout shimmerFrameLayout;

    public Twitter() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        Log.d(">>>>>>>>>>>>Fragment2", "thread = " + Thread.currentThread().getName());

        View view = inflater.inflate(R.layout.fragment_twitter, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_twitter);
        shimmerFrameLayout = view.findViewById(R.id.twitter_shimmer);
        shimmerFrameLayout.startShimmer();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        context = getContext();
        Context con = getActivity();

        image_database = Database_Singleton.getInstance().getImageDatabase();

//        AndroidNetworking.initialize(context);

//        requestQueue = VolleySingleton_Twitter.getInstance(con).getRequestQueue();

        requestQueue = Volley.newRequestQueue(requireContext(), new OkHttpStack());
//        requestQueue = Volley.newRequestQueue(con);

        saveImage = new SaveImage();

        loadData();
        //startAsyncTask();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == modelTwitters.size() - 1) {
                    loadData();
                    //startAsyncTask();
                }
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    public void startAsyncTask() {
        LoadingDataTwitter loadingData = new LoadingDataTwitter(this);
        loadingData.execute();
    }

    private void initAdapter() {

        adapter = new RecyclerVIew_Twitter(modelTwitters, context);
        recyclerView.setAdapter(adapter);
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);

        first = false;

        // Interface
        adapter.setOnItemClickedListener(new Twitter_Interface() {
            @Override
            public void onItemClick_T(Bitmap bitmap) {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
                    } else {
                        saveImage.dataSave(bitmap, context);
                    }
                } else {
                    saveImage.dataSave(bitmap, context);
                }
            }

            @Override
            public void onItemPosition_T(int position) {
                String post_url = modelTwitters.get(position).getPost_url();
                saveImage.ShareIntent(post_url, context);
            }

            @Override
            public void onItemBulk_T(int position, Bitmap bitmap) {

                String bitString = BitMapToString(bitmap);
                String title = modelTwitters.get(position).getPost_text();
                String id = image_database.push().getKey();
                ImageModel imageModel = new ImageModel(bitString, id, title);
                image_database.child("Memes").child(id).setValue(imageModel);
                Toast.makeText(context, "Bookmark added", Toast.LENGTH_SHORT).show();
            }
        });

        //// \\
    }

    private void loadData() {

        // Adding Null
        if (!first && !isNullAdded) {
            recyclerView.post(() -> {
                modelTwitters.add(null);
                adapter.notifyItemInserted(modelTwitters.size() - 1);
                isNullAdded = true;
                Log.d(TAG, "null addeed");
            });
        }

        //
//        String api_url = "https://ms-twitter-meme.azurewebsites.net/7";
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, "https://ms-twitter-meme.azurewebsites.net/7", null, response -> {
            if (response.has("memes")) {
                try {
                    JSONArray dataArray = response.getJSONArray("memes");
                    for (int i = 0; i < dataArray.length(); i++) {

                        JSONObject jsonObject = dataArray.getJSONObject(i);
                        String post_media_url = jsonObject.getString("post_media_url");
                        if (!post_media_url.contains("video")) {
                            String post_user = jsonObject.getString("post_user");

                            String post_text = jsonObject.getString("post_text");
                            String post_url_profile_url = jsonObject.getString("post_url_profile_url");
                            String post_url = jsonObject.getString("post_url");
                            int post_likes = jsonObject.getInt("post_likes");
                            int post_retweet = jsonObject.getInt("post_retweet");

                            Model_Twitter model_twitter = new Model_Twitter(post_user, post_media_url, post_text, post_url_profile_url, post_url, post_likes, post_retweet);

                            if (isNullAdded) {
                                modelTwitters.remove(modelTwitters.size() - 1);
                                int scrollPosition = modelTwitters.size();
                                adapter.notifyItemRemoved(scrollPosition);
                                Log.d(TAG, "null removed ");
                                isNullAdded = false;
                            }
                            modelTwitters.add(model_twitter);
                        }
                    }

                    if (first) {
                        initAdapter();
                    }

                    if (!first) {
                        recyclerView.post(() -> {
                            adapter.notifyItemInserted(modelTwitters.size());
                            Log.d(TAG, "Notify item inserted");
//                                    isLoading = false;
                        });
                    }

                    Log.d(">>>>>>>>>>>>>>ThreadCount Twitter", "" + Thread.activeCount());

                } catch (JSONException e) {
                    Log.d(">>>>>>>JSONException", "" + e);
                }
            } else {
                Log.d(">>>>>>>Twitter", "json !memes");
                loadData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(">>>>>>> Twitter ErrorResponse", "" + error);
            }
        });

        objectRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 5000, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(objectRequest);
        //

        /*
        // Android Networking
        AndroidNetworking.get("https://ms-twitter-meme.azurewebsites.net/7/")
                .setTag("twitter_load")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(!response.has("memes")){
                            Log.d("FastLibrary","No Memes");
                            AndroidNetworking.forceCancel("twitter_load");
                            loadData();
                        }
                        else{
                            try {
                                JSONArray dataArray = response.getJSONArray("memes");
                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject = dataArray.getJSONObject(i);
                                    String post_media_url = jsonObject.getString("post_media_url");
                                    if(!post_media_url.contains("video")){
                                        String post_user = jsonObject.getString("post_user");

                                        String post_text = jsonObject.getString("post_text");
                                        String post_url_profile_url = jsonObject.getString("post_url_profile_url");
                                        String post_url = jsonObject.getString("post_url");
                                        int post_likes = jsonObject.getInt("post_likes");
                                        int post_retweet = jsonObject.getInt("post_retweet");

                                        Model_Twitter model_twitter = new Model_Twitter(post_user, post_media_url, post_text, post_url_profile_url, post_url, post_likes, post_retweet);

                                        if(isNullAdded){
                                            modelTwitters.remove(modelTwitters.size()-1);
                                            int scrollPosition = modelTwitters.size();
                                            adapter.notifyItemRemoved(scrollPosition);
                                            Log.d(TAG,"null removed ");
                                            isNullAdded = false;
                                        }

                                        modelTwitters.add(model_twitter);
                                    }

                                }

                                if(first){
                                    initAdapter();
                                }

                                if (!first){
                                    recyclerView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.notifyItemInserted(modelTwitters.size());
                                            Log.d(TAG,"Notify item inserted");
//                                    isLoading = false;
                                        }
                                    });
                                }

                            } catch (JSONException e) {
                                Log.d(">>>>>>>JSONException",""+e);

                            }
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(">>>>>>>FastLibrary 2",""+anError);
                    }
                });
        //

         */

    }

    public void addNull() {
        if (!first && !isNullAdded) {
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    //
                    modelTwitters.add(null);
                    adapter.notifyItemInserted(modelTwitters.size() - 1);
                    isNullAdded = true;
                    Log.d(TAG, "null added");
                    //
                }
            });
        }
    }


    private void removeNull() {

        if (isNullAdded) {
            modelTwitters.remove(modelTwitters.size() - 1);
            int scrollPosition = modelTwitters.size();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyItemRemoved(scrollPosition);
                }
            });

            Log.d(TAG, "null removed");
            isNullAdded = false;
        }
    }

    public void updatedData() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemInserted(modelTwitters.size());
                Log.d(TAG, "Notify item inserted");
            }
        });
    }


    private static class LoadingDataTwitter extends AsyncTask<Void, Void, Boolean> {

        private final WeakReference<Twitter> twitterWeakReference;

        LoadingDataTwitter(Twitter activity) {
            twitterWeakReference = new WeakReference<Twitter>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d(">>>>>>>>>>>onPreExecute", "");

            Log.d(">>>>>>onPreExecute async", "thread = " + Thread.currentThread().getName());

            Twitter reference = twitterWeakReference.get();

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

            Twitter reference = twitterWeakReference.get();

            if (reference.first) {
                reference.initAdapter();
            } else {
                reference.updatedData();
            }
        }

        private boolean getData() {

            Twitter reference = twitterWeakReference.get();

            /*
            Retrofit retrofit = new Retrofit.Builder()
                    .callbackExecutor(Executors.newSingleThreadExecutor())
                    .baseUrl("https://ms-twitter-meme.azurewebsites.net/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceholder jsonPlaceholder = retrofit.create(JsonPlaceholder.class);
            Call<List<Model_Twitter>> call = jsonPlaceholder.getTwitterModel();
            try {

                retrofit2.Response<List<Model_Twitter>> res = call.execute();
                if(res.isSuccessful()){
                    List<Model_Twitter> d = res.body();
                    for(Model_Twitter m : d){

                        String post_media_url = m.getPost_media_url();
                        if (!post_media_url.contains("video")) {
                            String post_user = m.getPost_user();

                            String post_text = m.getPost_text();
                            String post_url_profile_url =m.getPost_url_profile_url();
                            String post_url = m.getPost_url();
                            int post_likes = m.getPost_likes();
                            int post_retweet = m.getPost_retweet();

                            Model_Twitter model_twitter = new Model_Twitter(post_user, post_media_url, post_text, post_url_profile_url, post_url, post_likes, post_retweet);

                            if (reference.isNullAdded) {
                                reference.removeNull();
                            }
                            reference.modelTwitters.add(model_twitter);
                            Log.d(">>>>>>inside volley", "thread = " + Thread.currentThread().getName());
                        }
                    }
                }else {
                    getData();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

             */



            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://ms-twitter-meme.azurewebsites.net/7", new JSONObject(), future, future);
            request.setTag(VolleyTwitterTAG);
            request.getPriority();

            request.setRetryPolicy(new DefaultRetryPolicy(5000, 5000, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            reference.requestQueue.add(request);

            try {
                JSONObject response = future.get();// this will block

                if (!response.has("memes")) {
                    Log.d(">>>>>>>>Twitter", "!json memes");
                    getData();
                } else {
                    JSONArray dataArray = response.getJSONArray("memes");
                    for (int i = 0; i < dataArray.length(); i++) {

                        JSONObject jsonObject = dataArray.getJSONObject(i);
                        String post_media_url = jsonObject.getString("post_media_url");
                        if (!post_media_url.contains("video")) {
                            String post_user = jsonObject.getString("post_user");

                            String post_text = jsonObject.getString("post_text");
                            String post_url_profile_url = jsonObject.getString("post_url_profile_url");
                            String post_url = jsonObject.getString("post_url");
                            int post_likes = jsonObject.getInt("post_likes");
                            int post_retweet = jsonObject.getInt("post_retweet");

                            Model_Twitter model_twitter = new Model_Twitter(post_user, post_media_url, post_text, post_url_profile_url, post_url, post_likes, post_retweet);

                            if (reference.isNullAdded) {
                                reference.removeNull();
                            }
                            reference.modelTwitters.add(model_twitter);
                            Log.d(">>>>>>inside volley", "thread = " + Thread.currentThread().getName());
                        }
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


    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
        Log.d("Twitter", "onDestroy");
//        requestQueue.stop();
//        requestQueue.cancelAll(context);
    }


    //    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if(isVisibleToUser){
//            loadData();
//        }
//    }
}
