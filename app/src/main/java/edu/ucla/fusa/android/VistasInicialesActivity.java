package edu.ucla.fusa.android;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
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
import edu.ucla.fusa.android.fragmentos.SplashScreenFragment;
import edu.ucla.fusa.android.modelo.seguridad.Usuario;

public class VistasInicialesActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, ActionBar.TabListener {

    private FragmentViewPagerAdapter mViewPagerAdapter;
    private TypedArray mIconsIndicator;
    private TabPageIndicator mTabIndicator;
    private ViewPager mViewPager;
    private UserTable mUserTable;
    private Usuario mUsuario;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_inicial);
        getActionBar().hide();
        mIconsIndicator = getResources().obtainTypedArray(R.array.nav_icons_inicial);
        mViewPager = (ViewPager) findViewById(R.id.view_pager_iniciales);
        
        mViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mIconsIndicator);
        mViewPagerAdapter.addFragment(SplashScreenFragment.newInstance());
        mViewPagerAdapter.addFragment(InicialEstudiantesFragment.newInstance());
        mViewPagerAdapter.addFragment(InicialProfesoresFragment.newInstance());
        mViewPagerAdapter.addFragment(InicialInstrumentosFragment.newInstance());
        mViewPagerAdapter.addFragment(InicialEventosFragment.newInstance());
        mViewPagerAdapter.addFragment(InicialContactoFragment.newInstance());
        
        mViewPager.setAdapter(mViewPagerAdapter);
        
        mTabIndicator = (TabPageIndicator) findViewById(R.id.tab_iniciales);
        mTabIndicator.setViewPager(mViewPager);
        mTabIndicator.setOnPageChangeListener(this);

        mUserTable = new UserTable(this);
    }

    public void onPageScrollStateChanged(int paramInt) {}

    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {}

    public void onPageSelected(int paramInt) {}

    public void onTabReselected(ActionBar.Tab paramTab, FragmentTransaction paramFragmentTransaction) {}

    public void onTabSelected(ActionBar.Tab paramTab, FragmentTransaction paramFragmentTransaction) {}

    public void onTabUnselected(ActionBar.Tab paramTab, FragmentTransaction paramFragmentTransaction) {
        mViewPager.setCurrentItem(paramTab.getPosition());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUsuario = mUserTable.searchUser();
        // Si hay datos, se redirige al home
        if (mUsuario != null) {
            if (!mUsuario.getUsername().equals("")) {
                startActivity(new Intent(this, VistasPrincipalesActivity.class)
                        .putExtra("TipoUsuario", mUsuario.getTipoUsuario().getId())
                        .putExtra("NombreUsuario", mUsuario.getUsername()));
                finish();
            }
        }
    }
}
