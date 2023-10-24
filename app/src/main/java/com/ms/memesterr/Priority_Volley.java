package com.ms.memesterr;

import android.app.DownloadManager;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

public class Priority_Volley extends Request {

    private Priority mPriority = Priority.HIGH;

    public Priority_Volley(String url, Response.ErrorListener errorListener) {
        super(url, errorListener);
    }

    public Priority_Volley(int method, String url, @Nullable Response.ErrorListener errorListener) {
        super(method, url, errorListener);
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        return null;
    }

    @Override
    protected void deliverResponse(Object response) {

    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }


    @Override
    public Priority getPriority() {
        return super.getPriority();
    }
}
