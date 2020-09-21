package com.example.arknightsoperatorviewer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SectionsPageAdapter adapt;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //view pager setup for TabLayout scrolling
        viewPager = (ViewPager)findViewById(R.id.pager);
        setupViewPager(viewPager);

        //set TabLayout content to the viewPager
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //Bottom Navigation Menu setup.
        BottomNavigationView botNav = (BottomNavigationView) findViewById(R.id.bottomNavBar);
        Menu menu = botNav.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        //listener for the bottom nav menu
        botNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.world:
                        break;
                    case R.id.operators:
                        Intent intent1 = new Intent(MainActivity.this, OperatorListActivity.class);
                        startActivity(intent1);
                        break;
                }
                return false;
            }
        });
    }

    //setup ViewPager with the necessary fragments
    private void setupViewPager(ViewPager viewPager)
    {
        adapt = new SectionsPageAdapter(getSupportFragmentManager());
        adapt.addFragment(new LandingFragment(), "Landing");
        adapt.addFragment(new Landing2Fragment(), "Test");
        viewPager.setAdapter(adapt);
    }

    //custom class to facilitate the ViewPager and the fragments that it needs to hold
    private class SectionsPageAdapter extends FragmentStatePagerAdapter {

        private final ArrayList<Fragment> fragList = new ArrayList<Fragment>();
        private final ArrayList<String> fragTitleList = new ArrayList<String>();

        public SectionsPageAdapter(FragmentManager fm)
        {
            super(fm);
        }

        public CharSequence getPageTitle(int position)
        {
            return fragTitleList.get(position);
        }

        public Fragment getItem(int position)
        {
            return fragList.get(position);
        }

        public int getCount()
        {
            return fragList.size();
        }

        public void addFragment(Fragment frag, String title)
        {
            fragList.add(frag);
            fragTitleList.add(title);
        }
    }

}
