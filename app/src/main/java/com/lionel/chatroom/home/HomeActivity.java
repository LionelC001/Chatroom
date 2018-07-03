package com.lionel.chatroom.home;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lionel.chatroom.R;
import com.lionel.chatroom.home.adapter.HomePagerAdapter;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViewPager();
    }

    private void initViewPager() {
        HomePagerAdapter viewPagerAdapter = new HomePagerAdapter(
                getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager_home);
        viewPager.setAdapter(viewPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout_home);
        tabLayout.setupWithViewPager(viewPager);
    }
}
