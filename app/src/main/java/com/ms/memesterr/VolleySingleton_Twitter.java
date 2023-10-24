package com.ms.memesterr;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton_Twitter {

    private static VolleySingleton_Twitter mInstance;
    private final RequestQueue mRequestQueue;
    private VolleySingleton_Twitter(Context context){
        mRequestQueue = Volley.newRequestQueue(context,new OkHttpStack());
    }
    public  static synchronized VolleySingleton_Twitter getInstance(Context context){
        if (mInstance==null){
            mInstance = new VolleySingleton_Twitter(context);
        }
        return mInstance;
    }
    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }

}
