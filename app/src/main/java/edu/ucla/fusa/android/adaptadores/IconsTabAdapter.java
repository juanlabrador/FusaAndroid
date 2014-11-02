package edu.ucla.fusa.android.adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import edu.ucla.fusa.android.fragmentos.CustomTabsFragment;

/**
 * Created by juanlabrador on 01/11/14.
 */
public class IconsTabAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

    private String[] titulos;
    private int[] icons;
    private List<Fragment> fragmentos;

    public IconsTabAdapter (FragmentManager fm, String[] titulos, int[] icons) {
        super(fm);
        this.titulos = titulos;
        this.icons = icons;
        this.fragmentos = new ArrayList<Fragment>();
    }

    public void adicionarFragmento(Fragment fragmento) {
        this.fragmentos.add(fragmento);
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragmentos.get(position);
    }

    @Override
    public int getIconResId(int index) {
        return icons[index];
    }

    @Override
    public int getCount() {
        return titulos.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titulos[position % titulos.length].toUpperCase();
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
