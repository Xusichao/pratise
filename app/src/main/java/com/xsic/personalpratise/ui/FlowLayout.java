package com.xsic.personalpratise.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {
    private List<List<View>> mAllViews = new ArrayList<>();
    private List<Integer> mLineHeight = new ArrayList<>();

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int finalWidth = 0;
        int finalHeight = 0;
        int childCount = getChildCount();
        int curLineWidth = 0;
        int curLineHeight = 0;

        for (int i=0; i<childCount; i++){
            View child = getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            int childHeight = child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;

            if (curLineWidth + childWidth > widthSize){
                //加入当前view之后会超过
                finalWidth = Math.max(curLineWidth, childWidth);
                curLineWidth = childWidth;
                curLineHeight = childHeight;
                finalHeight += childHeight;
            }else {
                curLineWidth += childWidth;
                finalHeight = Math.max(curLineHeight, childHeight);
            }
            if (i == childCount-1){
                finalWidth = Math.max(curLineWidth, childWidth);
                finalHeight += curLineHeight;
            }
        }

        setMeasuredDimension(widthMode==MeasureSpec.EXACTLY? widthSize : finalWidth,
                heightMode==MeasureSpec.EXACTLY? heightSize : finalHeight);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();

        int width = getWidth();
        int lineWidth = 0;
        int lineHeight = 0;
        List<View> lineViews = new ArrayList<>();
        int childCount = getChildCount();
        for (int i=0;i<childCount;i++){
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            if (childWidth + lp.leftMargin + lp.rightMargin + lineWidth > width){
                mLineHeight.add(lineHeight);
                mAllViews.add(lineViews);
                lineHeight = 0;
                lineViews = new ArrayList<>();
            }
            lineWidth = lineWidth + lp.leftMargin + lp.rightMargin + childWidth;
            lineHeight = Math.max(lineHeight, lp.topMargin + lp.bottomMargin + childHeight);
            lineViews.add(child);
        }
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);

        int left = 0;
        int top = 0;
        int lineNums = mAllViews.size();
        for (int j=0;j<lineNums;j++){
            lineViews = mAllViews.get(j);
            lineHeight = mLineHeight.get(j);
            for (int n=0;n<lineViews.size();n++){
                View child = lineViews.get(n);
                if (child.getVisibility() == GONE) continue;
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int childL = left + lp.leftMargin;
                int childT = top + lp.topMargin;
                int childR = childL + lp.rightMargin;
                int childB = childT + lp.bottomMargin;

                child.layout(childL,childT,childR,childB);

                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }
            left = 0;
            top += lineHeight;
        }

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}
