package edu.ucla.fusa.android;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.viewpagerindicator.TabPageIndicator;

import edu.ucla.fusa.android.DB.UserTable;
import edu.ucla.fusa.android.adaptadores.FragmentViewPagerAdapter;
import edu.ucla.fusa.android.fragmentos.InicialContactoFragment;
import edu.ucla.fusa.android.fragmentos.InicialEstudiantesFragment;
import edu.ucla.fusa.android.fragmentos.InicialEventosFragment;
import edu.ucla.fusa.android.fragmentos.InicialInstrumentosFragment;
import edu.ucla.fusa.android.fragmentos.InicialProfesoresFragment;
import edu.ucla.fusa.android.fragmentos.InicialSplashScreenFragment;
import edu.ucla.fusa.android.modelo.academico.Estudiante;

public class VistasInicialesActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, ActionBar.TabListener {

    private FragmentViewPagerAdapter adapter;
    private SharedPreferences.Editor editor;
    private TypedArray iconos;
    private SharedPreferences sharedPreferences;
    private TabPageIndicator tabPageIndicator;
    private ViewPager viewPager;
    private UserTable db;
    private Estudiante estudiante = new Estudiante();

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_inicial);
        getActionBar().hide();
        iconos = getResources().obtainTypedArray(R.array.nav_icons_inicial);
        viewPager = ((ViewPager) findViewById(R.id.view_pager_iniciales));
        adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), iconos);
        adapter.addFragment(InicialSplashScreenFragment.newInstance());
        adapter.addFragment(InicialEstudiantesFragment.newInstance());
        adapter.addFragment(InicialProfesoresFragment.newInstance());
        adapter.addFragment(InicialInstrumentosFragment.newInstance());
        adapter.addFragment(InicialEventosFragment.newInstance());
        adapter.addFragment(InicialContactoFragment.newInstance());
        viewPager.setAdapter(adapter);
        tabPageIndicator = ((TabPageIndicator)findViewById(R.id.tab_iniciales));
        tabPageIndicator.setViewPager(viewPager);
        tabPageIndicator.setOnPageChangeListener(this);

        db = new UserTable(this);
    }

    public void onPageScrollStateChanged(int paramInt) {}

    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {}

    public void onPageSelected(int paramInt) {}

    public void onTabReselected(ActionBar.Tab paramTab, FragmentTransaction paramFragmentTransaction) {}

    public void onTabSelected(ActionBar.Tab paramTab, FragmentTransaction paramFragmentTransaction) {}

    public void onTabUnselected(ActionBar.Tab paramTab, FragmentTransaction paramFragmentTransaction) {
        this.viewPager.setCurrentItem(paramTab.getPosition());
    }

    @Override
    protected void onResume() {
        super.onResume();
        estudiante = db.searchUser();
        if (estudiante != null)
            if (estudiante.getUsuario().getNombre() != null && estudiante.getUsuario().getClave() != null) {
                startActivity(new Intent(this, VistasPrincipalesActivity.class));
                finish();
            }
    }
}
