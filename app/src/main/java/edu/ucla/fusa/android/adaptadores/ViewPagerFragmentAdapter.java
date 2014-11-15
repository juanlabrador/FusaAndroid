package edu.ucla.fusa.android.adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juanlabrador on 16/10/14.
 *
 * Clase adaptadora que se utiliza para agregar distintos fragment al viewPager.
 */
public class ViewPagerFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

    private List<Fragment> fragmentos;
    private int[] icons;
    private String[] titulos;

    public ViewPagerFragmentAdapter(FragmentManager fm, int[] icons, String[] titulos) {
        super(fm);
        this.fragmentos = new ArrayList<Fragment>();
        this.icons = icons;
        this.titulos = titulos;
    }

    public void addFragment(Fragment fragmento) {
        this.fragmentos.add(fragmento);
    }

    @Override
    public Fragment getItem(int i) {
        return this.fragmentos.get(i);
    }

    @Override
    public int getIconResId(int index) {
        return icons[index];
    }

    @Override
    public int getCount() {
        return this.fragmentos.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titulos != null) {
            return titulos[position % titulos.length].toUpperCase();
        } else {
            return null;
        }
    }
}
