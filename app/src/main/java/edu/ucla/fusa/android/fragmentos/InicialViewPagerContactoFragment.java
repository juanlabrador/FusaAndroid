package edu.ucla.fusa.android.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ViewPagerFragmentAdapter;

/**
 * Created by juanlabrador on 27/10/14.
 *
 * Clase contenedora de las vistas de contacto con fundamusical, los datos y el mapa de ubicación
 */
public class InicialViewPagerContactoFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private ViewPagerFragmentAdapter adapter;
    private ViewPager viewPager;
    private TabPageIndicator tabPageIndicator;
    private String[] titulos;
    private int[] iconos;
    private View view;

    public static InicialViewPagerContactoFragment newInstance(){
        InicialViewPagerContactoFragment fragment = new InicialViewPagerContactoFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public InicialViewPagerContactoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_viewpager_informacion_contacto, container, false);

        titulos = new String[]{
                getString(R.string.titulo_tab_datos_contacto),
                getString(R.string.titulo_tab_ubicacion_contacto)
        };

        iconos = new int[] {
                R.drawable.ic_datos,
                R.drawable.ic_ubicacion
        };

        /** Agregamos los fragment al viewpager */
        viewPager = (ViewPager) view.findViewById(R.id.view_pager_contacto);
        adapter = new ViewPagerFragmentAdapter(getFragmentManager(), iconos, null);
        adapter.addFragment(InicialViewPagerContactoInformacionDatosFragment.newInstance());
        adapter.addFragment(InicialViewPagerContactoInformacionUbicacionFragment.newInstance());

        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);

        tabPageIndicator = (TabPageIndicator) view.findViewById(R.id.tab_indicator_contacto);
        tabPageIndicator.setViewPager(viewPager);
        tabPageIndicator.setOnPageChangeListener(this);

        return view;
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        /** Cuando desliza con el dedo y cambia de pestaña */
        //getActivity().getActionBar().setSelectedNavigationItem(i);
        if (i == 0) {
            ((TextView) getActivity().findViewById(R.id.tv_titulo_superior)).setText(R.string.titulo_tab_datos_contacto);
        } else {
            ((TextView) getActivity().findViewById(R.id.tv_titulo_superior)).setText(R.string.titulo_tab_ubicacion_contacto);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
