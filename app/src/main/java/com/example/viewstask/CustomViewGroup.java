package com.example.viewstask;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;

public class CustomViewGroup extends ViewGroup {

    private final int DEFAULT_DISTANCE = 10;
    private final int DEFAULT_HEIGHT = 10;

    private int distanceBetweenChilds;

    private int childsHeight;

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
        distanceBetweenChilds = typedArray.getDimensionPixelSize(R.styleable.CustomViewGroup_cvg_childDistance, DEFAULT_DISTANCE);
        childsHeight = typedArray.getDimensionPixelSize(R.styleable.CustomViewGroup_cvg_childHeight, DEFAULT_HEIGHT);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childsHeight, MeasureSpec.EXACTLY);

        measureChildren(widthMeasureSpec, childHeightMeasureSpec);

        int maxHeight = 0;
        int maxWidth = 0;

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE)
                continue;

            int childRight;
            int childBottom;

            LayoutParams layoutParams = child.getLayoutParams();

            childRight = layoutParams.x + child.getMeasuredWidth();
            childBottom = layoutParams.y + child.getMeasuredHeight();

            maxWidth = Math.max(maxWidth, childRight);
            maxHeight = Math.max(maxHeight, childBottom);
        }

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), totalChildHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
