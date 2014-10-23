package edu.ucla.fusa.android.actividades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.CirclePageIndicator;

import edu.ucla.fusa.android.adaptadores.ViewPagerFragmentAdapter;
import edu.ucla.fusa.android.fragmentos.VistaInicialEstudiantesFragment;
import edu.ucla.fusa.android.fragmentos.VistaInicialInstrumentosFragment;
import edu.ucla.fusa.android.fragmentos.VistaInicialPostularseFragment;
import edu.ucla.fusa.android.fragmentos.VistaInicialSplashScreenFragment;
import edu.ucla.fusa.android.fragmentos.VistaInicialEventosFragment;

import static edu.ucla.fusa.android.actividades.R.color.vinotinto_oscuro;

/**
 * Created by juanlabrador on 16/10/14.
 */
public class VistasInicialesActivity extends FragmentActivity {
    private ViewPager pagina;
    private ViewPagerFragmentAdapter fragmentoPagina;
    private CirclePageIndicator indicadorPagina;
    private SharedPreferences preferencias;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vistasiniciales);

        if(cargarPreferencias() != true) {
            this.pagina = (ViewPager) this.findViewById(R.id.vistaPaginas);

            fragmentoPagina = new ViewPagerFragmentAdapter(getSupportFragmentManager(), this);
            fragmentoPagina.adicionarFragmento(VistaInicialSplashScreenFragment.newInstance());
            fragmentoPagina.adicionarFragmento(VistaInicialEstudiantesFragment.newInstance());
            fragmentoPagina.adicionarFragmento(VistaInicialInstrumentosFragment.newInstance());
            fragmentoPagina.adicionarFragmento(VistaInicialEventosFragment.newInstance());
            fragmentoPagina.adicionarFragmento(VistaInicialPostularseFragment.newInstance());
            this.pagina.setAdapter(fragmentoPagina);

            indicadorPagina = (CirclePageIndicator) this.findViewById(R.id.indicador);
            indicadorPagina.setViewPager(pagina);
            indicadorPagina.setSnap(true);
            indicadorPagina.setFillColor(Color.WHITE);
            indicadorPagina.setPageColor(vinotinto_oscuro);
            indicadorPagina.setStrokeColor(Color.TRANSPARENT);

            guardarPreferencias(true);

        } else if (cargarPreferencias() == true) {
            startActivity(new Intent(this, VistasAccesoActivity.class).putExtra("acceso", 2));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void guardarPreferencias(boolean status) {
        preferencias = getSharedPreferences("index", Context.MODE_PRIVATE);
        editor = preferencias.edit();
        editor.putBoolean("ignorar", status);
        editor.commit();
    }

    public boolean cargarPreferencias() {
        preferencias = getSharedPreferences("index", Context.MODE_PRIVATE);
        return preferencias.getBoolean("ignorar", false);
    }
}
