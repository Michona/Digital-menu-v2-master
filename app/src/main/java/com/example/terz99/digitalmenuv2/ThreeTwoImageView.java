package com.example.terz99.digitalmenuv2;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;


public class ThreeTwoImageView extends android.support.v7.widget.AppCompatImageView {
    public ThreeTwoImageView(Context context) {
        super(context);
    }

    public ThreeTwoImageView (Context context, AttributeSet attrs){
        super(context,attrs);
    }

    public ThreeTwoImageView (Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int threetwoHeight = MeasureSpec.getSize(widthMeasureSpec) * 9 / 16;
        int threetwiHeightSpec = MeasureSpec.makeMeasureSpec(threetwoHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, threetwiHeightSpec);
    }
}