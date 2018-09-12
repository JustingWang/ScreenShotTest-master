package com.example.zhongyu.screenshottest;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：wangsai
 * 时间：2018/9/4 0004:15:52
 * 邮箱：1729340905@qq.com
 * 说明：
 */

public class LongImageView extends View {
    private static final float TOUCH_TOLERANCE = 4;


    private int width, height;
    private Path mPath;

    private VerticalSeekbar seek_test;

    //需要绘制的Bitmap
    private Bitmap bitmap;
    /**
     * 需要绘制的图片的区域
     */
    private Rect srcRect;

    /**
     * 绘制的区域
     */
    private RectF dstRectF;

    /**
     * 画笔
     */
    private Paint paint;
    private float clickX = 0,clickY = 0;
    private float startX = 0,startY = 0;
    private boolean isMove = true;
    private boolean isClear = false;
    private int color ;
    private float strokeWidth ;
    private int apha=255;
    /**
     * 是否需要滑动
     */
    private boolean isNeedSlide;

    /**
     * 已经滑动过的距离
     */
    private float slideLength;
    private Bitmap new2Bitmap = null;

    private Bitmap originalBitmap;
    private Canvas canvas = null;
    private List<Canvas>canvasList=new ArrayList<Canvas>();
    /**
     * 绘制的Bitmap
     */
    private Bitmap drawBitmap,new1Bitmap;
    {
        srcRect = new Rect();
        dstRectF = new RectF();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10.0f);
    }

    public LongImageView(Context context) {
        super(context);
    }

    public LongImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LongImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LongImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public void  setCoclor(int coclor){
       this.color=coclor;
    }
    public void  setStrokeWidth(float StrokeWidth){
        this.strokeWidth=StrokeWidth;
    }

    public void  setApha(int apha){
        this.apha=apha;

    }


    /**
     * 设置Bitmap
     *
     * @param bitmap
     *     需要绘制的Bitmap
     */
    public void setBitmap(Bitmap bitmap,VerticalSeekbar seekBar) {
        this.bitmap = bitmap;
        this.seek_test=seekBar;
        originalBitmap = bitmap.copy(Bitmap.Config.ARGB_4444, true);
        new1Bitmap = Bitmap.createBitmap(originalBitmap);
        new2Bitmap=Bitmap.createBitmap(originalBitmap);
        seek_test.setMax(new1Bitmap.getHeight());
        seek_test.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float moveY =new1Bitmap.getHeight()- progress;
                float distance = moveY - lastY;
                lastY = moveY;
                slideLength += distance;
                if (slideLength >= 0) {
                    slideLength = 0;
                }
                if (slideLength <= (-1) * (drawBitmap.getHeight() - height)) {
                    slideLength = (-1) * (drawBitmap.getHeight() - height);
                }
                postInvalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        width = getPaddingLeft() + getPaddingRight() + specSize;
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        height = getPaddingTop() + getPaddingBottom() + specSize;
        if (drawBitmap == null) {
            drawBitmap = resizeImage(bitmap, width);
            if (drawBitmap.getHeight() > 1.5 * height) {
                //需要滑动
                setNeedSlide(true);
            } else {
                //不需要滑动
                setNeedSlide(false);
                srcRect.left = 0;
                srcRect.top = 0;
                srcRect.right = drawBitmap.getWidth();
                srcRect.bottom = drawBitmap.getHeight();
                if (drawBitmap.getHeight() > height) {
                    drawBitmap = resizeImageH(drawBitmap, height - 20);
                } else {
                    float space = (height - drawBitmap.getHeight());
                    dstRectF.left = 0;
                    dstRectF.top = space;
                    dstRectF.right = width;
                    dstRectF.bottom = height - space;
                }
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isClear){
            canvas.drawBitmap(HandWriting(new2Bitmap),(width - drawBitmap.getWidth()) / 2,slideLength, paint);
        }else {
            canvas.drawBitmap(HandWriting(new1Bitmap),(width - drawBitmap.getWidth()) / 2,slideLength, paint);
        }
    }

    /**
     * 设置是否需要滑动
     *
     * @param needSlide
     *     true or false
     */
    public void setNeedSlide(boolean needSlide) {
        isNeedSlide = needSlide;
    }

    /**
     * 触摸操作的坐标
     */
    private float lastX;
    private float lastY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mPath = new Path();
        if (!isNeedSlide) {
            return super.onTouchEvent(event);
        }
        clickX = event.getX();
        clickY = event.getY();
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //按下
                lastX = event.getX();
                lastY = event.getY();
                isMove = false;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                isMove = true;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }
        return true;
    }

    public Bitmap resizeImage(Bitmap bitmap, int w) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) w) / width;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleWidth);
        return Bitmap.createBitmap(bitmap, 0, 0, width,
                height, matrix, true);
    }
    public Bitmap resizeImageH(Bitmap bitmap, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) h) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleWidth);
        return Bitmap.createBitmap(bitmap, 0, 0, width,
                height, matrix, true);
    }
    public Bitmap HandWriting(Bitmap originalBitmap)
    {
        if(isClear){
            canvas = new Canvas(new2Bitmap);
        }
        else{
            canvas = new Canvas(originalBitmap);
        }
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        if(isMove){
            canvas.drawLine(startX, startY+(Math.abs(slideLength)), clickX, clickY+(Math.abs(slideLength)), paint);
        }
        startX = clickX;
        startY = clickY;
        if(isClear){
            return new2Bitmap;
        }
        return originalBitmap;
    }
     public void setClear(){
         canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        setBitmap(bitmap,seek_test);





     }

}
