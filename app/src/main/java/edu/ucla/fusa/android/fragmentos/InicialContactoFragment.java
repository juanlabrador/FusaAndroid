package edu.ucla.fusa.android.fragmentos;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.VistasInicialesActivity;
import edu.ucla.fusa.android.adaptadores.ViewPagerFragmentAdapter;

/**
 * Created by juanlabrador on 27/10/14.
 *
 * Clase contenedora de las vistas de contacto con fundamusical, los datos y el mapa de ubicación
 */
public class InicialContactoFragment extends Fragment implements ViewPager.OnPageChangeListener, ActionBar.TabListener, View.OnKeyListener {

    private ViewPagerFragmentAdapter adapter;
    private ViewPager viewPager;
    private ActionBar actionBar;
    private String[] titulos;
    private int[] iconos;
    private View view;

    public static InicialContactoFragment newInstance(){
        InicialContactoFragment activity = new InicialContactoFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public InicialContactoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_informacion_contacto, container, false);

        /** Agregamos los fragment al viewpager */
        viewPager = (ViewPager) view.findViewById(R.id.vistasContacto);
        adapter = new ViewPagerFragmentAdapter(getFragmentManager());
        adapter.adicionarFragmento(InicialContactoInformacionDatosFragment.newInstance());
        adapter.adicionarFragmento(InicialContactoInformacionMapaFragment.newInstance());

        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);

        titulos = new String[]{getString(R.string.titulo_tab_datos_contacto),
                getString(R.string.titulo_tab_ubicacion_contacto)};

        iconos = new int[] {R.drawable.ic_datos, R.drawable.ic_ubicacion};

        actionBar = getActivity().getActionBar();
        actionBar.show();
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setTitle(R.string.titulo_action_bar_contacto);
        /** Agregamos las 2 pestañas de contenido */
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText(titulos[0])
                .setIcon(iconos[0])
                .setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(titulos[1])
                .setIcon(iconos[1])
                .setTabListener(this));

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(this);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMenuVisibility(true);


    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        /** Cuando desliza con el dedo y cambia de pestaña */
        getActivity().getActionBar().setSelectedNavigationItem(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        /** Cuando selecciona una pestaña */
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    /**
     *
     * Metodo para capturar cuando se haga click en el botón de retorno para
     * que me posicione en el viewpager
     *
     * */
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                startActivity(new Intent(getActivity(), VistasInicialesActivity.class).putExtra("position", 5));
                getActivity().finish();
                return true;
            }
        }
        return false;
    }
}
