package edu.ucla.fusa.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.ucla.fusa.android.DB.DataBaseHelper;
import edu.ucla.fusa.android.DB.EstudianteTable;
import edu.ucla.fusa.android.DB.NoticiasTable;
import edu.ucla.fusa.android.adaptadores.NavigationAdapter;
import edu.ucla.fusa.android.fragmentos.CambiarPasswordFragment;
import edu.ucla.fusa.android.fragmentos.ListadoOpcionesFragment;
import edu.ucla.fusa.android.fragmentos.HorarioClasesFragment;
import edu.ucla.fusa.android.fragmentos.PostulacionComodatoFragment;
import edu.ucla.fusa.android.fragmentos.CalendarioFragment;
import edu.ucla.fusa.android.fragmentos.ListadoNoticiasFragment;
import edu.ucla.fusa.android.fragmentos.SolicitudPrestamoFragment;
import edu.ucla.fusa.android.modelo.academico.Estudiante;
import edu.ucla.fusa.android.modelo.fundacion.Noticia;
import edu.ucla.fusa.android.modelo.herramientas.ItemListDrawer;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class VistasPrincipalesActivity extends FragmentActivity implements AdapterView.OnItemClickListener {


    private static String TAG = "VistasPrincipalesActivity";
    private View mHeaderDrawer;
    private TypedArray mIconsDrawer;
    private ArrayList<ItemListDrawer> mItemsDrawer;
    private ActionBarDrawerToggle mDrawerToogle;
    private NavigationAdapter mNavigationAdapter;
    private DrawerLayout mNavigationDrawer;
    private ListView mListDrawer;
    private String[] mTextDrawer;
    private JSONParser mJSONParser;
    
    private CircleImageView mFoto;
    private TextView mNombre;
    private TextView mCorreo;
    private ProgressBar mLoading;
    private TextView mTextLoading;
    
    private Button mRetryButton;
    private int mTipoUsuario;
    private String mUsername;

    private ArrayList<Noticia> mNoticias;
    private Estudiante mEstudiante;
    private EstudianteTable mEstudianteTable;
    private NoticiasTable mNoticiasTable;
    

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getActionBar().hide();
        setContentView(R.layout.activity_principal);
        
        mEstudianteTable = new EstudianteTable(this);
        mNoticiasTable = new NoticiasTable(this);
        mJSONParser = new JSONParser();
        
        mLoading = (ProgressBar) findViewById(R.id.pb_cargando);
        mTextLoading = (TextView) findViewById(R.id.text_cargando);
        mRetryButton = (Button) findViewById(R.id.btn_reintentar_conexion);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mListDrawer = (ListView) findViewById(R.id.lista_funciones);

        mDrawerToogle = new ActionBarDrawerToggle(this, mNavigationDrawer,
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

        mNavigationDrawer.setDrawerListener(mDrawerToogle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        new LoadingNoticias().execute();
        
        // Leemos los datos del usuario
        mTipoUsuario = getIntent().getIntExtra("TipoUsuario", -1);
        mUsername = getIntent().getStringExtra("NombreUsuario");
        cargarUsuario();

    }
    
    // Usuario
    
    private void cargarUsuario() {
        Log.i(TAG, "Tipo de usuario: " + mTipoUsuario);
        switch (mTipoUsuario) {
            case 1: // Es estudiante
                try {
                    mEstudiante = mEstudianteTable.searchUser();
                    if (mEstudiante != null) {
                        Log.i(TAG, "¡Busca al estudiante en la BD!");
                        cargarMenuEstudiante();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_container, ListadoNoticiasFragment.newInstance())
                                .commit();
                    } else {
                        if (exiteConexionInternet()) {
                            Log.i(TAG, "¡Busca al estudiante en el servidor!");
                            new BuscarEstudiante().execute(mUsername);
                        } else {
                            showOptionRetry();
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case 2: // Es instructor
                cargarMenuInstructor();
                break;
            default:
                showOptionRetry();
                break;
        }
    }

    public boolean exiteConexionInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //State edge = cm.getNetworkInfo(0).getState();
        NetworkInfo.State wifi = cm.getNetworkInfo(1).getState();
        //NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //if (edge == State.CONNECTED || edge == State.CONNECTING) {
        //return true;
        //}else
        if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            return true;
        } else {
            return false;
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

    public boolean onCreateOptionsMenu(Menu paramMenu) {
        //getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int position, long paramLong) {
        switch (mTipoUsuario) {
            case 1:
                showFragmentEstudiante(position);
                break;
            case 2:
                showFragmentInstructor(position);
                break;
            default:
                showOptionRetry();
                break;
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
        boolean drawerOpen = mNavigationDrawer.isDrawerOpen(mListDrawer);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(paramMenu);
    }

    private Bitmap convertByteToImage(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    private byte[] convertImageToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        return  stream.toByteArray();
    }

    private void showOptionRetry() {
        mRetryButton.setVisibility(View.VISIBLE);
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mTipoUsuario) {
                    case 1:
                        new BuscarEstudiante().execute(mUsername);
                        break;
                    case 2:
                        break;
                }
                
            }
        });
        mTextLoading.setText(R.string.mensaje_comprobar_conexion);
        mLoading.setVisibility(View.GONE);
    }

    // Estudiante

    private void cargarPerfilEstudiante(View mHeader) {
        mFoto = (CircleImageView) mHeader.findViewById(R.id.iv_foto_perfil_drawer);
        mNombre = (TextView) mHeader.findViewById(R.id.etNombreDrawer);
        mCorreo = (TextView) mHeader.findViewById(R.id.etEmailDrawer);

        mFoto.setVisibility(View.VISIBLE);
        mNombre.setVisibility(View.VISIBLE);
        mCorreo.setVisibility(View.VISIBLE);

        if (mEstudiante.getImagen() != null) {
            mFoto.setImageBitmap(convertByteToImage(mEstudiante.getImagen()));
        } else {
            mFoto.setImageResource(R.drawable.no_avatar);
        }

        mNombre.setText(mEstudiante.getNombre() + " " + mEstudiante.getApellido());
        mCorreo.setText(mEstudiante.getCorreo());
    }
    
    public void cargarMenuEstudiante() {
        if (mNavigationAdapter != null) {
            mNavigationAdapter.clear();
        }
        mHeaderDrawer = getLayoutInflater().inflate(R.layout.custom_header_drawer, null);
        cargarPerfilEstudiante(mHeaderDrawer);
        mIconsDrawer = getResources().obtainTypedArray(R.array.nav_icons_estudiante);
        mListDrawer.addHeaderView(mHeaderDrawer);
        mTextDrawer = getResources().getStringArray(R.array.nav_funciones_estudiante);
        mItemsDrawer = new ArrayList();
        mItemsDrawer.add(new ItemListDrawer(
                mTextDrawer[0],
                mIconsDrawer.getResourceId(0, -1)));
        mItemsDrawer.add(new ItemListDrawer(
                mTextDrawer[1],
                mIconsDrawer.getResourceId(1, -1)));

        mItemsDrawer.add(new ItemListDrawer(null, -1));

        mIconsDrawer = getResources().obtainTypedArray(R.array.nav_icons_general);
        mTextDrawer = getResources().getStringArray(R.array.nav_funciones_general);
        mItemsDrawer.add(new ItemListDrawer(
                mTextDrawer[0],
                mIconsDrawer.getResourceId(0, -1)));
        mItemsDrawer.add(new ItemListDrawer(
                mTextDrawer[1],
                mIconsDrawer.getResourceId(1, -1)));
        mItemsDrawer.add(new ItemListDrawer(
                mTextDrawer[2],
                mIconsDrawer.getResourceId(2, -1)));
        mItemsDrawer.add(new ItemListDrawer(
                mTextDrawer[3],
                mIconsDrawer.getResourceId(3, -1)));

        mNavigationAdapter = new NavigationAdapter(this, mItemsDrawer);
        mListDrawer.setAdapter(mNavigationAdapter);
        mListDrawer.setOnItemClickListener(this);

        getActionBar().show();
        mLoading.setVisibility(View.GONE);
        mTextLoading.setVisibility(View.GONE);
    }
    
    private void showFragmentEstudiante(int position) {
        getSupportFragmentManager().popBackStack();
        switch (position) {
            case 0:
                break;
            case 1:
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, HorarioClasesFragment.newInstance())
                        .commit();
                break;
            case 2:
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, SolicitudPrestamoFragment.newInstance())
                        .commit();
                break;
            case 4:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_container, ListadoNoticiasFragment.newInstance())
                        .commit();
                break;
            case 5:
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, CalendarioFragment.newInstance())
                        .commit();
                break;
            case 6:
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, ListadoOpcionesFragment.newInstance())
                        .commit();
                break;
            case 7:
                new Logout().execute();
                break;
        }
        
        mListDrawer.setItemChecked(position, true);
        mListDrawer.setSelection(position);
        mNavigationDrawer.closeDrawer(mListDrawer);
    }

    // Instructor

    private void cargarPerfilInstructor(View mHeader) {
        mFoto = (CircleImageView) mHeader.findViewById(R.id.iv_foto_perfil_drawer);
        mNombre = (TextView) mHeader.findViewById(R.id.etNombreDrawer);
        mCorreo = (TextView) mHeader.findViewById(R.id.etEmailDrawer);

        mFoto.setVisibility(View.VISIBLE);
        mNombre.setVisibility(View.VISIBLE);
        mCorreo.setVisibility(View.VISIBLE);

        //if (mEstudiante.getImagen() != null) {
            //mFoto.setImageBitmap(convertByteToImage(mEstudiante.getImagen()));
        //} else {
            mFoto.setImageResource(R.drawable.foto_instructor);
        //}

        mNombre.setText("Rafael \"Pollo\" Brito");
        mCorreo.setText("profesor@example.com");
    }
    
    public void cargarMenuInstructor() {
        if (mNavigationAdapter != null) {
            mNavigationAdapter.clear();
        }
        mHeaderDrawer = getLayoutInflater().inflate(R.layout.custom_header_drawer, null);
        cargarPerfilInstructor(mHeaderDrawer);
        mIconsDrawer = getResources().obtainTypedArray(R.array.nav_icons_instructor);
        mListDrawer.addHeaderView(mHeaderDrawer);
        mTextDrawer = getResources().getStringArray(R.array.nav_funciones_instructor);
        mItemsDrawer = new ArrayList();
        mItemsDrawer.add(new ItemListDrawer(
                mTextDrawer[0],
                mIconsDrawer.getResourceId(0, -1)));
        mItemsDrawer.add(new ItemListDrawer(
                mTextDrawer[1],
                mIconsDrawer.getResourceId(1, -1)));
        
        mItemsDrawer.add(new ItemListDrawer(null, -1));
        mIconsDrawer = getResources().obtainTypedArray(R.array.nav_icons_general);
        mTextDrawer = getResources().getStringArray(R.array.nav_funciones_general);
        mItemsDrawer.add(new ItemListDrawer(
                mTextDrawer[0],
                mIconsDrawer.getResourceId(0, -1)));
        mItemsDrawer.add(new ItemListDrawer(
                mTextDrawer[1],
                mIconsDrawer.getResourceId(1, -1)));
        mItemsDrawer.add(new ItemListDrawer(
                mTextDrawer[2],
                mIconsDrawer.getResourceId(2, -1)));
        mItemsDrawer.add(new ItemListDrawer(
                mTextDrawer[3],
                mIconsDrawer.getResourceId(3, -1)));

        mNavigationAdapter = new NavigationAdapter(this, mItemsDrawer);
        mListDrawer.setAdapter(mNavigationAdapter);
        mListDrawer.setOnItemClickListener(this);
    }

    private void showFragmentInstructor(int position) {
        getSupportFragmentManager().popBackStack();
        switch (position) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, PostulacionComodatoFragment.newInstance())
                        .commit();
                break;
            case 4:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, ListadoNoticiasFragment.newInstance())
                        .commit();
                break;
            case 5:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, CalendarioFragment.newInstance())
                        .commit();
                break;
            case 6:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, CambiarPasswordFragment.newInstance())
                        .commit();
                break;
            case 7:
                new Logout().execute();
                break;
        }
        mListDrawer.setItemChecked(position, true);
        mListDrawer.setSelection(position);
        mNavigationDrawer.closeDrawer(mListDrawer);
    }

    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(mListDrawer)) {
            mNavigationDrawer.closeDrawer(mListDrawer);
        } else {
            super.onBackPressed();
        }
    }

    public void onConfigurationChanged(Configuration paramConfiguration) {
        super.onConfigurationChanged(paramConfiguration);
        mDrawerToogle.onConfigurationChanged(paramConfiguration);
    }

    private class BuscarEstudiante extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.setVisibility(View.VISIBLE);
            mTextLoading.setText(R.string.cargando);
            mRetryButton.setVisibility(View.GONE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            int response;
            
            // Parametros via GET
            ArrayList<NameValuePair> parametros = new ArrayList<NameValuePair>();
            parametros.add(new BasicNameValuePair("username", params[0]));

            // Aplicamos el servicio
            mEstudiante = mJSONParser.serviceEstudiante(parametros);
            if (mEstudiante != null) {
                if (mEstudiante.getId() != -1) { // Existe el estudiante

                    // Guardamos los datos internamente
                    mEstudianteTable.insertData(
                            mEstudiante.getId(),
                            mEstudiante.getNombre(),
                            mEstudiante.getApellido(),
                            mEstudiante.getCedula(),
                            mEstudiante.getCorreo(),
                            mEstudiante.getEdad(),
                            mEstudiante.getFechanac(),
                            mEstudiante.getSexo(),
                            mEstudiante.getTelefonoFijo(),
                            mEstudiante.getTelefonoMovil(),
                            mEstudiante.getImagen(),
                            mEstudiante.getBecado(),
                            mEstudiante.getInscritoConservatorio(),
                            mEstudiante.getInscritoCoro(),
                            mEstudiante.getInstrumentoPropio());
                    response = 100;
                } else { // No existe el estudiante
                    response = -1;
                }
            } else { // Problemas con el servidor o conexión
                response = 0;
            }
            return response;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 100:
                    cargarMenuEstudiante();
                    // Delay 2 sg
                    SystemClock.sleep(2000);

                    Log.i(TAG, "¡Estudiante localizado!");
                    mLoading.setVisibility(View.GONE);
                    mTextLoading.setVisibility(View.GONE);
                    getActionBar().show();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_container, ListadoNoticiasFragment.newInstance())
                            .commit();
                    break;
                case -1:
                    Log.i(TAG, "¡No existe el estudiante!");
                    showOptionRetry();
                    errorBusqueda();
                    break;
                case 0:
                    Log.i(TAG, "¡Problemas con el servidor o de conexion!");
                    showOptionRetry();
                    errorServidor();
                    break;
            }
        }
    }
    
    // Noticias 

    private class LoadingNoticias extends AsyncTask<Void, Void, Integer> {

        protected Integer doInBackground(Void[] voids) {
            SystemClock.sleep(2000);
            Log.i(TAG, "¡Buscando noticias!");
            mNoticias = new ArrayList<Noticia>();
            mNoticias = mJSONParser.serviceLoadingNoticias();
            if (mNoticias == null) {
                return 0;
            } else if (mNoticias.size() != 0) {
                for (Noticia noticia : mNoticias) {                   
                    
                    //Guardamos en la base de datos
                    mNoticiasTable.insertData(noticia.getTitulo(),
                            noticia.getDescripcion(),
                            noticia.getFechapublicacion().getTime(),
                            noticia.getImagen(),
                            noticia.getId());
                }
                return 100;
            } else {
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 100:
                    Log.i(TAG, "¡Noticias guardadas!");
                    break;
                case 0:
                    Log.i(TAG, "¡Error al buscar las noticias!");
                    break;
            }
            
        }
    }
    
    // Cerrar sesión 

    private class Logout extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog;

        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(VistasPrincipalesActivity.this);
            dialog.setMessage(getResources().getString(R.string.cerrar_sesion));
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        protected Void doInBackground(Void[] params) {
            SystemClock.sleep(2000);
            deleteDatabase(DataBaseHelper.NAME);
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dialog.dismiss();
            startActivity(new Intent(getApplicationContext(), VistasInicialesActivity.class));
            finish();
        }
    }
   
}