package edu.ucla.fusa.android;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.HexagonImageView;
import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import edu.ucla.fusa.android.DB.DataBaseHelper;
import edu.ucla.fusa.android.DB.EstudianteTable;
import edu.ucla.fusa.android.DB.EventoTable;
import edu.ucla.fusa.android.DB.LugarTable;
import edu.ucla.fusa.android.DB.NoticiasTable;
import edu.ucla.fusa.android.DB.TipoPrestamoTable;
import edu.ucla.fusa.android.DB.UserTable;
import edu.ucla.fusa.android.adaptadores.ListOpcionesAdapter;
import edu.ucla.fusa.android.adaptadores.NavigationAdapter;
import edu.ucla.fusa.android.fragmentos.CambiarPasswordFragment;
import edu.ucla.fusa.android.fragmentos.HorarioClasesFragment;
import edu.ucla.fusa.android.fragmentos.CalendarioFragment;
import edu.ucla.fusa.android.fragmentos.ListadoNoticiasFragment;
import edu.ucla.fusa.android.fragmentos.SolicitudPrestamoFragment;
import edu.ucla.fusa.android.modelo.academico.Estudiante;
import edu.ucla.fusa.android.modelo.evento.Evento;
import edu.ucla.fusa.android.modelo.evento.Lugar;
import edu.ucla.fusa.android.modelo.fundacion.Noticia;
import edu.ucla.fusa.android.modelo.herramientas.ItemListDrawer;
import edu.ucla.fusa.android.modelo.herramientas.ItemListNoticia;
import edu.ucla.fusa.android.modelo.herramientas.ItemListOpcionesMultimedia;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import edu.ucla.fusa.android.modelo.instrumentos.TipoPrestamo;
import edu.ucla.fusa.android.modelo.seguridad.Usuario;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class VistasPrincipalesActivity extends FragmentActivity implements AdapterView.OnItemClickListener {

    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 3;
    private static String TAG = "VistasPrincipalesActivity";
    private View mHeaderDrawer;
    private TypedArray mIconsDrawer;
    private ArrayList<ItemListDrawer> mItemsDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationAdapter mNavigationAdapter;
    private DrawerLayout mNavigationDrawer;
    private ListView mListDrawer;
    private String[] mTextDrawer;
    private JSONParser mJSONParser;
    
    private SharedPreferences mPreferencias;
    private SharedPreferences.Editor mEditor;
    private HexagonImageView mFoto;
    private TextView mNombre;
    private TextView mCorreo;
    private CircularProgressBar mLoading;
    private TextView mTextLoading;
    private Toolbar mToolbar;
    
    private Button mRetryButton;
    private String mUsername;
    private Uri mFotoCaptureUri;
    private ArrayList<ItemListOpcionesMultimedia> mItemsMultimedia;

    private ArrayList<Noticia> mNoticias;
    private Estudiante mEstudiante;
    private EstudianteTable mEstudianteTable;
    private NoticiasTable mNoticiasTable;
    private UserTable mUserTable;
    private Usuario mUsuario;
    private Bitmap mBitmap;

    private DrawerArrowDrawable mDrawerArrow;
    private byte[] mPhoto;
    private File mFile;
    private ArrayList<TipoPrestamo> mTiposPrestamos;
    private TipoPrestamoTable mTipoPrestamoTable;


    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_principal);
        mItemsMultimedia = new ArrayList<>();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/HelveticaNeueLight.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        mDrawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.noticias_titulo_barra);
        mToolbar.setNavigationIcon(mDrawerArrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNavigationDrawer.isDrawerOpen(mListDrawer)) {
                    mNavigationDrawer.closeDrawer(mListDrawer);
                } else {
                    mNavigationDrawer.openDrawer(mListDrawer);
                }
            }
        });
        mToolbar.setVisibility(View.GONE);
        
        mEstudianteTable = new EstudianteTable(this);
        mNoticiasTable = new NoticiasTable(this);
        mUserTable = new UserTable(this);
        mJSONParser = new JSONParser();
        mTipoPrestamoTable = new TipoPrestamoTable(this);
        
        mLoading = (CircularProgressBar) findViewById(R.id.pb_cargando);
        mTextLoading = (TextView) findViewById(R.id.text_cargando);
        mRetryButton = (Button) findViewById(R.id.btn_reintentar_conexion);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mListDrawer = (ListView) findViewById(R.id.lista_funciones);

        mDrawerToggle = new ActionBarDrawerToggle(this, mNavigationDrawer,
                mDrawerArrow,
                R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View paramAnonymousView) {
                super.onDrawerClosed(paramAnonymousView);
            }

            public void onDrawerOpened(View paramAnonymousView) {
                super.onDrawerOpened(paramAnonymousView);
            }
        };

        mNavigationDrawer.setDrawerListener(mDrawerToggle);

        new LoadingEventos().execute();
        new LoadingNoticias().execute();
        new LoadingTipoPrestamo().execute();

        // Leemos los datos del usuario
        mUsername = getIntent().getStringExtra("NombreUsuario");
        cargarUsuario();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    
    // Usuario
    
    private void cargarUsuario() {
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

    public boolean onCreateOptionsMenu(Menu paramMenu) {
        //getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int position, long paramLong) {
        getSupportFragmentManager().popBackStack();
        switch (position) {
            case 0:showDialog();
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
            case 3:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_container, ListadoNoticiasFragment.newInstance())
                        .commit();
                break;
            case 4:
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, CalendarioFragment.newInstance())
                        .commit();
                break;
            case 5:
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, CambiarPasswordFragment.newInstance())
                        .commit();
                break;
            case 6:
                new Logout().execute();
                break;
        }

        mListDrawer.setItemChecked(position, true);
        mListDrawer.setSelection(position);
        mNavigationDrawer.closeDrawer(mListDrawer);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    private void showOptionRetry() {
        mRetryButton.setVisibility(View.VISIBLE);
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BuscarEstudiante().execute(mUsername);
            }
        });
        mTextLoading.setText(R.string.mensaje_comprobar_conexion);
        mLoading.setVisibility(View.GONE);
    }

    // Estudiante

    private void cargarHeader(View mHeader) {
        mFoto = (HexagonImageView) mHeader.findViewById(R.id.iv_foto_perfil_drawer);
        mNombre = (TextView) mHeader.findViewById(R.id.etNombreDrawer);
        mCorreo = (TextView) mHeader.findViewById(R.id.etCorreoDrawer);
        mFoto.setVisibility(View.VISIBLE);
        mNombre.setVisibility(View.VISIBLE);
        mCorreo.setVisibility(View.VISIBLE);
        if (mEstudiante.getImagen().length != 0) {
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
        cargarHeader(mHeaderDrawer);
        mIconsDrawer = getResources().obtainTypedArray(R.array.iconos_menu);
        mListDrawer.addHeaderView(mHeaderDrawer);
        mTextDrawer = getResources().getStringArray(R.array.contenido_menu);
        mItemsDrawer = new ArrayList();
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
        mItemsDrawer.add(new ItemListDrawer(
                mTextDrawer[4],
                mIconsDrawer.getResourceId(4, -1)));
        mItemsDrawer.add(new ItemListDrawer(
                mTextDrawer[5],
                mIconsDrawer.getResourceId(5, -1)));

        mNavigationAdapter = new NavigationAdapter(this, mItemsDrawer);
        mListDrawer.setAdapter(mNavigationAdapter);
        mListDrawer.setOnItemClickListener(this);

        mToolbar.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.GONE);
        mTextLoading.setVisibility(View.GONE);
    }

    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(mListDrawer)) {
            mNavigationDrawer.closeDrawer(mListDrawer);
        } else {
            super.onBackPressed();
        }
    }

    public class BuscarEstudiante extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.setVisibility(View.VISIBLE);
            mTextLoading.setText(R.string.mensaje_cargando);
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
                            mEstudiante.getInstrumentoPropio(),
                            mEstudiante.getUsuario().getUsername());
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
                    Log.i(TAG, "¡Estudiante localizado!");
                    break;
                case -1:
                    Log.i(TAG, "¡No existe el estudiante!");
                    
                    break;
                case 0:
                    Log.i(TAG, "¡Problemas con el servidor o de conexion!");
                    
                    break;
            }
        }
    }
    
    // Noticias 

    public class LoadingNoticias extends AsyncTask<Void, Void, Integer> {

        private ArrayList<ItemListNoticia> mItemsNoticias;

        protected Integer doInBackground(Void[] voids) {
            SystemClock.sleep(2000);
            mItemsNoticias = mNoticiasTable.searchNews();
            if (mItemsNoticias.size() == 0) {
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
                    return -1;
                }
            } else {
                return 200;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 200:
                    Log.i(TAG, "¡Noticias por actualizar!");
                    break;
                case 100:
                    Log.i(TAG, "¡Noticias guardadas!");
                    mLoading.setVisibility(View.GONE);
                    mTextLoading.setVisibility(View.GONE);
                    mToolbar.setVisibility(View.VISIBLE);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_container, ListadoNoticiasFragment.newInstance())
                            .commit();
                    break;
                case -1:
                    Log.i(TAG, "¡No hay noticias!");
                    showOptionRetry();
                    SnackbarManager.show(
                            Snackbar.with(getApplicationContext())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_busqueda));
                    break;
                case 0:
                    Log.i(TAG, "¡Error al buscar las noticias!");
                    showOptionRetry();
                    SnackbarManager.show(
                            Snackbar.with(getApplicationContext())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_servidor));
                    break;
            }
            
        }
    }
    
    // Eventos
    
    public class LoadingEventos extends AsyncTask<Void, Void, Integer> {
        
        private String TAG = "LoadingEventos";
        private Calendar mDesde;
        private Calendar mHasta;
        private SimpleDateFormat mDateFormat;
        private ArrayList<Evento> mEventos;
        private EventoTable mEventoTable;
        private LugarTable mLugarTable;
        private Lugar mLugar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            mDesde = Calendar.getInstance();
            mHasta = Calendar.getInstance();
            // Sumamos un año
            mHasta.set(mDesde.get(Calendar.YEAR) + 1, mDesde.get(Calendar.MONTH), mDesde.get(Calendar.DAY_OF_MONTH));
            mLugar = new Lugar();
            mEventoTable = new EventoTable(getApplicationContext());
            mLugarTable = new LugarTable(getApplicationContext());
            Log.i(TAG, mDateFormat.format(mDesde.getTime()));
            Log.i(TAG, mDateFormat.format(mHasta.getTime()));
        }

        @Override
        protected Integer doInBackground(Void... params) {
            mEventos = mEventoTable.searchEventos(mDateFormat.format(mDesde.getTime()));
            Log.i(TAG, "Cantidad de eventos: " + mEventos.size());
            if (mEventos.size() == 0) {
                /** Cargamos los parametros que enviaremos por URL */
                ArrayList<NameValuePair> parametros = new ArrayList<NameValuePair>();
                parametros.add(new BasicNameValuePair("desde", mDateFormat.format(mDesde.getTime())));
                parametros.add(new BasicNameValuePair("hasta", mDateFormat.format(mHasta.getTime())));
                mEventos = mJSONParser.serviceLoadingEventos(parametros);
                if (mEventos == null) {
                    return 0;
                } else if (mEventos.size() != 0) {
                    for (Evento evento : mEventos) {
                        mEventoTable.insertData(
                                evento.getNombre(),
                                evento.getLogistica(),
                                evento.getFecha(),
                                evento.getHora(),
                                evento.getId(),
                                evento.getLugar().getId()
                        );
                        mLugar = mLugarTable.searchLugar(String.valueOf(evento.getLugar().getId()));
                        if (mLugar == null) {
                            Log.i(TAG, "¡No existe el lugar!");
                            mLugarTable.insertData(
                                    evento.getLugar().getId(),
                                    evento.getLugar().getDescripcion(),
                                    evento.getLugar().getDireccion()
                            );
                        }
                    }
                    return 100;
                } else {
                    return -1;
                }
            } else {
                return 200;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 200:
                    Log.i(TAG, "¡Eventos por actualizar!");
                    break;
                case 100:
                    Log.i(TAG, "¡Eventos guardados!");
                    break;
                case -1:
                    Log.i(TAG, "¡No hay eventos!");
                    break;
                case 0:
                    Log.i(TAG, "¡Problemas con el servidor!");
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
            mUsuario = mUserTable.searchUser();
        }

        protected Void doInBackground(Void[] params) {
            SystemClock.sleep(2000);
            mPreferencias = getSharedPreferences("usuario", Context.MODE_PRIVATE);
            mEditor = mPreferencias.edit();
            mEditor.clear();
            mEditor.putString("usuario", mUsuario.getUsername());
            mEditor.putString("foto", Base64.encodeToString(mUsuario.getFoto(), Base64.DEFAULT));
            mEditor.commit();
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

    // Cambiar foto de perfil

    private void showDialog() {
        mItemsMultimedia.clear();
        mItemsMultimedia.add(new ItemListOpcionesMultimedia(getString(R.string.opcion_multimedia_camara), R.drawable.ic_camara));
        mItemsMultimedia.add(new ItemListOpcionesMultimedia(getString(R.string.opcion_multimedia_galeria), R.drawable.ic_cambiar_foto));
        ListOpcionesAdapter adapter = new ListOpcionesAdapter(this, mItemsMultimedia);
        new AlertDialog.Builder(this).setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int item) {
                if (item == 0) { /** Desde la cámara */
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    mFotoCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                    intent.putExtra("output", mFotoCaptureUri);
                    try {
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else { /** Desde galeria de imagenes */
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction("android.intent.action.GET_CONTENT");
                    startActivityForResult(Intent.createChooser(intent, "Completa la acción usando..."), PICK_FROM_FILE);
                }
            }
        }).create().show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        
        mUsuario = mUserTable.searchUser();
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case PICK_FROM_CAMERA:
                    doCrop();
                    break;
                case PICK_FROM_FILE:
                    mFotoCaptureUri = data.getData();
                    doCrop();
                    break;
                case CROP_FROM_CAMERA:
                    Bundle b = data.getExtras();
                    if (b != null) {
                        mBitmap = b.getParcelable("data");
                        mPhoto = convertImageToByte(mBitmap);
                        mUsuario.setFoto(mPhoto);
                        mEstudiante.setImagen(mPhoto);
                        new UploadFoto().execute(mUsuario);
                        new UploadFotoEstudiante().execute(mEstudiante);
                    }
                    mFile = new File(mFotoCaptureUri.getPath());
                    if (mFile.exists()) {
                        mFile.delete();
                        Log.i(TAG, "¡Encontro la foto en la memoria!");
                    } else {
                        Log.i(TAG, "¡No encontro la foto en la memoria!");
                    }
                    break;
            }
        }
    }

    private void doCrop() {
        startActivityForResult(new Intent("com.android.camera.action.CROP")
                .setDataAndType(mFotoCaptureUri, "image/*")
                .putExtra("crop", "true")
                .putExtra("outputX", 200)
                .putExtra("outputY", 200)
                .putExtra("aspectX", 1)
                .putExtra("aspectY", 1)
                .putExtra("scale", true)
                .putExtra("return-data", true), CROP_FROM_CAMERA);
    }

    private class UploadFoto extends AsyncTask<Usuario, Void, Integer> {

        @Override
        protected Integer doInBackground(Usuario... usuarios) {
            return mJSONParser.updateUsuario(usuarios[0]);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case -1:
                    Log.i(TAG, "¡No se cargo la foto!");
                    break;
                case 100:
                    Log.i(TAG, "¡Foto actualizada!");
                    break;
            }
        }
    }

    private class UploadFotoEstudiante extends AsyncTask<Estudiante, Void, Integer> {

        @Override
        protected Integer doInBackground(Estudiante... estudiantes) {
            return mJSONParser.updateEstudiante(estudiantes[0]);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case -1:
                    Log.i(TAG, "¡No se cargo la foto!");
                    break;
                case 100:
                    Log.i(TAG, "¡Foto actualizada!");
                    ((HexagonImageView) findViewById(R.id.iv_foto_perfil_drawer)).setImageBitmap(mBitmap);
                    break;
            }
        }
    }
    
    // Tipo de Prestamo 

    private class LoadingTipoPrestamo extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            mTiposPrestamos = mTipoPrestamoTable.searchTiposPrestamos();
            if (mTiposPrestamos.size() == 0) {
                mTiposPrestamos = mJSONParser.serviceLoadingTipoPrestamo();
                if (mTiposPrestamos == null) {
                    return 0;
                } else if (mTiposPrestamos.size() != 0) {
                    for (TipoPrestamo mTipoPrestamo : mTiposPrestamos) {
                        mTipoPrestamoTable.insertData(
                                mTipoPrestamo.getId(),
                                mTipoPrestamo.getDescripcion()
                        );
                    }
                    return 100;
                } else {
                    return 0;
                }
            } else {
                return 200;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 0:
                    Log.i(TAG, "¡No hay tipos de prestamos!");
                    break;
                case 100:
                    Log.i(TAG, "¡Tipos de prestamos guardados!");
                    break;
                case 200:
                    Log.i(TAG, "¡Ya existen tipos de prestamo!");
                    break;
            }
        }
    }
}