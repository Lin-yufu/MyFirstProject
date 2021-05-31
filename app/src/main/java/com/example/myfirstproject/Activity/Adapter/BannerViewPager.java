package com.example.myfirstproject.Activity.Adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class BannerViewPager extends ViewPager {
    private OnViewPagerTouchListen mOnViewPagerTouchListen;

    public BannerViewPager(@NonNull Context context) {
        super(context);
    }

    public BannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mOnViewPagerTouchListen != null) {
                    mOnViewPagerTouchListen.onViewPagerTouch(true);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mOnViewPagerTouchListen != null) {
                    mOnViewPagerTouchListen.onViewPagerTouch(false);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void setOnViewPagerTouchListen(OnViewPagerTouchListen onViewPagerTouchListen) {
        this.mOnViewPagerTouchListen = onViewPagerTouchListen;
    }

    public interface OnViewPagerTouchListen {
        void onViewPagerTouch(boolean isTouch);
    }

}