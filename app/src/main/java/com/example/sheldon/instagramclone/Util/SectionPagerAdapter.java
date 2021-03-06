package com.example.sheldon.instagramclone.Util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sheldon on 7/5/2017.
 * Represent a set of static fragments as pages
 */

public class SectionPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    public SectionPagerAdapter(FragmentManager fm) {

        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    /**
     * Add fragment to the list
     * @param fragment the fragment being added
     */
    public void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
    }
}
