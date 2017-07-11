package com.example.sheldon.instagramclone.Util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sheldon on 7/10/2017.
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

    public void addFragment(Fragment fragment, String name) {
        fragmentList.add(fragment);
        int fragIndex = fragmentList.size() - 1;
        fragmentIntegerHashMap.put(fragment, fragIndex);
        fragNameIntegerHashMap.put(name, fragIndex);
        integerFragNameHashMap.put(fragIndex, name);
    }

    public int getFragmentNumber(String name) {
        return fragNameIntegerHashMap.get(name);
    }

    public int getFragmentNumber(Fragment fragment) {
        return fragmentIntegerHashMap.get(fragment);
    }

    public String getFragmentName(Integer num) {
        return integerFragNameHashMap.get(num);
    }
}

