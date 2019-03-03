package com.example.viewstask;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class CustomViewGroup extends ViewGroup {

    private final int DEFAULT_DISTANCE = 20;
    private final int DEFAULT_HEIGHT = 30;
    private final int ROWS_DISTANCE = 20;

    private int distanceBetweenChilds;
    private int childsHeight;
    private int rowDistance;

    public CustomViewGroup(Context context) {
        super(context);
    }

    public CustomViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        fillAttributes(context, attrs);
    }

    public CustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        fillAttributes(context, attrs);

    }

    public CustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        fillAttributes(context, attrs);
    }

    private void fillAttributes(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomViewGroup);
        this.distanceBetweenChilds = typedArray.getDimensionPixelSize(R.styleable.CustomViewGroup_cvg_childDistance, DEFAULT_DISTANCE);
        this.childsHeight = typedArray.getDimensionPixelSize(R.styleable.CustomViewGroup_cvg_childHeight, DEFAULT_HEIGHT);
        this.rowDistance = typedArray.getDimensionPixelSize(R.styleable.CustomViewGroup_cvg_rowDistance, ROWS_DISTANCE);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int suggestedWidth = MeasureSpec.getSize(widthMeasureSpec);
        int totalHeight = 0;
        int remainingLineWidth = suggestedWidth;
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            measureChild(child, widthMeasureSpec, MeasureSpec.makeMeasureSpec(this.childsHeight, MeasureSpec.EXACTLY));

            int childMeasuredWidth = child.getMeasuredWidth() + this.distanceBetweenChilds;
            int childMeasuredHeight = child.getMeasuredHeight();

            if(totalHeight == 0){
                totalHeight = childMeasuredHeight;
            }

            if(remainingLineWidth >= childMeasuredWidth){
                remainingLineWidth -= childMeasuredWidth;
            } else {
                totalHeight += childMeasuredHeight + this.rowDistance;
                remainingLineWidth = suggestedWidth - childMeasuredWidth;
            }
        }

        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childLeft = this.getPaddingLeft();
        final int childTop = this.getPaddingTop();
        final int childRight = this.getMeasuredWidth() - this.getPaddingRight();
        //final int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
        /*final int childWidth = childRight - childLeft;
        final int childHeight = childBottom - childTop;*/

        int curWidth;
        int curHeight;
        int maxHeight = 0;
        int curLeft = childLeft;
        int curTop = childTop;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE)
                continue;

            /*child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));*/
            curWidth = child.getMeasuredWidth();
            curHeight = child.getMeasuredHeight();

            if (curLeft + curWidth >= childRight) {
                curLeft = childLeft;
                curTop += maxHeight;
                maxHeight = 0;
            }

            child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);

            if (maxHeight < curHeight)
                maxHeight = curHeight + this.rowDistance;
            curLeft += curWidth + this.distanceBetweenChilds;
        }
    }

    public void setDistanceBetweenChilds(int distanceBetweenChilds) {
        this.distanceBetweenChilds = distanceBetweenChilds;
    }

    public void setChildsHeight(int childsHeight) {
        this.childsHeight = childsHeight;
    }

    public void setRowDistance(int rowDistance) {
        this.rowDistance = rowDistance;
    }
}
