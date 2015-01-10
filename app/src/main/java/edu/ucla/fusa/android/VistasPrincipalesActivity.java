package edu.ucla.fusa.android;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.ucla.fusa.android.DB.EstudianteTable;
import edu.ucla.fusa.android.adaptadores.NavigationAdapter;
import edu.ucla.fusa.android.fragmentos.ConfiguracionListadoFragment;
import edu.ucla.fusa.android.fragmentos.DrawerHorarioFragment;
import edu.ucla.fusa.android.fragmentos.DrawerPostulacionComodatoFragment;
import edu.ucla.fusa.android.fragmentos.EventoCalendarioFragment;
import edu.ucla.fusa.android.fragmentos.DrawerNoticiasListadoFragment;
import edu.ucla.fusa.android.fragmentos.DrawerPerfilFragment;
import edu.ucla.fusa.android.fragmentos.DrawerSolicitudPrestamoFragment;
import edu.ucla.fusa.android.modelo.academico.Estudiante;
import edu.ucla.fusa.android.modelo.herramientas.ItemListDrawer;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;

import java.text.ParseException;
import java.util.ArrayList;

public class VistasPrincipalesActivity extends FragmentActivity implements AdapterView.OnItemClickListener {

    private static String TAG = "VistasPrincipalesActivity";
    private View header;
    private TypedArray iconos;
    private ArrayList<ItemListDrawer> items;
    private ActionBarDrawerToggle mDrawerToogle;
    private NavigationAdapter navigationAdapter;
    private DrawerLayout navigationDrawer;
    private ListView navigationList;
    private String[] titulos;
    private JSONParser jsonParser = new JSONParser();
    private EstudianteTable db;
    private CircleImageView foto;
    private TextView nombre;
    private TextView correo;
    private ProgressBar progress;
    private TextView descripcionProgress;
    private Estudiante estudiante;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_principal);
        db = new EstudianteTable(this);
        getActionBar().hide();
        progress = (ProgressBar) findViewById(R.id.pb_cargando);
        descripcionProgress = (TextView) findViewById(R.id.text_cargando);
        navigationDrawer = ((DrawerLayout) findViewById(R.id.drawer_layout));
        navigationDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        navigationList = ((ListView)findViewById(R.id.lista_funciones));

        Log.i("USUARIO", String.valueOf(getIntent().getIntExtra("tipoUser", -1)));
        if (getIntent().getIntExtra("tipoUser", -1) == 1) {
            try {
                estudiante = db.searchUser();
                if (estudiante != null) {
                    Log.i(TAG, "Entra en los datos guardados");
                    cargarMenuEstudiante(estudiante.getNombre(), estudiante.getCorreo(), estudiante.getImagen());
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_container, DrawerNoticiasListadoFragment.newInstance())
                            .commit();
                } else {
                    Log.i(TAG, "Busca datos en el servidor");
                    new BuscarEstudiante().execute(getIntent().getStringExtra("user"));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (getIntent().getIntExtra("tipoUser", -1) == 2) {
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

    }

    public boolean onCreateOptionsMenu(Menu paramMenu) {
        //getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int position, long paramLong) {
        if (getIntent().getIntExtra("tipoUser", -1) == 1) {
            showFragmentEstudiante(position);
        } else if (getIntent().getIntExtra("tipoUser", -1) == 2) {
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

    private Bitmap convertByteToImage(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public void cargarMenuEstudiante(String n, String c, byte[] f) {
        header = getLayoutInflater().inflate(R.layout.custom_header_drawer, null);
        foto = (CircleImageView) header.findViewById(R.id.iv_foto_perfil_drawer);
        nombre = (TextView) header.findViewById(R.id.etNombreDrawer);
        correo = (TextView) header.findViewById(R.id.etEmailDrawer);
        foto.setImageBitmap(convertByteToImage(f));
        nombre.setText(n);
        correo.setText(c);
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

        getActionBar().show();
        progress.setVisibility(View.GONE);
        descripcionProgress.setVisibility(View.GONE);
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
        foto = (CircleImageView) header.findViewById(R.id.iv_foto_perfil_drawer);
        nombre = (TextView) header.findViewById(R.id.etNombreDrawer);
        correo = (TextView) header.findViewById(R.id.etEmailDrawer);
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

    private class BuscarEstudiante extends AsyncTask<String, Void, Integer> {

        private String nombre;
        private String correo;
        private byte[] foto;
        private Estudiante estudiante;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            SystemClock.sleep(3000);
            int response = -1;
            /** Cargamos los parametros que enviaremos por URL */
            ArrayList<NameValuePair> parametros = new ArrayList<NameValuePair>();
            parametros.add(new BasicNameValuePair("username", params[0]));

            /** Mandamos los parametros y esperemos una respuesta del servidor */
            estudiante = jsonParser.serviceEstudiante(parametros);
            if (estudiante != null) {
                if (estudiante.getId() != -1) { /** Si el estudiante existe */

                    nombre = estudiante.getNombreApellido();
                    correo = estudiante.getCorreo();
                    foto = estudiante.getImagen();
                    /** Guardamos sus datos internamente para que no se loguee de nuevo */
                    db.insertData(
                        estudiante.getNombre(),
                        estudiante.getApellido(),
                        estudiante.getCedula(),
                        estudiante.getCorreo(),
                        estudiante.getDireccion(),
                        estudiante.getEdad(),
                        estudiante.getFechanac(),
                        estudiante.getSexo(),
                        estudiante.getTelefonoFijo(),
                        estudiante.getTelefonoMovil(),
                        estudiante.getImagen(),
                        estudiante.getBecado(),
                        estudiante.getInscritoConservatorio(),
                        estudiante.getInscritoCoro(),
                        estudiante.getInstrumentoPropio());
                    response = 100;
                } else { /** Si el estudiante no existe */
                    response = -1;
                }
            } else { /** Hubo problemas con el servidor o lentitud de la red */
                response = 0;
            }
            return response;
        }

        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);
            if (i == 100) {
                cargarMenuEstudiante(nombre, correo, foto);
                SystemClock.sleep(2000);
                Log.i(TAG, "Felicidades, existe el estudiante");
                progress.setVisibility(View.GONE);
                descripcionProgress.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, DrawerNoticiasListadoFragment.newInstance())
                        .commit();

            } else if (i == -1) {
                Log.i(TAG, "Error, no existe el estudiante");
                errorBusqueda();
            } else {
                Log.i(TAG, "Problemas con conectividad");
                errorServidor();
            }
            getActionBar().show();
        }
    }

    public void errorBusqueda(){
        Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        Toast.makeText(this, R.string.mensaje_error_busqueda, Toast.LENGTH_SHORT).show();
    }

    public void errorServidor(){
        Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        Toast.makeText(this,R.string.mensaje_error_servidor, Toast.LENGTH_SHORT).show();;
    }
}