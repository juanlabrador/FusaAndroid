package edu.ucla.fusa.android;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.TabPageIndicator;

import edu.ucla.fusa.android.adaptadores.ViewPagerFragmentAdapter;
import edu.ucla.fusa.android.fragmentos.VIewPagerInicialInstrumentosFragment;
import edu.ucla.fusa.android.fragmentos.ViewPagerInicialContactoFragment;
import edu.ucla.fusa.android.fragmentos.ViewPagerInicialEstudiantesFragment;
import edu.ucla.fusa.android.fragmentos.ViewPagerInicialEventosFragment;
import edu.ucla.fusa.android.fragmentos.ViewPagerInicialFragment;
import edu.ucla.fusa.android.fragmentos.ViewPagerInicialProfesoresFragment;
import edu.ucla.fusa.android.fragmentos.ViewPagerInicialSplashScreenFragment;

/**
 * Created by juanlabrador on 16/10/14.
 *
 * Esta clase administra todas las vista deslizables iniciales, donde se muestra información
 * de la Fundación, y manejamos dos opciones de iniciación, logueandose o postulandose con
 * Fundamusical.
 *
 */
public class VistasInicialesActivity extends FragmentActivity {

    private Bundle arguments;
    int position;
    private ViewPager viewPager;
    private ViewPagerFragmentAdapter adapter;
    private TabPageIndicator tabPageIndicator;
    private int[] icons = new int[] {
            R.drawable.ic_cerrar_sesion,
            R.drawable.ic_estudiantes,
            R.drawable.ic_profesores,
            R.drawable.ic_instrumentos,
            R.drawable.ic_eventos,
            R.drawable.ic_contacto
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
        getActionBar().hide();

        //position = getIntent().getIntExtra("position", 0);
        //arguments = new Bundle();
        //arguments.putInt("position", position);

        viewPager = (ViewPager) findViewById(R.id.view_pager_iniciales);
        /** Agregamos los fragment a el ViewPager */

        adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), icons, null);
        adapter.addFragment(ViewPagerInicialSplashScreenFragment.newInstance());
        adapter.addFragment(ViewPagerInicialEstudiantesFragment.newInstance());
        adapter.addFragment(ViewPagerInicialProfesoresFragment.newInstance());
        adapter.addFragment(VIewPagerInicialInstrumentosFragment.newInstance());
        adapter.addFragment(ViewPagerInicialEventosFragment.newInstance());
        adapter.addFragment(ViewPagerInicialContactoFragment.newInstance());
        viewPager.setAdapter(adapter);

        /** Aqui agregamos los indicadores de página */
        tabPageIndicator = (TabPageIndicator) findViewById(R.id.tab_indicator_iniciales);
        tabPageIndicator.setViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
