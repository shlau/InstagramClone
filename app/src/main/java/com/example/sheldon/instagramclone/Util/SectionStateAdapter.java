package com.example.sheldon.instagramclone.Util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sheldon on 7/10/2017.
 * Represent a large number of fragments as pages
 */

public class SectionStateAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragmentList = new ArrayList<>();
    HashMap<Fragment,Integer> fragmentIntegerHashMap = new HashMap<>();
    HashMap<String,Integer> fragNameIntegerHashMap = new HashMap<>();
    HashMap<Integer,String> integerFragNameHashMap = new HashMap<>();

    public SectionStateAdapter(FragmentManager fm) {
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
     * Added a fragment and its associated name to the corresponding hashmaps
     * @param fragment the fragment being added
     * @param name the fragment's name
     */
    public void addFragment(Fragment fragment, String name) {
        fragmentList.add(fragment);
        int fragIndex = fragmentList.size() - 1;
        fragmentIntegerHashMap.put(fragment, fragIndex);
        fragNameIntegerHashMap.put(name, fragIndex);
        integerFragNameHashMap.put(fragIndex, name);
    }

    /**
     * Get the fragment number associated with the fragment name
     * @param name the fragment name
     * @return the fragment number
     */
    public int getFragmentNumber(String name) {
        return fragNameIntegerHashMap.get(name);
    }

    /**
     * Get the fragment number associated with the fragment
     * @param fragment the fragment
     * @return the fragment's number
     */
    public int getFragmentNumber(Fragment fragment) {
        return fragmentIntegerHashMap.get(fragment);
    }

    /**
     * Get the fragment name associated with the fragment number
     * @param num the fragment number
     * @return the fragment's name
     */
    public String getFragmentName(Integer num) {
        return integerFragNameHashMap.get(num);
    }
}

