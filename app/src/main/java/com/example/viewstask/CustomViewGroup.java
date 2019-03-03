package com.example.viewstask;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class CustomViewGroup extends ViewGroup {

    private final String GRAVITY_LEFT = "left";
    private final String GRAVITY_RIGHT = "right";

    private final int DEFAULT_DISTANCE = 20;
    private final int DEFAULT_HEIGHT = 30;
    private final int ROWS_DISTANCE = 20;

    private int distanceBetweenChildren;
    private int childrenHeight;
    private int rowDistance;
    private String gravity;

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
        this.distanceBetweenChildren = typedArray.getDimensionPixelSize(R.styleable.CustomViewGroup_cvg_childDistance, DEFAULT_DISTANCE);
        this.childrenHeight = typedArray.getDimensionPixelSize(R.styleable.CustomViewGroup_cvg_childHeight, DEFAULT_HEIGHT);
        this.rowDistance = typedArray.getDimensionPixelSize(R.styleable.CustomViewGroup_cvg_rowDistance, ROWS_DISTANCE);
        this.gravity = typedArray.getString(R.styleable.CustomViewGroup_cvg_gravity);
        if(this.gravity == null){
            this.gravity = GRAVITY_LEFT;
        }

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

            measureChild(child, widthMeasureSpec, MeasureSpec.makeMeasureSpec(this.childrenHeight, MeasureSpec.EXACTLY));

            int childMeasuredWidth = child.getMeasuredWidth() + this.distanceBetweenChildren;
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

        int curWidth;
        int curHeight;
        int maxHeight = 0;
        int curLeft = childLeft;
        int curTop = childTop;
        int curRight = childRight;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE)
                continue;

            curWidth = child.getMeasuredWidth();
            curHeight = child.getMeasuredHeight();

            if(this.gravity == null || this.gravity.equals(GRAVITY_LEFT)){
                if (curLeft + curWidth >= childRight) {
                    curLeft = childLeft;
                    curTop += maxHeight;
                    maxHeight = 0;
                }

                child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);

                if (maxHeight < curHeight)
                    maxHeight = curHeight + this.rowDistance;
                curLeft += curWidth + this.distanceBetweenChildren;
            } else {
                if (curRight - curWidth < childLeft) {
                    curRight = childRight;
                    curTop += maxHeight;
                    maxHeight = 0;
                }

                child.layout(curRight - curWidth, curTop, curRight, curTop + curHeight);

                if (maxHeight < curHeight)
                    maxHeight = curHeight + this.rowDistance;
                curRight -= curWidth + this.distanceBetweenChildren;
            }


        }
    }

    public void setDistanceBetweenChildren(int distanceBetweenChildren) {
        this.distanceBetweenChildren = distanceBetweenChildren;
    }

    public void setChildrenHeight(int childrenHeight) {
        this.childrenHeight = childrenHeight;
    }

    public void setRowDistance(int rowDistance) {
        this.rowDistance = rowDistance;
    }

    public void setGravity(String gravity) {
        this.gravity = gravity;
    }
}
