package edu.ucla.fusa.android;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import edu.ucla.fusa.android.adaptadores.NavigationAdapter;
import edu.ucla.fusa.android.fragmentos.ConfiguracionListadoFragment;
import edu.ucla.fusa.android.fragmentos.EventoCalendarioFragment;
import edu.ucla.fusa.android.fragmentos.NoticiasListadoFragment;
import edu.ucla.fusa.android.fragmentos.PerfilFragment;
import edu.ucla.fusa.android.fragmentos.SolicitudPrestamoFragment;
import edu.ucla.fusa.android.modelo.herramientas.ItemListDrawer;
import java.util.ArrayList;

public class VistasPrincipalesActivity extends FragmentActivity implements AdapterView.OnItemClickListener {

    private FragmentManager fragmentManager;
    private View header;
    private TypedArray iconos;
    private ArrayList<ItemListDrawer> items;
    private ActionBarDrawerToggle mDrawerToogle;
    private NavigationAdapter navigationAdapter;
    private DrawerLayout navigationDrawer;
    private ListView navigationList;
    private String[] titulos;

    public void onBackPressed() {
        if (this.navigationDrawer.isDrawerOpen(this.navigationList)) {
            this.navigationDrawer.closeDrawer(this.navigationList);
        } else {
            super.onBackPressed();
        }
    }

    public void onConfigurationChanged(Configuration paramConfiguration) {
        super.onConfigurationChanged(paramConfiguration);
        this.mDrawerToogle.onConfigurationChanged(paramConfiguration);
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_principal);
        navigationDrawer = ((DrawerLayout) findViewById(R.id.drawer_layout));
        navigationDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        navigationList = ((ListView)findViewById(R.id.lista_funciones));
        header = getLayoutInflater().inflate(R.layout.custom_header_drawer, null);
        iconos = getResources().obtainTypedArray(R.array.nav_icons_user);
        navigationList.addHeaderView(this.header);
        titulos = getResources().getStringArray(R.array.nav_funciones_user);
        items = new ArrayList();
        items.add(new ItemListDrawer(
                this.titulos[0],
                this.iconos.getResourceId(0, -1)));
        items.add(new ItemListDrawer(
                this.titulos[1],
                this.iconos.getResourceId(1, -1)));
        items.add(new ItemListDrawer(
                this.titulos[2],
                this.iconos.getResourceId(2, -1)));
        items.add(new ItemListDrawer(
                this.titulos[3],
                this.iconos.getResourceId(3, -1)));
        items.add(new ItemListDrawer(
                this.titulos[4],
                this.iconos.getResourceId(4, -1)));
        items.add(new ItemListDrawer(
                this.titulos[5],
                this.iconos.getResourceId(5, -1)));

        navigationAdapter = new NavigationAdapter(this, this.items);
        navigationList.setAdapter(this.navigationAdapter);
        navigationList.setOnItemClickListener(this);

        mDrawerToogle = new ActionBarDrawerToggle(this, navigationDrawer,
                R.drawable.ic_navigation_drawer,
                R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View paramAnonymousView) {
                super.onDrawerClosed(paramAnonymousView);
            }

            public void onDrawerOpened(View paramAnonymousView) {
                super.onDrawerOpened(paramAnonymousView);
            }
        };

        navigationDrawer.setDrawerListener(mDrawerToogle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, NoticiasListadoFragment.newInstance())
                .commit();
    }

    private void showFragment(int position) {
        getSupportFragmentManager().popBackStack();
        switch (position) {
            case 0:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, PerfilFragment.newInstance())
                        .commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, NoticiasListadoFragment.newInstance())
                        .commit();
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, EventoCalendarioFragment.newInstance())
                        .commit();
                break;
            case 5:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, SolicitudPrestamoFragment.newInstance())
                        .commit();
                break;
            case 6:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, ConfiguracionListadoFragment.newInstance())
                        .commit();
                break;
        }
        this.navigationList.setItemChecked(position, true);
        this.navigationList.setSelection(position);
        this.navigationDrawer.closeDrawer(this.navigationList);
    }

    public boolean onCreateOptionsMenu(Menu paramMenu) {
        //getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int position, long paramLong) {
        showFragment(position);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.mDrawerToogle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onPostCreate(Bundle paramBundle) {
        super.onPostCreate(paramBundle);
        this.mDrawerToogle.syncState();
    }

    public boolean onPrepareOptionsMenu(Menu paramMenu) {
        boolean drawerOpen = navigationDrawer.isDrawerOpen(this.navigationList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(paramMenu);
    }
}