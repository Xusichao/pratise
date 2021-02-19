package com.xsic.personalpratise.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

public class WaveView extends View {
    static final int RADIUS = 150;
    static final int WAVE_HEIGHT = 30;
    static final int WAVE_WIDTH = 60;
    private float mWaveWidth = 0;


    private Paint mPaint = new Paint();
    private Path mBazierPath_1 = new Path();
    private Path mBazierPath_2 = new Path();


    public WaveView(Context context) {
        this(context, null);
        init();
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int finalHeight,finalWidth;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY){
            finalWidth = widthSize;
        }else {
            finalWidth = RADIUS*2;
            if (widthMode == MeasureSpec.AT_MOST){
                finalWidth = Math.max(finalWidth, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY){
            finalHeight = widthSize;
        }else {
            finalHeight = RADIUS*2;
            if (heightMode == MeasureSpec.AT_MOST){
                finalHeight = Math.max(finalHeight, heightSize);
            }
        }

        setMeasuredDimension(finalWidth,finalHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int progress = 50;
        for (int i=0;mWaveWidth>getWidth();i++){

        }

    }


}
