package edu.ucla.fusa.android;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;


import edu.ucla.fusa.android.DB.UserTable;
import edu.ucla.fusa.android.adaptadores.FragmentViewPagerAdapter;
import edu.ucla.fusa.android.fragmentos.InicialContactoFragment;
import edu.ucla.fusa.android.fragmentos.InicialAspiranteFragment;
import edu.ucla.fusa.android.fragmentos.SplashScreenFragment;
import edu.ucla.fusa.android.modelo.seguridad.Usuario;
import me.relex.circleindicator.CircleIndicator;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class VistasInicialesActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, ActionBar.TabListener {

    private FragmentViewPagerAdapter mViewPagerAdapter;
    private CircleIndicator mCircleIndicator;
    private ViewPager mViewPager;
    private UserTable mUserTable;
    private Usuario mUsuario;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_inicial);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/HelveticaNeueLight.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        mViewPager = (ViewPager) findViewById(R.id.view_pager_iniciales);

        mViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), null);
        mViewPagerAdapter.addFragment(SplashScreenFragment.newInstance());
        mViewPagerAdapter.addFragment(InicialAspiranteFragment.newInstance());
        mViewPagerAdapter.addFragment(InicialContactoFragment.newInstance());
        
        mViewPager.setAdapter(mViewPagerAdapter);

        mCircleIndicator = (CircleIndicator) findViewById(R.id.indicador);
        mCircleIndicator.setViewPager(mViewPager);

        mUserTable = new UserTable(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
