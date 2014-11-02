package edu.ucla.fusa.android;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.TabPageIndicator;

import edu.ucla.fusa.android.adaptadores.IconsTabAdapter;
import edu.ucla.fusa.android.fragmentos.ViewPagerEventoComentariosFragment;
import edu.ucla.fusa.android.fragmentos.ViewPagerEventoInformacionFragment;
import edu.ucla.fusa.android.fragmentos.ViewPagerEventoParticipantesFragment;
import edu.ucla.fusa.android.fragmentos.ViewPagerEventoUbicacionFragment;

/**
 * Created by juanlabrador on 30/10/14.
 */
public class VistasEventoActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private IconsTabAdapter adapter;
    private TabPageIndicator tabPageIndicator;
    private String[] titulos = new String[] {
            "Información", "Ubicación", "Participantes", "Comentarios"
    };
    private int[] iconos = new int[] {
            R.drawable.ic_datos,
            R.drawable.ic_ubicacion,
            R.drawable.ic_participantes,
            R.drawable.ic_comentarios
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vistas_evento);

        viewPager = (ViewPager) findViewById(R.id.view_pager_evento);

        adapter = new IconsTabAdapter(getSupportFragmentManager(), titulos, iconos);
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

        tabPageIndicator = (TabPageIndicator) findViewById(R.id.tab_indicator_evento);
        tabPageIndicator.setViewPager(viewPager);

    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        getActionBar().setSelectedNavigationItem(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
