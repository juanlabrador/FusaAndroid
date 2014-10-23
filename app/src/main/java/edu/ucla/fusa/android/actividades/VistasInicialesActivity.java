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
 *
 * Esta clase administra todas las vista deslizables iniciales, donde se muestra información
 * de la Fundación, y manejamos dos opciones de iniciación, logueandose o postulandose con
 * Fundamusical.
 *
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


        /** Buscamos en la preferencias si existen, que ya se haya visualizado antes las vistas */
        if(cargarPreferencias() != true) { /** De no haber sucedido */
            this.pagina = (ViewPager) this.findViewById(R.id.vistaPaginas);
               /** Agregamos los fragment a el ViewPager */
            fragmentoPagina = new ViewPagerFragmentAdapter(getSupportFragmentManager(), this);
            fragmentoPagina.adicionarFragmento(VistaInicialSplashScreenFragment.newInstance());
            fragmentoPagina.adicionarFragmento(VistaInicialEstudiantesFragment.newInstance());
            fragmentoPagina.adicionarFragmento(VistaInicialInstrumentosFragment.newInstance());
            fragmentoPagina.adicionarFragmento(VistaInicialEventosFragment.newInstance());
            fragmentoPagina.adicionarFragmento(VistaInicialPostularseFragment.newInstance());
            this.pagina.setAdapter(fragmentoPagina);
            /** Aqui agregamos los indicadores de página */
            indicadorPagina = (CirclePageIndicator) this.findViewById(R.id.indicador);
            indicadorPagina.setViewPager(pagina);
            indicadorPagina.setSnap(true);
            indicadorPagina.setFillColor(Color.WHITE);
            indicadorPagina.setPageColor(vinotinto_oscuro);
            indicadorPagina.setStrokeColor(Color.TRANSPARENT);
            /** Guardamos la información de que ya se ha instanciado estas vistas */
            guardarPreferencias(true);

            /** Si ya habian sido instanceadas, se redirige a la vista de Tipo de Acceso */
        } else if (cargarPreferencias() == true) {
            startActivity(new Intent(this, VistasAccesoActivity.class).putExtra("acceso", 2));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /** Guardamos las preferencias que ignorar las vistas iniciales cuando inicie de nuevo la app */
    public void guardarPreferencias(boolean status) {
        preferencias = getSharedPreferences("index", Context.MODE_PRIVATE);
        editor = preferencias.edit();
        editor.putBoolean("ignorar", status);
        editor.commit();
    }

    /** Cargamos las preferencias de existir alamacenadas */
    public boolean cargarPreferencias() {
        preferencias = getSharedPreferences("index", Context.MODE_PRIVATE);
        return preferencias.getBoolean("ignorar", false);
    }
}
