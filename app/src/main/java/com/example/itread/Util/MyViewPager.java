package com.example.itread.Util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

/**
 * Content：ScrollView与viewPager之间的滑动冲突
 * Actor：韩小呆 ヾ(✿ﾟ▽ﾟ)ノ
 * Time:  2018/8/1 13:48
 * Update:
 * Time:
 */
public class MyViewPager extends ViewPager {
    public MyViewPager(Context context) {
        super(context);
    }


    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height)
                height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
