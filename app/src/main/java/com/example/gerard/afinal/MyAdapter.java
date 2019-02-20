package com.example.gerard.afinal;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends FragmentPagerAdapter  {

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






