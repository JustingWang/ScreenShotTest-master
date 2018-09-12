package com.example.zhongyu.screenshottest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 作者：wangsai
 * 时间：2018/9/7 0007:09:07
 * 邮箱：1729340905@qq.com
 * 说明：
 */

public class TuyaView extends View {
    public float currentX = 80;
    public float currentY = 60;


    public TuyaView(Context context)
    {
        super(context);
    }

    public TuyaView(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.RED);
        canvas.drawCircle(currentX, currentY, 10, p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.currentX = event.getX(); //触摸座标X
        this.currentY = event.getY(); //触摸座标Y
        invalidate();//重绘组件
        return true;//返回true：事件已处理，此处必须返回true，否则小球移动不了。
    }

}
