package com.xsic.personalpratise.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;


public class WaveView extends View {
    static final int DURATION = 1000;
    static final int PROGRESS_DURATION = 3000;
    static final int RADIUS = 500;
    static final int WAVE_HEIGHT = 100;
    static final int WAVE_WIDTH = 200;
    private float mWaveWidth = 0;
    private float mProgress = 0.30f;

    //正弦波上下定点
    private float topY = 0;
    private float bottomY = 0;
    private float offset = 0;


    private Paint mPaint = new Paint();
    private Paint mWave_1 = new Paint();
    private Paint mWave_2 = new Paint();
    private Path mBazierPath_1 = new Path();
    private Path mBazierPath_2 = new Path();
    private Path mClipPath = new Path();


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
        mPaint.setColor(Color.parseColor("#D4FAD1"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(130);
        mWave_1.setAntiAlias(true);
        mWave_1.setColor(Color.parseColor("#33F82A"));
        mWave_1.setStyle(Paint.Style.FILL);
        mWave_2.setAntiAlias(true);
        mWave_2.setColor(Color.parseColor("#89FA83"));
        mWave_2.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mClipPath.addCircle(w/2f,h/2f,RADIUS, Path.Direction.CW);
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
            finalWidth =  RADIUS*2;
        }

        if (heightMode == MeasureSpec.EXACTLY){
            finalHeight = heightSize;
        }else {
            finalHeight = RADIUS*2;
        }
        setMeasuredDimension(finalWidth,finalHeight);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initApex();
    }

    private void initApex(){
        topY = (getBottom()-getTop())*(1-mProgress)-WAVE_HEIGHT;
        bottomY = (getBottom()-getTop())*(1-mProgress)+WAVE_HEIGHT;
    }

    public void startAnim(){
        actAnim();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBazierPath_1.reset();
        mBazierPath_2.reset();
        canvas.save();
        canvas.clipPath(mClipPath);
        mPaint.setColor(Color.parseColor("#D4FAD1"));
        canvas.drawCircle((getRight()-getLeft())/2f,(getBottom()-getTop())/2f,RADIUS,mPaint);
        mPaint.setColor(Color.WHITE);
        canvas.drawText((int)mProgress*100+"%",getWidth()/2f,getHeight()/2f+(Math.abs(mPaint.ascent())-mPaint.descent())/2,mPaint);

        mBazierPath_1.moveTo(mWaveWidth,(getBottom()-getTop())*(1-mProgress));
        mBazierPath_2.moveTo(mWaveWidth,(getBottom()-getTop())*(1-mProgress));
        while (mWaveWidth<getWidth()){
            mBazierPath_1.lineTo(mWaveWidth,(getBottom()-getTop())*(1-mProgress));
            mBazierPath_2.lineTo(mWaveWidth,(getBottom()-getTop())*(1-mProgress));
            mWaveWidth += WAVE_WIDTH/2f;
            mBazierPath_1.quadTo(mWaveWidth,bottomY,
                    mWaveWidth+WAVE_WIDTH/2f,(getBottom()-getTop())*(1-mProgress));
            mBazierPath_2.quadTo(mWaveWidth,topY,
                    mWaveWidth+WAVE_WIDTH/2f,(getBottom()-getTop())*(1-mProgress));
            mWaveWidth += WAVE_WIDTH/2f;
            mBazierPath_1.lineTo(mWaveWidth,(getBottom()-getTop())*(1-mProgress));
            mBazierPath_2.lineTo(mWaveWidth,(getBottom()-getTop())*(1-mProgress));
            mWaveWidth += WAVE_WIDTH/2f;
            mBazierPath_1.quadTo(mWaveWidth,topY,
                    mWaveWidth+WAVE_WIDTH/2f,(getBottom()-getTop())*(1-mProgress));
            mBazierPath_2.quadTo(mWaveWidth,bottomY,
                    mWaveWidth+WAVE_WIDTH/2f,(getBottom()-getTop())*(1-mProgress));
            mWaveWidth += WAVE_WIDTH/2f;
        }
        mBazierPath_1.lineTo(getRight(),getBottom());
        mBazierPath_1.lineTo(0,getBottom());
        mBazierPath_1.close();
        mBazierPath_2.lineTo(getRight(),getBottom());
        mBazierPath_2.lineTo(0,getBottom());
        mBazierPath_2.close();
        canvas.drawPath(mBazierPath_1,mWave_1);
        canvas.drawPath(mBazierPath_2,mWave_2);
        canvas.restore();
    }


    private void actAnim(){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1f);
        valueAnimator.setDuration(DURATION);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mWaveWidth = 0;
                topY = ((getBottom()-getTop())*(1-mProgress)-WAVE_HEIGHT) +
                        animation.getAnimatedFraction()*2*WAVE_HEIGHT;
                bottomY = ((getBottom()-getTop())*(1-mProgress)+WAVE_HEIGHT) -
                        animation.getAnimatedFraction()*2*WAVE_HEIGHT;
                invalidate();
            }
        });
        valueAnimator.start();
    }

}
