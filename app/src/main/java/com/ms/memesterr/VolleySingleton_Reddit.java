package com.ms.memesterr;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton_Reddit {
    private static VolleySingleton_Reddit mInstance;
    private final RequestQueue mRequestQueue;
    private VolleySingleton_Reddit(Context context){
        mRequestQueue = Volley.newRequestQueue(context,new OkHttpStack());
    }
    public  static synchronized VolleySingleton_Reddit getInstance(Context context){
        if (mInstance==null){
            mInstance = new VolleySingleton_Reddit(context);
        }
        return mInstance;
    }
    public RequestQueue getsRequestQueue(){
        return mRequestQueue;
    }
}
