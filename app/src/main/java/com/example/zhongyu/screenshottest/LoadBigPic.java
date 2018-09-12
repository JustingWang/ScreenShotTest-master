package com.example.zhongyu.screenshottest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LoadBigPic extends AppCompatActivity {
    private LongImageView image_test;
    private VerticalSeekbar seek_test;
    private String picUrl;
    private Button test_btn,test_back;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_big_pic);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }
        test_back=(Button)findViewById(R.id.test_back);
        test_btn=(Button)findViewById(R.id.test_btn);
        seek_test=(VerticalSeekbar)findViewById(R.id.seek_test);
        picUrl=getIntent().getStringExtra("picUrl");
        System.out.println("picUrl---->"+picUrl);
        image_test=(LongImageView)findViewById(R.id.image_test);
        bitmap = BitmapFactory.decodeFile(picUrl);
        image_test.setBitmap(bitmap,seek_test);
        //设置触摸监听
        image_test.setCoclor(Color.BLUE);
        image_test.setStrokeWidth(10);
        test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_test.setStrokeWidth(20);
                image_test.setCoclor(Color.parseColor("#DFEBE7"));
            }
        });
        test_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_test.setClear();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
       bitmap.recycle();
    }
}
