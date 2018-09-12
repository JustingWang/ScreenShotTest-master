package com.example.zhongyu.screenshottest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ScrollView scrollView;
    private TextView shoot;
    private  FastScrollWebView test_web;
    private Button id_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }
        id_edit=(Button)findViewById(R.id.id_edit);
        test_web=(FastScrollWebView)findViewById(R.id.test_web);
//        shoot=(TextView)findViewById(R.id.shoot);
        scrollView = (ScrollView) findViewById(R.id.scroll);
        test_web.setWebViewClient(new CustomWebViewClient());

        //链接js注入接口，使能选中返回数据
//        test_web.linkJSInterface();



        test_web.getSettings().setBuiltInZoomControls(true);
        test_web.getSettings().setDisplayZoomControls(false);
        //使用javascript
        test_web.getSettings().setJavaScriptEnabled(true);
        test_web.getSettings().setDomStorageEnabled(true);
        test_web.loadUrl("http://www.fae.cn:11888/mobile_server_is/FlfgServlet?type=flfg-link-detail&id=2080289&background=0&fontSize=2&pid=4fd4326c-3eec-491c-824d-cfac7dd514d8");

        id_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_edit.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("11111");
                     startActivity(new Intent(MainActivity.this,LoadBigPic.class).putExtra("picUrl",ScreenShot.ScreenUtils.savePic(MainActivity.this, ScreenShot.ScreenUtils.compressImage(ScreenShot.ScreenUtils.getBitmapByView(scrollView)))));
                    }
                }, 1000);

            }
        });
    }

    private class CustomWebViewClient extends WebViewClient {

        private boolean mLastLoadFailed = false;

        @Override
        public void onPageFinished(WebView webView, String url) {
            super.onPageFinished(webView, url);
            if (!mLastLoadFailed) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ScreenShot.ScreenUtils.savePic(MainActivity.this, ScreenShot.ScreenUtils.compressImage(ScreenShot.ScreenUtils.getBitmapByView(scrollView)));
                    }
                }, 1000);


            }
        }

        @Override
        public void onPageStarted(WebView webView, String url, Bitmap favicon) {
            super.onPageStarted(webView, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            mLastLoadFailed = true;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }



    private static String TAG = "Listview and ScrollView item 截图:";









}
