package edu.ucla.fusa.android.fragmentos;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.IconsTabAdapter;
import edu.ucla.fusa.android.adaptadores.ViewPagerFragmentAdapter;

/**
 * Created by juanlabrador on 30/10/14.
 */
public class ViewPagerEventoFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private IconsTabAdapter adapter;
    private TabPageIndicator tabPageIndicator;
    private View view;
    private ActionBar actionBar;
    private String[] titulos = new String[] {
            "Información", "Ubicación", "Participantes", "Comentarios"
    };
    private int[] iconos = new int[] {
            R.drawable.ic_datos,
            R.drawable.ic_ubicacion,
            R.drawable.ic_participantes,
            R.drawable.ic_comentarios
    };

    public static ViewPagerEventoFragment newInstance() {
        ViewPagerEventoFragment activity = new ViewPagerEventoFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public ViewPagerEventoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_viewpager_evento, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.view_pager_evento);

        adapter = new IconsTabAdapter(getFragmentManager(), titulos, iconos);
        adapter.adicionarFragmento(ViewPagerEventoInformacionFragment.newInstance());
        adapter.adicionarFragmento(ViewPagerEventoUbicacionFragment.newInstance());
        adapter.adicionarFragmento(ViewPagerEventoParticipantesFragment.newInstance());
        adapter.adicionarFragmento(ViewPagerEventoComentariosFragment.newInstance());

        viewPager.setAdapter(adapter);

        /*actionBar = getActivity().getActionBar();
        actionBar.show();
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setTitle(R.string.titulo_action_bar_contacto);
        /** Agregamos las 2 pestañas de contenido */
        /*actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText(titulos[0])
                .setIcon(R.drawable.ic_datos)
                .setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(titulos[1])
                .setIcon(R.drawable.ic_ubicacion)
                .setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(titulos[2])
                .setIcon(R.drawable.ic_participantes)
                .setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(titulos[3])
                .setIcon(R.drawable.ic_comentarios)
                .setTabListener(this));*/

        tabPageIndicator = (TabPageIndicator) view.findViewById(R.id.tab_indicator_evento);
        tabPageIndicator.setViewPager(viewPager);

        return view;
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        getActivity().getActionBar().setSelectedNavigationItem(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
