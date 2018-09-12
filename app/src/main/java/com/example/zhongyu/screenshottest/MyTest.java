package com.example.zhongyu.screenshottest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MyTest extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout main = new LinearLayout(this);
        main.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        main.setOrientation(LinearLayout.HORIZONTAL);
        main.setGravity(Gravity.CENTER);

        TuyaView mDrawView = new TuyaView(this);
        mDrawView.setBackgroundColor(Color.BLUE);
        main.addView(mDrawView);

        setContentView(main);//设置Aictivity 显示View
    }

}
