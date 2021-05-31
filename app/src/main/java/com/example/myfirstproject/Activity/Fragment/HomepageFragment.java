package com.example.myfirstproject.Activity.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myfirstproject.Activity.HomepageFragment.BlankFragment;
import com.example.myfirstproject.R;
import com.google.android.material.tabs.TabLayout;


import java.util.ArrayList;
import java.util.List;

public class HomepageFragment extends Fragment {
    private ViewPager mViewPager;
    private List<Fragment> fragmentContainer=new ArrayList<>();
    private List<String> titles= new ArrayList<>();
    private TabLayout mTablayout;

    public HomepageFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        mViewPager=(ViewPager)view.findViewById(R.id.homepage_viewpager);
        mTablayout=(TabLayout)view.findViewById(R.id.home_tablayout);
        fragmentContainer.add(BlankFragment.newInstance("考研", "考研"));
        fragmentContainer.add(BlankFragment.newInstance("英语", "英语"));
        fragmentContainer.add(BlankFragment.newInstance("数学", "数学"));
        fragmentContainer.add(BlankFragment.newInstance("计算机", "计算机"));
        titles.add("考研");
        titles.add("英语");
        titles.add("数学");
        titles.add("计算机");
        //设置viewpager的adapter



        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragmentContainer.get(position);
            }

            @Override
            public int getCount() {
                return fragmentContainer.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }



        });
        System.out.println(mViewPager.getContext());
        //tablayout绑定viewpager
          mTablayout.setupWithViewPager(mViewPager);
          mViewPager.setCurrentItem(0);
          System.out.println("title");
//        mTablayout.addTab(mTablayout.newTab().setText("计算机"));
//        mTablayout.addTab(mTablayout.newTab().setText("英语"));
//        mTablayout.addTab(mTablayout.newTab().setText("数学"));
//        mTablayout.addTab(mTablayout.newTab().setText("考研"));
        return view;
    }
}

