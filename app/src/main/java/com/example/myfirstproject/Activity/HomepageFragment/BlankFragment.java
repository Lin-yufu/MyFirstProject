package com.example.myfirstproject.Activity.HomepageFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myfirstproject.Activity.Adapter.BannerPagerAdapter;
import com.example.myfirstproject.Activity.Adapter.BannerViewPager;
import com.example.myfirstproject.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class BlankFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String TAG = "BlankFragment";
    private BannerViewPager mShuffling;
    private BannerPagerAdapter mShufflingAdapter;
    private static List<Integer> sList = new ArrayList<>();
    private boolean mIsTouch = false;
    private boolean isUICreate=false;

    static {
        sList.add(1);
        sList.add(2);
        sList.add(3);
        sList.add(4);
        sList.add(5);
        sList.add(6);
        sList.add(7);
        sList.add(8);
        sList.add(9);
        sList.add(10);
        sList.add(11);
    }

    private Handler mHandler;
    private LinearLayout mLinearLayout;


    public BlankFragment() {
        // Required empty public constructor
    }

    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        TextView text = (TextView) view.findViewById(R.id.text);
        initView(view);
        mHandler = new Handler();
        isUICreate=true;
        return view;
    }

    public void testMeasure() {
    }

    public void test2() {
    }


    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (!mIsTouch) {
                int currentItem = mShuffling.getCurrentItem();
                mShuffling.setCurrentItem(++currentItem, false);
            }
            mHandler.postDelayed(this, 3000);
        }
    };

    private void initView(View view) {
//        1.找到ViewPager组件
        mShuffling = view.findViewById(R.id.shuffling);
        mShuffling.setOnViewPagerTouchListen(new BannerViewPager.OnViewPagerTouchListen() {
            @Override
            public void onViewPagerTouch(boolean isTouch) {
                mIsTouch = isTouch;
            }
        });
//        2.设置适配器
        mShufflingAdapter = new BannerPagerAdapter();
        mShufflingAdapter.setData(sList);
        mShuffling.setAdapter(mShufflingAdapter);
        mShuffling.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int realPosition = 0;
                if (mShufflingAdapter.getDataRealSize() != 0) {
                    realPosition = position % mShufflingAdapter.getDataRealSize();
                }
                selectedPoint(realPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mLinearLayout = view.findViewById(R.id.points);
        initPoints(view);
        mShuffling.setCurrentItem(mShufflingAdapter.getDataRealSize() * 100 - 1, false);
    }

    private void selectedPoint(int realPosition) {

        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
            View point = mLinearLayout.getChildAt(i);
            if (i == realPosition) {
                point.setBackgroundResource(R.drawable.point_selected);
            } else {
                point.setBackgroundResource(R.drawable.point);
            }
        }


    }

    private void initPoints(View view) {
        for (int i = 0; i < mShufflingAdapter.getDataRealSize(); i++) {
            View point = new View(view.getContext());
            point.setBackgroundResource(R.drawable.point);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(40, 40);
            params.leftMargin = 20;
            point.setLayoutParams(params);
            mLinearLayout.addView(point);
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser&&isUICreate)mHandler.post(mRunnable);
        if(!isVisibleToUser&&isUICreate)mHandler.removeCallbacks(mRunnable);
    }

}
