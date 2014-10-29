package edu.ucla.fusa.android.fragmentos;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.viewpagerindicator.CirclePageIndicator;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ViewPagerFragmentAdapter;

/**
 * Created by juanlabrador on 16/10/14.
 *
 * Esta clase administra todas las vista deslizables iniciales, donde se muestra información
 * de la Fundación, y manejamos dos opciones de iniciación, logueandose o postulandose con
 * Fundamusical.
 *
 */
public class VistasInicialesFragment extends Fragment {

    private ViewPager pagina;
    private ViewPagerFragmentAdapter fragmentoPagina;
    private CirclePageIndicator indicadorPagina;
    private View view;

    public static VistasInicialesFragment newInstance(Bundle arguments) {
        VistasInicialesFragment activity = new VistasInicialesFragment();
        activity.setRetainInstance(true);
        if (arguments != null) {
            activity.setArguments(arguments);
        }
        return activity;
    }

    public VistasInicialesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_vistas_iniciales, container, false);

        pagina = (ViewPager) view.findViewById(R.id.vistaPaginas);
        /** Agregamos los fragment a el ViewPager */

        fragmentoPagina = new ViewPagerFragmentAdapter(getFragmentManager());
        fragmentoPagina.adicionarFragmento(VistaInicialSplashScreenFragment.newInstance());
        fragmentoPagina.adicionarFragmento(VistaInicialEstudiantesFragment.newInstance());
        fragmentoPagina.adicionarFragmento(VistaInicialProfesoresFragment.newInstance());
        fragmentoPagina.adicionarFragmento(VistaInicialInstrumentosFragment.newInstance());
        fragmentoPagina.adicionarFragmento(VistaInicialEventosFragment.newInstance());
        fragmentoPagina.adicionarFragmento(VistaInicialContactoFragment.newInstance());
        this.pagina.setAdapter(fragmentoPagina);
        /** Aqui agregamos los indicadores de página */
        indicadorPagina = (CirclePageIndicator) view.findViewById(R.id.indicador);
        indicadorPagina.setViewPager(pagina);
        indicadorPagina.setSnap(true);
        indicadorPagina.setFillColor(getResources().getColor(R.color.blanco));
        indicadorPagina.setPageColor(getResources().getColor(R.color.azul_oscuro));
        indicadorPagina.setStrokeColor(Color.TRANSPARENT);

        pagina.setCurrentItem(getArguments().getInt("position"));
        return view;
    }

}
