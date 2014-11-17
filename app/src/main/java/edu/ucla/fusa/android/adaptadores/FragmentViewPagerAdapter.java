package edu.ucla.fusa.android.adaptadores;

import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.viewpagerindicator.IconPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class FragmentViewPagerAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter {

    private List<Fragment> fragments = new ArrayList();
    private TypedArray iconos;

    public FragmentViewPagerAdapter(FragmentManager paramFragmentManager, TypedArray paramTypedArray) {
        super(paramFragmentManager);
        this.iconos = paramTypedArray;
    }

    public void addFragment(Fragment paramFragment) {
        fragments.add(paramFragment);
    }

    public int getCount() {
        return fragments.size();
    }

    public int getIconResId(int paramInt) {
        return iconos.getResourceId(paramInt, -1);
    }

    public Fragment getItem(int paramInt) {
        return fragments.get(paramInt);
    }
}

/* Location:           /home/juanlabrador/Escritorio/apk/dex2jar-0.0.9.15/classes_dex2jar.jar
 * Qualified Name:     edu.ucla.fusa.android.adaptadores.FragmentViewPagerAdapter
 * JD-Core Version:    0.6.2
 */