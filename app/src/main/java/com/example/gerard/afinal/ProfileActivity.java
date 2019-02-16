package com.example.gerard.afinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private TabLayout tabs;
    private ViewPager viewPager;
    private BottomNavigationView bottom_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tabs = findViewById(R.id.tabs);
        viewPager=findViewById(R.id.viewPager);
        bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.navigation_home){
                    Intent home = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(home);
                }
                return false;
            }
        });
        MyAdapter adapter=new MyAdapter(getSupportFragmentManager());
        adapter.addFragment(new InterestsFragment(),"Interests");
        adapter.addFragment(new EventHistoryFragment(),"EventHistory");
        adapter.addFragment(new FollowingFragment(),"Following");
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
    }
    class MyAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> fragmentHeaderList = new ArrayList<>();

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentHeaderList.get(position);
        }
        public void addFragment(Fragment fragment, String header){
            fragmentList.add(fragment);
            fragmentHeaderList.add(header);

        }

    }


}
