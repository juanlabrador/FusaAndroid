package edu.ucla.fusa.android;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.ucla.fusa.android.adaptadores.NavigationAdapter;
import edu.ucla.fusa.android.fragmentos.ConfiguracionListadoFragment;
import edu.ucla.fusa.android.fragmentos.DrawerHorarioFragment;
import edu.ucla.fusa.android.fragmentos.DrawerPostulacionComodatoFragment;
import edu.ucla.fusa.android.fragmentos.EventoCalendarioFragment;
import edu.ucla.fusa.android.fragmentos.DrawerNoticiasListadoFragment;
import edu.ucla.fusa.android.fragmentos.DrawerPerfilFragment;
import edu.ucla.fusa.android.fragmentos.DrawerSolicitudPrestamoFragment;
import edu.ucla.fusa.android.modelo.herramientas.ItemListDrawer;
import java.util.ArrayList;

public class VistasPrincipalesActivity extends FragmentActivity implements AdapterView.OnItemClickListener {

    private View header;
    private TypedArray iconos;
    private ArrayList<ItemListDrawer> items;
    private ActionBarDrawerToggle mDrawerToogle;
    private NavigationAdapter navigationAdapter;
    private DrawerLayout navigationDrawer;
    private ListView navigationList;
    private String[] titulos;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_principal);
        navigationDrawer = ((DrawerLayout) findViewById(R.id.drawer_layout));
        navigationDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        navigationList = ((ListView)findViewById(R.id.lista_funciones));

        Log.i("USUARIO", String.valueOf(getIntent().getIntExtra("user", -1)));
        if (getIntent().getIntExtra("user", -1) == 1) {
            cargarMenuEstudiante();
        } else if (getIntent().getIntExtra("user", -1) == 2) {
            cargarMenuInstructor();
        }

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
                .replace(R.id.frame_container, DrawerNoticiasListadoFragment.newInstance())
                .commit();
    }

    public boolean onCreateOptionsMenu(Menu paramMenu) {
        //getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int position, long paramLong) {
        if (getIntent().getIntExtra("user", -1) == 1) {
            showFragmentEstudiante(position);
        } else if (getIntent().getIntExtra("user", -1) == 2) {
            showFragmentInstructor(position);
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToogle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onPostCreate(Bundle paramBundle) {
        super.onPostCreate(paramBundle);
        mDrawerToogle.syncState();
    }

    public boolean onPrepareOptionsMenu(Menu paramMenu) {
        boolean drawerOpen = navigationDrawer.isDrawerOpen(this.navigationList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(paramMenu);
    }

    public void cargarMenuEstudiante() {
        header = getLayoutInflater().inflate(R.layout.custom_header_drawer, null);
        CircleImageView foto = (CircleImageView) header.findViewById(R.id.iv_foto_perfil_drawer);
        TextView nombre = (TextView) header.findViewById(R.id.etNombreDrawer);
        TextView correo = (TextView) header.findViewById(R.id.etEmailDrawer);
        foto.setImageResource(R.drawable.foto_perfil);
        nombre.setText("Juan Labrador");
        correo.setText("juan@example.com");
        iconos = getResources().obtainTypedArray(R.array.nav_icons_estudiante);
        navigationList.addHeaderView(header);
        titulos = getResources().getStringArray(R.array.nav_funciones_estudiante);
        items = new ArrayList();
        items.add(new ItemListDrawer(
                titulos[0],
                iconos.getResourceId(0, -1)));
        items.add(new ItemListDrawer(
                titulos[1],
                iconos.getResourceId(1, -1)));
        items.add(new ItemListDrawer(
                titulos[2],
                iconos.getResourceId(2, -1)));

        items.add(new ItemListDrawer(null, -1));
        iconos = getResources().obtainTypedArray(R.array.nav_icons_general);
        titulos = getResources().getStringArray(R.array.nav_funciones_general);
        items.add(new ItemListDrawer(
                titulos[0],
                iconos.getResourceId(0, -1)));
        items.add(new ItemListDrawer(
                titulos[1],
                iconos.getResourceId(1, -1)));
        items.add(new ItemListDrawer(
                titulos[2],
                iconos.getResourceId(2, -1)));

        navigationAdapter = new NavigationAdapter(this, items);
        navigationList.setAdapter(navigationAdapter);
        navigationList.setOnItemClickListener(this);
    }

    private void showFragmentEstudiante(int position) {
        getSupportFragmentManager().popBackStack();
        switch (position) {
            case 0:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, DrawerPerfilFragment.newInstance())
                        .commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, DrawerHorarioFragment.newInstance())
                        .commit();
                break;
            case 2:
                break;
            case 3:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, DrawerSolicitudPrestamoFragment.newInstance())
                        .commit();
                break;
            case 5:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, DrawerNoticiasListadoFragment.newInstance())
                        .commit();
                break;
            case 6:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, EventoCalendarioFragment.newInstance())
                        .commit();
                break;
            case 7:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, ConfiguracionListadoFragment.newInstance())
                        .commit();
                break;
        }
        navigationList.setItemChecked(position, true);
        navigationList.setSelection(position);
        navigationDrawer.closeDrawer(navigationList);
    }

    public void cargarMenuInstructor() {
        header = getLayoutInflater().inflate(R.layout.custom_header_drawer, null);
        CircleImageView foto = (CircleImageView) header.findViewById(R.id.iv_foto_perfil_drawer);
        TextView nombre = (TextView) header.findViewById(R.id.etNombreDrawer);
        TextView correo = (TextView) header.findViewById(R.id.etEmailDrawer);
        foto.setImageResource(R.drawable.foto_instructor);
        nombre.setText("Rafael \"Pollo\" Brito");
        correo.setText("profesor@example.com");
        iconos = getResources().obtainTypedArray(R.array.nav_icons_instructor);
        navigationList.addHeaderView(header);
        titulos = getResources().getStringArray(R.array.nav_funciones_instructor);
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
        items.add(new ItemListDrawer(null, -1));
        iconos = getResources().obtainTypedArray(R.array.nav_icons_general);
        titulos = getResources().getStringArray(R.array.nav_funciones_general);
        items.add(new ItemListDrawer(
                titulos[0],
                iconos.getResourceId(0, -1)));
        items.add(new ItemListDrawer(
                titulos[1],
                iconos.getResourceId(1, -1)));
        items.add(new ItemListDrawer(
                titulos[2],
                iconos.getResourceId(2, -1)));

        navigationAdapter = new NavigationAdapter(this, items);
        navigationList.setAdapter(navigationAdapter);
        navigationList.setOnItemClickListener(this);
    }

    private void showFragmentInstructor(int position) {
        getSupportFragmentManager().popBackStack();
        switch (position) {
            case 0:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, DrawerPerfilFragment.newInstance())
                        .commit();
                break;
            case 1:

                break;
            case 2:
                break;
            case 3:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, DrawerPostulacionComodatoFragment.newInstance())
                        .commit();
                break;
            case 4:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, DrawerNoticiasListadoFragment.newInstance())
                        .commit();
                break;
            case 5:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, EventoCalendarioFragment.newInstance())
                        .commit();
                break;
            case 6:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, ConfiguracionListadoFragment.newInstance())
                        .commit();
                break;
        }
        navigationList.setItemChecked(position, true);
        navigationList.setSelection(position);
        navigationDrawer.closeDrawer(this.navigationList);
    }

    public void onBackPressed() {
        if (navigationDrawer.isDrawerOpen(this.navigationList)) {
            navigationDrawer.closeDrawer(this.navigationList);
        } else {
            super.onBackPressed();
        }
    }

    public void onConfigurationChanged(Configuration paramConfiguration) {
        super.onConfigurationChanged(paramConfiguration);
        mDrawerToogle.onConfigurationChanged(paramConfiguration);
    }
}