package edu.ucla.fusa.android.adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juanlabrador on 16/10/14.
 */
public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentos;
    private Context contexto;

    public ViewPagerFragmentAdapter(FragmentManager fm, Context contexto) {
        super(fm);
        this.fragmentos = new ArrayList<Fragment>();
        this.contexto = contexto;
    }

    public ViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void adicionarFragmento(Fragment fragmento) {
        this.fragmentos.add(fragmento);
    }

    @Override
    public Fragment getItem(int i) {
        return this.fragmentos.get(i);
    }

    @Override
    public int getCount() {
        return this.fragmentos.size();
    }
}
