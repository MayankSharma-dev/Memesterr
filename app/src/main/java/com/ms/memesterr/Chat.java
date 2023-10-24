package com.ms.memesterr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Chat extends AppCompatActivity {

    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        web = findViewById(R.id.web_view);
        web.loadUrl("https://www.omegle.com/");
        // this will enable the javascript.
        //web.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        web.setWebViewClient(new WebViewClient());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TAG","Chat onDestory initiated");
        web.clearCache(true);
        WebStorage.getInstance().deleteAllData();

        // Clear all the cookies
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();

        web.clearCache(true);
        web.clearFormData();
        web.clearHistory();
        web.clearSslPreferences();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}