package com.example.myfirstproject.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myfirstproject.Activity.Fragment.DiscoverFragment;
import com.example.myfirstproject.Activity.Fragment.HomepageFragment;
import com.example.myfirstproject.Activity.Fragment.MineFragment;
import com.example.myfirstproject.R;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ImageView homepageButton;
    private ImageView discoverButton;
    private ImageView mineButton;

    private DiscoverFragment discoverFragment;
    private HomepageFragment homepageFragment;
    private MineFragment mineFragment;
    private FragmentManager fragmentManager;
    public static String hostId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        hostId = intent.getStringExtra("hostId");
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

            fragmentManager = getSupportFragmentManager();
            bind();
            homepageButton.performClick();
    }

    private void bind() {
        homepageButton = (ImageView) findViewById(R.id.hompage_button);
        discoverButton = (ImageView) findViewById(R.id.discover_button);
        mineButton = (ImageView) findViewById(R.id.mine_button);

        homepageButton.setOnClickListener(this);
        discoverButton.setOnClickListener(this);
        mineButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction);
        switch (v.getId()) {
            case R.id.hompage_button:
                setAllSelectedFalse();
                homepageButton.setSelected(true);
                if (homepageFragment == null) {
                    homepageFragment = new HomepageFragment();
                    fragmentTransaction.add(R.id.main_content, homepageFragment);
                } else {
                    fragmentTransaction.show(homepageFragment);
                }
                break;
            case R.id.discover_button:
                setAllSelectedFalse();
                discoverButton.setSelected(true);
                if (discoverFragment == null) {
                    discoverFragment = new DiscoverFragment();
                    fragmentTransaction.add(R.id.main_content, discoverFragment);
                } else {
                    fragmentTransaction.show(discoverFragment);
                }
                break;
            case R.id.mine_button:
                setAllSelectedFalse();
                mineButton.setSelected(true);
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    fragmentTransaction.add(R.id.main_content, mineFragment);
                } else {
                    fragmentTransaction.show(mineFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    private void setAllSelectedFalse() {
        homepageButton.setSelected(false);
        discoverButton.setSelected(false);
        mineButton.setSelected(false);
    }

    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (homepageFragment != null) fragmentTransaction.hide(homepageFragment);
        if (discoverFragment != null) fragmentTransaction.hide(discoverFragment);
        if (mineFragment != null) fragmentTransaction.hide(mineFragment);
    }

}


