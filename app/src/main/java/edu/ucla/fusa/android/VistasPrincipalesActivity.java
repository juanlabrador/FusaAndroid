package edu.ucla.fusa.android;

import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.HexagonImageView;
import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;


import at.markushi.ui.CircleButton;
import edu.ucla.fusa.android.DB.DataBaseHelper;
import edu.ucla.fusa.android.DB.EstudianteTable;
import edu.ucla.fusa.android.DB.UserTable;
import edu.ucla.fusa.android.adaptadores.NavigationAdapter;
import edu.ucla.fusa.android.fragmentos.AspiranteFragment;
import edu.ucla.fusa.android.fragmentos.CambiarPasswordFragment;
import edu.ucla.fusa.android.fragmentos.ContenedorHorarioFragment;
import edu.ucla.fusa.android.fragmentos.ContenedorNoticiasFragment;
import edu.ucla.fusa.android.fragmentos.EstatusPrestamoFragment;
import edu.ucla.fusa.android.fragmentos.CalendarioFragment;
import edu.ucla.fusa.android.fragmentos.NotificacionesFragment;
import edu.ucla.fusa.android.fragmentos.SolicitudPrestamoFragment;
import edu.ucla.fusa.android.modelo.academico.Estudiante;
import edu.ucla.fusa.android.modelo.herramientas.ItemListDrawer;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import edu.ucla.fusa.android.modelo.instrumentos.Prestamo;
import edu.ucla.fusa.android.modelo.instrumentos.SolicitudPrestamo;
import edu.ucla.fusa.android.modelo.seguridad.Usuario;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import me.drakeet.materialdialog.MaterialDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class VistasPrincipalesActivity extends FragmentActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private static final int APROBADO = 1;
    private static final int RECHAZADO = 2;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 3;
    private static String TAG = "VistasPrincipalesActivity";
    private View mHeaderDrawer;
    private TypedArray mIconsDrawer;
    private ArrayList<ItemListDrawer> mItemsDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationAdapter mNavigationAdapter;
    public DrawerLayout mNavigationDrawer;
    public ListView mListDrawer;
    private String[] mTextDrawer;
    private JSONParser mJSONParser;
    private LinearLayout mContenedorPrincipal;
    
    private SharedPreferences mPreferencias;
    private SharedPreferences.Editor mEditor;
    private HexagonImageView mFoto;
    private TextView mNombre;
    private CircularProgressBar mLoading;
    private TextView mTextLoading;
    private Toolbar mToolbar;
    
    private CircleButton mRetryButton;
    private String mUsername;
    private Uri mFotoCaptureUri;
    
    private Estudiante mEstudiante;
    private EstudianteTable mEstudianteTable;
    private UserTable mUserTable;
    private Usuario mUsuario;
    private Bitmap mBitmap;

    private DrawerArrowDrawable mDrawerArrow;
    private byte[] mPhoto;
    private File mFile;
    private int mPositionList;

    private SolicitudPrestamo mSolicitudPrestamo;
    private Prestamo mPrestamo;
    
    private ListView mListPhoto;
    private ArrayAdapter<String> mAdapterPhoto;
    private MaterialDialog mDialog;
    private NotificationManager mManager;
    private LoadingSolicitudPrestamo mServiceSolicitud;
    
    private LinearLayout mNotificaciones;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_principal);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/HelveticaNeue.ttf")
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
                
        mEstudianteTable = new EstudianteTable(this);
        mUserTable = new UserTable(this);
        mJSONParser = new JSONParser();
        
        mLoading = (CircularProgressBar) findViewById(R.id.pb_cargando_principal);
        mTextLoading = (TextView) findViewById(R.id.principal_vacio);
        mRetryButton = (CircleButton) findViewById(R.id.button_network_principal);
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoading.setVisibility(View.VISIBLE);
                mTextLoading.setText(R.string.mensaje_cargando);
                mTextLoading.setVisibility(View.VISIBLE);
                mRetryButton.setVisibility(View.GONE);
                mContenedorPrincipal.setVisibility(View.GONE);
                new BuscarEstudiante().execute(mUsuario.getUsername());
            }
        });
        
        mContenedorPrincipal = (LinearLayout) findViewById(R.id.contenedor_principal);
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
        
        // Leemos los datos del usuario
        mUsername = getIntent().getStringExtra("NombreUsuario");
        cargarUsuario();

    }

    @Override
    protected void onResume() {
        super.onResume();
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
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    // Usuario
    
    private void cargarUsuario() {
        mUsuario = mUserTable.searchUser();
        if (mUsuario != null) {
            Log.i(TAG, "¡Busca al estudiante en la BD!");
            cargarMenuEstudiante();
            mEstudiante = mEstudianteTable.searchUser();
            if (mEstudiante != null) {
                mContenedorPrincipal.setVisibility(View.VISIBLE);
                mLoading.setVisibility(View.GONE);
                mTextLoading.setVisibility(View.GONE);
                mRetryButton.setVisibility(View.GONE);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_container, ContenedorNoticiasFragment.newInstance())
                        .commit();
            } else {
                if (exiteConexionInternet()) {
                    Log.i(TAG, "¡Busca al estudiante en el servidor!");
                    new BuscarEstudiante().execute(mUsername);
                } else {
                    showOptionRetry();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mServiceSolicitud != null) {
            if (!mServiceSolicitud.isCancelled()) {
                mServiceSolicitud.cancel(true);
            }
        }
    }

    public boolean exiteConexionInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State edge = cm.getNetworkInfo(0).getState();
        NetworkInfo.State wifi = cm.getNetworkInfo(1).getState();
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (edge == NetworkInfo.State.CONNECTED || edge == NetworkInfo.State.CONNECTING) {
            return true;
        }else if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            return true;
        } else {
            return false;
        }
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int position, long paramLong) {
        getSupportFragmentManager().popBackStack();
        mPositionList = position;
        switch (mPositionList) {
            case 0:
                showDialog();
                break;
            case 1:
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, ContenedorHorarioFragment.newInstance())
                        .commit();
                break;
            case 2:
                mServiceSolicitud = new LoadingSolicitudPrestamo();
                mServiceSolicitud.execute(mEstudiante.getId());
                SnackbarManager.show(
                        Snackbar.with(getApplicationContext())
                                .text(R.string.prestamo_buscando), VistasPrincipalesActivity.this);
            case 3:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_container, ContenedorNoticiasFragment.newInstance())
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
                        .replace(R.id.frame_container, AspiranteFragment.newInstance())
                        .commit();
                break;
            case 6:
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, CambiarPasswordFragment.newInstance())
                        .commit();
                break;
            case 7:
                new Logout().execute();
                break;
        }

        mListDrawer.setItemChecked(mPositionList, true);
        mListDrawer.setSelection(mPositionList);
        mNavigationDrawer.closeDrawer(mListDrawer);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.boton_notificaciones:
                mNavigationDrawer.closeDrawer(mListDrawer);
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, NotificacionesFragment.newInstance())
                        .commit();
                break;
        }

    }
    
    
    private void cargarHeader(View mHeader) {
        mNotificaciones = (LinearLayout) mHeader.findViewById(R.id.boton_notificaciones);
        mNotificaciones.setOnClickListener(this);
        mFoto = (HexagonImageView) mHeader.findViewById(R.id.iv_foto_perfil_drawer);
        mNombre = (TextView) mHeader.findViewById(R.id.etNombreDrawer);
        mFoto.setVisibility(View.VISIBLE);
        mNombre.setVisibility(View.VISIBLE);
        if (mUsuario.getFoto() != null && mUsuario.getFoto().length > 40) {
            mFoto.setImageBitmap(convertByteToImage(mUsuario.getFoto()));
        } else {
            mFoto.setImageResource(R.drawable.no_avatar);
        }

        mNombre.setText(mUsuario.getNombre() + " " + mUsuario.getApellido());
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
        mItemsDrawer.add(new ItemListDrawer(
                mTextDrawer[6],
                mIconsDrawer.getResourceId(6, -1)));

        mNavigationAdapter = new NavigationAdapter(this, mItemsDrawer);
        mListDrawer.setAdapter(mNavigationAdapter);
        mListDrawer.setOnItemClickListener(this);
        mListDrawer.setItemChecked(3, true);
        mListDrawer.setSelection(3);

        mLoading.setVisibility(View.GONE);
        mTextLoading.setVisibility(View.GONE);
    }

    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(mListDrawer)) {
            mNavigationDrawer.closeDrawer(mListDrawer);
        } else {
            Log.i(TAG, "¡Cantidad de fragmentos activos: " + getSupportFragmentManager().getBackStackEntryCount());
            if (getSupportFragmentManager().getBackStackEntryCount() > 0 && getSupportFragmentManager().getBackStackEntryCount() < 2) {
                mListDrawer.setItemChecked(3, true);
                mListDrawer.setSelection(3);
                getSupportFragmentManager().popBackStack(getSupportFragmentManager().getBackStackEntryAt(0).getId(), getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
            } else {
                //mListDrawer.setItemChecked(3, true);
                //mListDrawer.setSelection(3);
                super.onBackPressed();
            }
        }
    }
    
    public void nuevaSolicitudPrestamo() {
        mSolicitudPrestamo = null;
        mPrestamo = null;
    }

    public class BuscarEstudiante extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.setVisibility(View.VISIBLE);
            mTextLoading.setText(R.string.mensaje_cargando);
            mTextLoading.setVisibility(View.VISIBLE);
            mRetryButton.setVisibility(View.GONE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            SystemClock.sleep(2000);
            Log.i(TAG, "¡Busca al estudiante en el servidor!");
            // Aplicamos el servicio
            mEstudiante = mJSONParser.serviceEstudiante(params[0]);
            if (mEstudiante != null) {
                if (mEstudiante.getId() != -1) { // Existe el estudiante
                    // Guardamos los datos internamente
                    mEstudianteTable.insertData(
                            mEstudiante.getId(),
                            mEstudiante.getNombre(),
                            mEstudiante.getApellido(),
                            mEstudiante.getCedula(),
                            mEstudiante.getCorreo());
                    return 100;
                } else { // No existe el estudiante
                    return -1;
                }
            } else { // Problemas con el servidor o conexión
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 100:
                    // Delay 2 sg
                    Log.i(TAG, "¡Estudiante localizado!");
                    mContenedorPrincipal.setVisibility(View.VISIBLE);
                    mLoading.setVisibility(View.GONE);
                    mTextLoading.setVisibility(View.GONE);
                    mRetryButton.setVisibility(View.GONE);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_container, ContenedorNoticiasFragment.newInstance())
                            .commit();
                    break;
                case -1:
                    Log.i(TAG, "¡No existe el estudiante!");
                    mContenedorPrincipal.setVisibility(View.GONE);
                    mLoading.setVisibility(View.GONE);
                    mTextLoading.setText(R.string.mensaje_reintentar_estudiante);
                    mTextLoading.setVisibility(View.VISIBLE);
                    mRetryButton.setVisibility(View.VISIBLE);
                    break;
                case 0:
                    Log.i(TAG, "¡Problemas con el servidor o de conexion!");
                    mContenedorPrincipal.setVisibility(View.GONE);
                    mLoading.setVisibility(View.GONE);
                    mTextLoading.setText(R.string.mensaje_error_servidor);
                    mTextLoading.setVisibility(View.VISIBLE);
                    mRetryButton.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
    
    // Cambiar foto de perfil

    private void showDialog() {
        mDialog = new MaterialDialog(this);
        mAdapterPhoto = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mAdapterPhoto.add(getString(R.string.opcion_multimedia_camara));
        mAdapterPhoto.add(getString(R.string.opcion_multimedia_galeria));
        mListPhoto = new ListView(this);
        mListPhoto.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (8 * scale + 0.5f);
        mListPhoto.setPadding(0, dpAsPixels, 0, dpAsPixels);
        mListPhoto.setDividerHeight(0);
        mListPhoto.setAdapter(mAdapterPhoto);
        mListPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) { /** Desde la cámara */
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
                mDialog.dismiss();
                Log.i(TAG, "¡Selecciono el evento " + i + " de la lista");
            }
        });
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setTitle("Cambia tu foto de perfil")
                .setContentView(mListPhoto)
                .show();
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
                        new UploadFoto().execute(mUsuario);
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
        protected void onPreExecute() {
            super.onPreExecute();
            sendNotificacion();
        }

        @Override
        protected Integer doInBackground(Usuario... usuarios) {
            return mJSONParser.updateUsuario(usuarios[0]);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 0:
                    Log.i(TAG, "¡No se cargo la foto!");
                    SnackbarManager.show(
                            Snackbar.with(getApplicationContext())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_excepcion), VistasPrincipalesActivity.this);
                    mManager.cancel(2);
                    break;
                case -1:
                    Log.i(TAG, "¡No se cargo la foto!");
                    SnackbarManager.show(
                            Snackbar.with(getApplicationContext())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_foto), VistasPrincipalesActivity.this);
                    mManager.cancel(2);
                    break;
                case 100:
                    Log.i(TAG, "¡Foto actualizada!");
                    SnackbarManager.show(
                            Snackbar.with(getApplicationContext())
                                    .text(R.string.foto_actualizada), VistasPrincipalesActivity.this);
                    ((HexagonImageView) findViewById(R.id.iv_foto_perfil_drawer)).setImageBitmap(mBitmap);
                    mManager.cancel(2);
                    mUserTable.updateFoto(mUsuario.getUsername(), mUsuario.getFoto());
                    break;
            }
        }
    }

    // Solicitud de prestamo 
    
    public class LoadingSolicitudPrestamo extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            mSolicitudPrestamo = mJSONParser.serviceSolicitudPrestamoPorEstudiante(integers[0]);
            if (mSolicitudPrestamo != null) {  // Existe internamente, comprobar su estatus
                if (mSolicitudPrestamo.getId() != -1) {
                    if (!mSolicitudPrestamo.getEstatus().equals("finalizado")) {
                        return 100;
                    } else {
                        return -1;
                    }
                } else {
                    return -1;
                }
            } else {
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 100:
                    Log.i(TAG, "¡Tiene unas solicitud!");
                    Bundle mID = new Bundle();
                    mID.putInt("id_solicitud", mSolicitudPrestamo.getId());
                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, EstatusPrestamoFragment.newInstance(mID))
                            .commit();
                    break;
                case 0:
                    Log.i(TAG, "¡Problemas de conexion!");
                    SnackbarManager.show(
                            Snackbar.with(getApplicationContext())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_servidor), VistasPrincipalesActivity.this);
                    break;
                case -1:
                    Log.i(TAG, "¡No tiene una solicitud!");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, SolicitudPrestamoFragment.newInstance())
                            .commit();
                    break;
            }
        }
    }
    
    private void notificacion(int titulo, int encabezado, int estatus) {
        String[] notasAprobado = new String[] {
                "¡Tu solicitud fue APROBADA! :)",
                "Recuerda pasar por las oficinas", 
                "para recoger tu instrumento."};

        String[] notasRechazado = new String[] {
                "¡Lo sentimos! :(", 
                "Su solicitud fue RECHAZADA.",
                "Puedo volver a solicitar",
                "nuevamente un prestamo."};

        NotificationCompat.InboxStyle mInboxStyle =
                new NotificationCompat.InboxStyle();

        NotificationCompat.Builder mNotificacion = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(titulo))
                .setTicker(getString(encabezado))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        
        if (estatus == 1) {
            mInboxStyle.addLine(notasAprobado[0]);
            mInboxStyle.addLine(notasAprobado[1]);
            mInboxStyle.addLine(notasAprobado[2]);
        } else {
            mInboxStyle.addLine(notasRechazado[0]);
            mInboxStyle.addLine(notasRechazado[1]);
            mInboxStyle.addLine(notasRechazado[2]);
            mInboxStyle.addLine(notasRechazado[3]);
        }
        
        mNotificacion.setStyle(mInboxStyle);
        mNotificacion.setAutoCancel(true);
        NotificationManager mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mManager.notify(1, mNotificacion.build());
                
        
    }

    private void sendNotificacion() {

        NotificationCompat.Builder mNotificacion = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.contraseña_enviar))
                .setTicker(getString(R.string.contraseña_enviar))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        mNotificacion.setAutoCancel(true);
        mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mManager.notify(2, mNotificacion.build());

    }
    
    public void actualizarSolicitud() {
        mListDrawer.setItemChecked(3, true);
        mListDrawer.setSelection(3);
    }

    public void actualizarPostulacion() {
        mListDrawer.setItemChecked(3, true);
        mListDrawer.setSelection(3);
    }

    public void actualizarPassword() {
        mListDrawer.setItemChecked(3, true);
        mListDrawer.setSelection(3);
    }

    private class Logout extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            mUsuario = mUserTable.searchUser();
            mContenedorPrincipal.setVisibility(View.GONE);
            mTextLoading.setText(R.string.mensaje_cargando);
            mTextLoading.setVisibility(View.VISIBLE);
            mLoading.setVisibility(View.VISIBLE);
            mRetryButton.setVisibility(View.GONE);
        }


        protected Void doInBackground(Void[] params) {
            SystemClock.sleep(3000);
            mPreferencias = getSharedPreferences("usuario", Context.MODE_PRIVATE);
            mEditor = mPreferencias.edit();
            mEditor.clear();
            mEditor.putString("usuario", mUsuario.getUsername());
            if (mUsuario.getFoto() != null) {
                mEditor.putString("foto", Base64.encodeToString(mUsuario.getFoto(), Base64.DEFAULT));
            } else {
                mEditor.putString("foto", "");
            }
            mEditor.commit();
            Log.i(TAG, "¡Cerrando sesión...!");
            DataBaseHelper.getInstance(getApplicationContext()).close();
            deleteDatabase(DataBaseHelper.NAME);
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            startActivity(new Intent(VistasPrincipalesActivity.this, VistasInicialesActivity.class));
            finish();
        }
    }
}