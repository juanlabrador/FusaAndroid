package edu.ucla.fusa.android.adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class FragmentViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;
    private String[] mTabs;

    public FragmentViewPagerAdapter(FragmentManager paramFragmentManager, String[] tabs) {
        super(paramFragmentManager);
        mTabs = tabs;
        fragments = new ArrayList();
    }

    public void addFragment(Fragment paramFragment) {
        fragments.add(paramFragment);
    }

    public int getCount() {
        return fragments.size();
    }

    public Fragment getItem(int paramInt) {
        return fragments.get(paramInt);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs[position % mTabs.length].toUpperCase();
    }

}
