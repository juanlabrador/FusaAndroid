package edu.ucla.fusa.android;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.NavigationAdapter;
import edu.ucla.fusa.android.fragmentos.CalendarioFragment;
import edu.ucla.fusa.android.fragmentos.ListadoConfiguracionFragment;
import edu.ucla.fusa.android.fragmentos.ListadoNoticiasFragment;
import edu.ucla.fusa.android.fragmentos.PerfilFragment;
import edu.ucla.fusa.android.fragmentos.PrestamoInstrumentoFragment;
import edu.ucla.fusa.android.modelo.ItemListDrawer;

/**
 * Created by juanlabrador on 16/10/14.
 *
 * Esta clase administra el contenido de la aplicación a traves de fragmentos.
 *
 */

public class VistasPrincipalesActivity extends Activity implements AdapterView.OnItemClickListener {

    private DrawerLayout navigationDrawer;
    private ListView navigationList;
    private View header;
    private String[] titulos;
    private ArrayList<ItemListDrawer> items;
    private NavigationAdapter navigationAdapter;
    private TypedArray navigationsIcons;
    private ActionBarDrawerToggle mDrawerToogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        /** Creamos el menu deslizable */
        navigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        navigationList = (ListView) findViewById(R.id.listaFunciones);

        header = getLayoutInflater().inflate(R.layout.custom_header_drawer, null);

        navigationsIcons = getResources().obtainTypedArray(R.array.nav_icons_user);
        navigationList.addHeaderView(header);

        titulos = getResources().getStringArray(R.array.nav_funciones_user);

        items = new ArrayList<ItemListDrawer>();

        items.add(new ItemListDrawer(titulos[0], navigationsIcons.getResourceId(0, -1)));
        items.add(new ItemListDrawer(titulos[1], navigationsIcons.getResourceId(1, -1)));
        items.add(new ItemListDrawer(titulos[2], navigationsIcons.getResourceId(2, -1)));
        items.add(new ItemListDrawer(titulos[3], navigationsIcons.getResourceId(3, -1)));
        items.add(new ItemListDrawer(titulos[4], navigationsIcons.getResourceId(4, -1)));

        navigationsIcons = getResources().obtainTypedArray(R.array.nav_icons_app);
        titulos = getResources().getStringArray(R.array.nav_funciones_app);


        items.add(new ItemListDrawer(titulos[0], navigationsIcons.getResourceId(0, -1)));

        navigationAdapter = new NavigationAdapter(this, items);
        navigationList.setAdapter(navigationAdapter);
        navigationList.setOnItemClickListener(this);

        /** Agregamos el icono de navegación en la esquina superior izquierda al lado del logo */
        mDrawerToogle = new ActionBarDrawerToggle(this, navigationDrawer,
                R.drawable.ic_navigation_drawer,
                R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        navigationDrawer.setDrawerListener(mDrawerToogle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        getFragmentManager().beginTransaction()
                .replace(R.id.frame_container, ListadoNoticiasFragment.newInstance())
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToogle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToogle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToogle.onConfigurationChanged(newConfig);
    }

    /** Deacuerdo a su posición instanciamos el fragmento llamado desde el menú de navegación */
    private void showFragment(int position) {

        switch (position) {
            case 0:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, PerfilFragment.newInstance())
                        //.addToBackStack(null)
                        .commit();
                break;
            case 1:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, ListadoNoticiasFragment.newInstance())
                                //.addToBackStack(null)
                        .commit();
                break;
            case 4:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, CalendarioFragment.newInstance())
                        //.addToBackStack(null)
                        .commit();
                break;
            case 5:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, PrestamoInstrumentoFragment.newInstance())
                                //.addToBackStack(null)
                        .commit();
                break;
            case 6:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, ListadoConfiguracionFragment.newInstance())
                        //.addToBackStack(null)
                        .commit();
                break;
        }

        navigationList.setItemChecked(position, true);
        navigationList.setSelection(position);

        navigationDrawer.closeDrawer(navigationList);
    }

    /** Evento referente al presionar el botón back del teléfono */
    @Override
    public void onBackPressed() {
        if (navigationDrawer.isDrawerOpen(navigationList)) {
            navigationDrawer.closeDrawer(navigationList);
        } else {
            super.onBackPressed();
        }
    }

    /** Evento que ignora los iconos del menú de action bar al desplegar el menú de navegación */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = navigationDrawer.isDrawerOpen(navigationList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /** Evento onClickItem referente a la lista de navegación */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showFragment(position);
    }
}
