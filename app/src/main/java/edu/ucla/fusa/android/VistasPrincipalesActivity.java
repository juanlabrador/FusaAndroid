package edu.ucla.fusa.android;

import android.app.AlertDialog;
import android.app.NotificationManager;
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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import edu.ucla.fusa.android.DB.AgrupacionTable;
import edu.ucla.fusa.android.DB.ClaseParticularTable;
import edu.ucla.fusa.android.DB.DiaTable;
import edu.ucla.fusa.android.DB.EstudianteTable;
import edu.ucla.fusa.android.DB.EventoTable;
import edu.ucla.fusa.android.DB.HorarioAreaTable;
import edu.ucla.fusa.android.DB.HorarioTable;
import edu.ucla.fusa.android.DB.InstructorTable;
import edu.ucla.fusa.android.DB.LugarTable;
import edu.ucla.fusa.android.DB.NoticiasTable;
import edu.ucla.fusa.android.DB.PrestamoTable;
import edu.ucla.fusa.android.DB.SolicitudPrestamoTable;
import edu.ucla.fusa.android.DB.TipoInstrumentoTable;
import edu.ucla.fusa.android.DB.TipoPrestamoTable;
import edu.ucla.fusa.android.DB.UserTable;
import edu.ucla.fusa.android.adaptadores.NavigationAdapter;
import edu.ucla.fusa.android.fragmentos.CambiarPasswordFragment;
import edu.ucla.fusa.android.fragmentos.ContenedorHorarioFragment;
import edu.ucla.fusa.android.fragmentos.EstatusPrestamoFragment;
import edu.ucla.fusa.android.fragmentos.CalendarioFragment;
import edu.ucla.fusa.android.fragmentos.ListadoNoticiasFragment;
import edu.ucla.fusa.android.fragmentos.LogoutFragment;
import edu.ucla.fusa.android.fragmentos.SolicitudPrestamoFragment;
import edu.ucla.fusa.android.modelo.academico.Agrupacion;
import edu.ucla.fusa.android.modelo.academico.ClaseParticular;
import edu.ucla.fusa.android.modelo.academico.Dia;
import edu.ucla.fusa.android.modelo.academico.Estudiante;
import edu.ucla.fusa.android.modelo.academico.Horario;
import edu.ucla.fusa.android.modelo.academico.Instructor;
import edu.ucla.fusa.android.modelo.evento.Evento;
import edu.ucla.fusa.android.modelo.evento.Lugar;
import edu.ucla.fusa.android.modelo.fundacion.Noticia;
import edu.ucla.fusa.android.modelo.herramientas.ItemListDrawer;
import edu.ucla.fusa.android.modelo.herramientas.ItemListNoticia;
import edu.ucla.fusa.android.modelo.herramientas.ItemListOpcionesMultimedia;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import edu.ucla.fusa.android.modelo.instrumentos.Prestamo;
import edu.ucla.fusa.android.modelo.instrumentos.SolicitudPrestamo;
import edu.ucla.fusa.android.modelo.instrumentos.TipoInstrumento;
import edu.ucla.fusa.android.modelo.instrumentos.TipoPrestamo;
import edu.ucla.fusa.android.modelo.seguridad.Usuario;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import me.drakeet.materialdialog.MaterialDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VistasPrincipalesActivity extends FragmentActivity implements AdapterView.OnItemClickListener {

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
    private DrawerLayout mNavigationDrawer;
    private ListView mListDrawer;
    private String[] mTextDrawer;
    private JSONParser mJSONParser;
    
    private SharedPreferences mPreferencias;
    private SharedPreferences.Editor mEditor;
    private HexagonImageView mFoto;
    private TextView mNombre;
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
    private ArrayList<TipoInstrumento> mTiposInstrumentos;
    private TipoInstrumentoTable mTipoInstrumentoTable;
    private int mPositionList;
    
    private SolicitudPrestamoTable mSolicitudPrestamoTable;
    private PrestamoTable mPrestamoTable;
    private SolicitudPrestamo mSolicitudPrestamo;
    private Prestamo mPrestamo;
    
    private ListView mListPhoto;
    private ArrayAdapter<String> mAdapterPhoto;
    private MaterialDialog mDialog;
    private NotificationManager mManager;


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
        mTipoInstrumentoTable = new TipoInstrumentoTable(this);
        
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
        new LoadingTipoInstrumentos().execute();

        // Leemos los datos del usuario
        mUsername = getIntent().getStringExtra("NombreUsuario");
        cargarUsuario();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mToolbar.setVisibility(View.GONE);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    // Usuario
    
    private void cargarUsuario() {
        mEstudiante = mEstudianteTable.searchUser();
        if (mEstudiante != null) {
            Log.i(TAG, "¡Busca al estudiante en la BD!");
            cargarMenuEstudiante();
            new LoadingSolicitudPrestamo().execute(mEstudiante.getId());
        } else {
            if (exiteConexionInternet()) {
                Log.i(TAG, "¡Busca al estudiante en el servidor!");
                new BuscarEstudiante().execute(mUsername);
            } else {
                showOptionRetry();
            }
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

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int position, long paramLong) {
        getSupportFragmentManager().popBackStack();
        mPositionList = position;
        switch (mPositionList) {
            case 0:showDialog();
                break;
            case 1:
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, ContenedorHorarioFragment.newInstance())
                        .commit();
                break;
            case 2:
                if (mSolicitudPrestamo == null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, SolicitudPrestamoFragment.newInstance(this))
                            .commit();
                } else {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, EstatusPrestamoFragment.newInstance(this))
                            .commit();
                }
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
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.principal, LogoutFragment.newInstance())
                    .commit();
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

    private void cargarHeader(View mHeader) {
        mFoto = (HexagonImageView) mHeader.findViewById(R.id.iv_foto_perfil_drawer);
        mNombre = (TextView) mHeader.findViewById(R.id.etNombreDrawer);
        mFoto.setVisibility(View.VISIBLE);
        mNombre.setVisibility(View.VISIBLE);
        if (mEstudiante.getImagen() != null) {
            mFoto.setImageBitmap(convertByteToImage(mEstudiante.getImagen()));
        } else {
            mFoto.setImageResource(R.drawable.no_avatar);
        }

        mNombre.setText(mEstudiante.getNombre() + " " + mEstudiante.getApellido());
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
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                mListDrawer.setItemChecked(3, true);
                mListDrawer.setSelection(3);
                getSupportFragmentManager().popBackStack(getSupportFragmentManager().getBackStackEntryAt(0).getId(), getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
            } else {
                mListDrawer.setItemChecked(3, true);
                mListDrawer.setSelection(3);
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
            mRetryButton.setVisibility(View.GONE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            int response;
            Log.i(TAG, "¡Busca al estudiante en el servidor!");
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
                            mEstudiante.getUsuario().getUsername());
                    new LoadingSolicitudPrestamo().execute(mEstudiante.getId());
                    new LoadingAgrupacion().execute(mEstudiante.getId());
                    new LoadingClasePaticulares().execute(mEstudiante.getId());
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
    
    private class LoadingAgrupacion extends AsyncTask<Integer, Void, Void> {

        private AgrupacionTable mAgrupacionTable;
        private Agrupacion mAgrupacion;
        private HorarioTable mHorarioTable;
        private InstructorTable mInstructorTable;
        private Instructor mInstructor;
        private HorarioAreaTable mHorarioAreaTable;
        private Horario mHorario;
        private DiaTable mDiaTable;
        private Dia mDia;
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mAgrupacionTable = new AgrupacionTable(getApplicationContext());
            mInstructorTable = new InstructorTable(getApplicationContext());
            mHorarioTable = new HorarioTable(getApplicationContext());
            mAgrupacion = new Agrupacion();
            mHorarioAreaTable = new HorarioAreaTable(getApplicationContext());
            mDia = new Dia();
            mDiaTable = new DiaTable(getApplicationContext());
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            
            mAgrupacion = mAgrupacionTable.searchAgrupacion();
            if (mAgrupacion != null) {
                Log.i(TAG, "¡Ya tengo una agrupacion guardada!");
            } else {
                Log.i(TAG, "¡Buscando agrupacion en el servidor");
                mAgrupacion = mJSONParser.serviceAgrupacionEstudiante(integers[0]);
                if (mAgrupacion != null) {
                    Log.i(TAG, "¡Estoy en una agrupacion!");
                    mAgrupacionTable.insertData(
                            mAgrupacion.getDescripcion(),
                            mAgrupacion.getTipoAgrupacion().getDescripcion(),
                            mAgrupacion.getInstructor().getId(),
                            mAgrupacion.getId()
                    );
                    
                    mInstructor = mInstructorTable.searchInstructor(mAgrupacion.getInstructor().getId());
                    if (mInstructor == null) {
                        Log.i(TAG, "¡No existe el instructor en agrupacion!");
                        mInstructorTable.insertData(
                                mAgrupacion.getInstructor().getId(),
                                mAgrupacion.getInstructor().getNombre(),
                                mAgrupacion.getInstructor().getApellido(),
                                mAgrupacion.getInstructor().getCorreo(),
                                mAgrupacion.getInstructor().getTelefonoMovil(),
                                mAgrupacion.getInstructor().getTelefonoFijo(),
                                mAgrupacion.getInstructor().getImagen()

                        );
                    }
                    
                    for (int i = 0; i < mAgrupacion.getHorarioArea().size(); i++){
                        
                        mHorarioAreaTable.insertData(
                                0, 
                                mAgrupacion.getId(), 
                                mAgrupacion.getHorarioArea().get(i).getId()
                        );
                        
                        mHorario = mHorarioTable.searchHorario(mAgrupacion.getHorarioArea().get(i).getId());
                        if (mHorario == null) {
                            Log.i(TAG, "¡No hay este horario para la agrupacion!");
                            
                            mHorarioTable.insertData(
                                    mAgrupacion.getHorarioArea().get(i).getHorario().getHorario_id(),
                                    mAgrupacion.getHorarioArea().get(i).getHorario().getDia().getDia_id(),
                                    mAgrupacion.getHorarioArea().get(i).getHorario().getHoraInicio(),
                                    mAgrupacion.getHorarioArea().get(i).getHorario().getHoraFin(),
                                    mAgrupacion.getHorarioArea().get(i).getId()
                            );
                            
                            mDia = mDiaTable.search(mAgrupacion.getHorarioArea().get(i).getHorario().getDia().getDia_id());
                            if (mDia == null) {
                                mDiaTable.insertData(
                                        mAgrupacion.getHorarioArea().get(i).getHorario().getDia().getDia_id(),
                                        mAgrupacion.getHorarioArea().get(i).getHorario().getDia().getDescripcion()
                                );
                            }
                            
                        } else {
                            Log.i(TAG, "¡Ya tenemos este horario de la agrupacion!");
                        }
                        
                    }
                    Log.i(TAG, "¡Guardado correctamente!");
                } else {
                    Log.i(TAG, "¡No estoy en una agrupacion!");
                }
            }
            return null;
        }
    }

    private class LoadingClasePaticulares extends AsyncTask<Integer, Void, Void> {

        private ClaseParticularTable mClaseParticularTable;
        private List<ClaseParticular> mClaseParticulares;
        private HorarioTable mHorarioTable;
        private InstructorTable mInstructorTable;
        private Instructor mInstructor;
        private HorarioAreaTable mHorarioAreaTable;
        private Horario mHorario;
        private DiaTable mDiaTable;
        private Dia mDia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mClaseParticularTable = new ClaseParticularTable(getApplicationContext());
            mInstructorTable = new InstructorTable(getApplicationContext());
            mHorarioTable = new HorarioTable(getApplicationContext());
            mClaseParticulares= new ArrayList<>();
            mHorarioAreaTable = new HorarioAreaTable(getApplicationContext());
            mDia = new Dia();
            mDiaTable = new DiaTable(getApplicationContext());
        }

        @Override
        protected Void doInBackground(Integer... integers) {

            mClaseParticulares = mClaseParticularTable.searchClases();
            if (mClaseParticulares != null) {
                Log.i(TAG, "¡Ya tengo mis clases guardada!");
            } else {
                Log.i(TAG, "¡Buscando mis clases en el servidor");
                mClaseParticulares = mJSONParser.serviceClaseEstudiante(integers[0]);
                if (mClaseParticulares.size() != 0) {
                    Log.i(TAG, "¡Estoy en una agrupacion!");
                    
                    for (int i = 0; i < mClaseParticulares.size(); i++) {
                        mClaseParticularTable.insertData(
                                mClaseParticulares.get(i).getCatedra().getDescripcion(),
                                mClaseParticulares.get(i).getInstructor().getId(),
                                mClaseParticulares.get(i).getId()
                                
                        );
                        
                        mInstructor = mInstructorTable.searchInstructor(mClaseParticulares.get(i).getInstructor().getId());
                        if (mInstructor == null) {
                            Log.i(TAG, "¡No existe el instructor en Clase Particular!");
                            mInstructorTable.insertData(
                                    mClaseParticulares.get(i).getInstructor().getId(),
                                    mClaseParticulares.get(i).getInstructor().getNombre(),
                                    mClaseParticulares.get(i).getInstructor().getApellido(),
                                    mClaseParticulares.get(i).getInstructor().getCorreo(),
                                    mClaseParticulares.get(i).getInstructor().getTelefonoMovil(),
                                    mClaseParticulares.get(i).getInstructor().getTelefonoFijo(),
                                    mClaseParticulares.get(i).getInstructor().getImagen()

                            );
                        }
                        
                        
                        for (int j = 0; j < mClaseParticulares.get(i).getHorarioArea().size(); j++) {

                            mHorarioAreaTable.insertData(
                                   mClaseParticulares.get(i).getId(),
                                    0,
                                    mClaseParticulares.get(i).getHorarioArea().get(j).getId()
                            );
                            
                            mHorario = mHorarioTable.searchHorario(mClaseParticulares.get(i).getHorarioArea().get(j).getId());
                            if (mHorario == null) {
                                Log.i(TAG, "¡No existe este horario para la clase!");
                                mHorarioTable.insertData(
                                        mClaseParticulares.get(i).getHorarioArea().get(j).getHorario().getHorario_id(),
                                        mClaseParticulares.get(i).getHorarioArea().get(j).getHorario().getDia().getDia_id(),
                                        mClaseParticulares.get(i).getHorarioArea().get(j).getHorario().getHoraInicio(),
                                        mClaseParticulares.get(i).getHorarioArea().get(j).getHorario().getHoraFin(),
                                        mClaseParticulares.get(i).getHorarioArea().get(j).getId()
                                );

                                mDia = mDiaTable.search(mClaseParticulares.get(i).getHorarioArea().get(j).getHorario().getDia().getDia_id());
                                if (mDia == null) {
                                    mDiaTable.insertData(
                                            mClaseParticulares.get(i).getHorarioArea().get(j).getHorario().getDia().getDia_id(),
                                            mClaseParticulares.get(i).getHorarioArea().get(j).getHorario().getDia().getDescripcion()
                                    );
                                }
                            } else {
                                Log.i(TAG, "¡Ya tenemos este horario para la clase!");
                            }
                        }
                    }
                    
                    Log.i(TAG, "¡Guardado correctamente!");
                } else {
                    Log.i(TAG, "¡No estoy en una agrupacion!");
                }
            }
            return null;
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
                mNoticias = new ArrayList<>();
                mNoticias = mJSONParser.serviceLoadingNoticias();
                if (mNoticias == null) {
                    return 0;
                } else if (mNoticias.size() != 0) {
                    for (Noticia noticia : mNoticias) {
                        if (noticia.getImagen() != null) {
                            mItemsNoticias.add(new ItemListNoticia(
                                    noticia.getId(),
                                    noticia.getTitulo(),
                                    noticia.getFechapublicacion().getTime(),
                                    noticia.getImagen(),
                                    noticia.getDescripcion(),
                                    1));
                            //Guardamos en la base de datos
                            mNoticiasTable.insertData(noticia.getTitulo(),
                                    noticia.getDescripcion(),
                                    noticia.getFechapublicacion().getTime(),
                                    noticia.getImagen(),
                                    noticia.getId(),
                                    1);
                        } else {
                            mItemsNoticias.add(new ItemListNoticia(
                                    noticia.getId(),
                                    noticia.getTitulo(),
                                    noticia.getFechapublicacion().getTime(),
                                    noticia.getImagen(),
                                    noticia.getDescripcion(),
                                    0));
                            //Guardamos en la base de datos
                            mNoticiasTable.insertData(noticia.getTitulo(),
                                    noticia.getDescripcion(),
                                    noticia.getFechapublicacion().getTime(),
                                    noticia.getImagen(),
                                    noticia.getId(),
                                    0);
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
                    Log.i(TAG, "¡Noticias por actualizar!");
                    mLoading.setVisibility(View.GONE);
                    mTextLoading.setVisibility(View.GONE);
                    mToolbar.setVisibility(View.VISIBLE);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_container, ListadoNoticiasFragment.newInstance())
                            .commit();
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
        private ArrayList<Evento> mEventos;
        private EventoTable mEventoTable;
        private LugarTable mLugarTable;
        private Lugar mLugar;
        private int mUltimoEvento;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mEventoTable = new EventoTable(getApplicationContext());
            mLugarTable = new LugarTable(getApplicationContext());
        }

        @Override
        protected Integer doInBackground(Void... params) {
            mEventos = mEventoTable.searchEventos();
            Log.i(TAG, "Cantidad de eventos: " + mEventos.size());
            if (mEventos.size() == 0) {
                mEventos = mJSONParser.serviceLoadingEventos();
                if (mEventos == null) {
                    return 0;
                } else if (mEventos.size() != 0) {
                    for (Evento evento : mEventos) {
                        mEventoTable.insertData(
                                evento.getNombre(),
                                evento.getDescripcion(),
                                evento.getFecha(),
                                evento.getHora(),
                                evento.getId(),
                                evento.getLugar().getId()
                        );
                        mLugar = mLugarTable.searchLugar(evento.getLugar().getId());
                        if (mLugar == null) {
                            Log.i(TAG, "¡No existe el lugar!");
                            mLugarTable.insertData(
                                    evento.getLugar().getId(),
                                    evento.getLugar().getDescripcion(),
                                    evento.getLugar().getDireccion()
                            );
                        } else {
                            Log.i(TAG, "¡Existe el lugar!");
                        }
                    }
                    return 100;
                } else {
                    return -1;
                }
            } else {
                if (mEventos.size() < 30) {
                    mUltimoEvento = mEventoTable.searchUltimoEvento();
                    mEventos = mJSONParser.serviceLoadingNuevosEventos(mUltimoEvento);
                    if (mEventos == null) {
                        return -2;
                    } else if (mEventos.size() != 0) {
                        for (Evento evento : mEventos) {
                            mEventoTable.insertData(
                                    evento.getNombre(),
                                    evento.getDescripcion(),
                                    evento.getFecha(),
                                    evento.getHora(),
                                    evento.getId(),
                                    evento.getLugar().getId()
                            );
                            mLugar = mLugarTable.searchLugar(evento.getLugar().getId());
                            if (mLugar == null) {
                                Log.i(TAG, "¡No existe el lugar para actualizar!");
                                mLugarTable.insertData(
                                        evento.getLugar().getId(),
                                        evento.getLugar().getDescripcion(),
                                        evento.getLugar().getDireccion()
                                );
                            } else {
                                Log.i(TAG, "¡Si existe el lugar para actualizar!");
                            }                          
                        } 
                        return 200;
                    } else {
                        return -2;
                    }
                }
            }
            Log.i(TAG, "¡No hay eventos ni viejos ni nuevos!");
            return -1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 200:
                    Log.i(TAG, "¡Eventos actualizados!");
                    break;
                case 100:
                    Log.i(TAG, "¡Eventos guardados!");
                    break;
                case -2:
                    Log.i(TAG, "¡No hay eventos nuevos!");
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
                        mEstudiante.setImagen(mPhoto);
                        mEstudianteTable.updateFoto(mEstudiante.getId(), mEstudiante.getImagen());
                        mUserTable.updateFoto(mUsuario.getId(), mUsuario.getFoto());
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
        protected void onPreExecute() {
            super.onPreExecute();
            sendNotificacion();
        }

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
                    SnackbarManager.show(
                            Snackbar.with(getApplicationContext())
                                    .text(R.string.mensaje_error_foto));
                    break;
                case 100:
                    Log.i(TAG, "¡Foto actualizada!");
                    SnackbarManager.show(
                            Snackbar.with(getApplicationContext())
                                    .text(R.string.foto_actualizada));
                    ((HexagonImageView) findViewById(R.id.iv_foto_perfil_drawer)).setImageBitmap(mBitmap);
                    mManager.cancel(2);
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

    // Tipo de Instrumento

    private class LoadingTipoInstrumentos extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            mTiposInstrumentos = mTipoInstrumentoTable.searchTiposInstrumentos();
            if (mTiposInstrumentos.size() == 0) {
                mTiposInstrumentos = mJSONParser.serviceLoadingTipoInstrumento();
                if (mTiposInstrumentos == null) {
                    return 0;
                } else if (mTiposInstrumentos.size() != 0) {
                    for (TipoInstrumento mTipoInstrumento : mTiposInstrumentos) {
                        mTipoInstrumentoTable.insertData(
                                mTipoInstrumento.getId(),
                                mTipoInstrumento.getDescripcion()
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
                    Log.i(TAG, "¡No hay tipos de instrumentos!");
                    break;
                case 100:
                    Log.i(TAG, "¡Tipos de instrumentos guardados!");
                    break;
                case 200:
                    Log.i(TAG, "¡Ya existen tipos de instrumentos!");
                    break;
            }
        }
    }
    
    // Solicitud de prestamo 
    
    public class LoadingSolicitudPrestamo extends AsyncTask<Integer, Void, Void> {

        private String mEstatusSolicitud;
        private int mIDSolicitud, mIDPrestamo;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSolicitudPrestamoTable = new SolicitudPrestamoTable(VistasPrincipalesActivity.this);
            mJSONParser = new JSONParser();
            mPrestamoTable = new PrestamoTable(VistasPrincipalesActivity.this);

        }

        @Override
        protected Void doInBackground(Integer... integers) {
            mSolicitudPrestamo = mSolicitudPrestamoTable.searchSolicitudPrestamo();
            if (mSolicitudPrestamo != null) {  // Existe internamente, comprobar su estatus
                mEstatusSolicitud = mSolicitudPrestamo.getEstatus();
                if (!mEstatusSolicitud.equals("finalizado")) {
                    if (!mEstatusSolicitud.equals("rechazado")) {
                        mIDSolicitud = mSolicitudPrestamo.getId();
                        mSolicitudPrestamo = mJSONParser.serviceSolicitudPrestamo(mIDSolicitud);
                        if (mSolicitudPrestamo != null) {
                            if (mSolicitudPrestamo.getEstatus().equals(mEstatusSolicitud)) {
                                Log.i(TAG, "¡No ha cambiado el estatus!");
                            } else {
                                Log.i(TAG, "¡Cambio el estatus, actualiza!");
                                mSolicitudPrestamoTable.updateData(
                                        mSolicitudPrestamo.getId(),
                                        mSolicitudPrestamo.getEstatus());
                                mEstatusSolicitud = mSolicitudPrestamo.getEstatus();
                                if (mEstatusSolicitud.equals("aprobado")) {
                                    Log.i(TAG, "¡Cambio a aprobado!");
                                    notificacion(
                                            R.string.prestamo_aprobada_titulo,
                                            R.string.prestamo_encabezado_aprobado,
                                            APROBADO);
                                } else if (mEstatusSolicitud.equals("rechazado")) {
                                    Log.i(TAG, "¡Cambio a rechazado!");
                                    notificacion(
                                            R.string.prestamo_rechazada_titulo,
                                            R.string.prestamo_encabezado_rechazado,
                                            RECHAZADO);
                                } else if (mEstatusSolicitud.equals("entregado")) {
                                    Log.i(TAG, "¡Cambio a entregado!");
                                    mIDSolicitud = mSolicitudPrestamo.getId();
                                    mPrestamo = mPrestamoTable.searchPrestamo();
                                    if (mPrestamo != null) {  // Actualiza el prestamo
                                        Log.i(TAG, "¡Tengo un prestamo guardado internamente!");
                                        Log.i(TAG, "¡Actualiza estatus del prestamo!");
                                        mIDPrestamo = mPrestamo.getId();
                                        mPrestamoTable.updateData(mIDPrestamo, mEstatusSolicitud);
                                    } else { // El prestamo fue aprobado, lo guarda
                                        Log.i(TAG, "¡No tengo un prestamo interno, busco en el servidor!");
                                        mPrestamo = mJSONParser.servicePrestamo(mIDSolicitud);
                                        if (mPrestamo != null) {
                                            Log.i(TAG, "¡Nuevo prestamo, guardado!");
                                            mPrestamoTable.insertData(
                                                    mPrestamo.getFechaEmision(),
                                                    mPrestamo.getFechaVencimiento(),
                                                    mPrestamo.getEstatus(),
                                                    mPrestamo.getInstrumento().getTipoInstrumento().getDescripcion(),
                                                    mPrestamo.getInstrumento().getModelo().getDescripcion(),
                                                    mPrestamo.getInstrumento().getSerial(),
                                                    mPrestamo.getInstrumento().getModelo().getMarca().getDescripcion(),
                                                    mPrestamo.getId()
                                            );
                                        } else {
                                            Log.i(TAG, "¡Es raro, deberia de haber un prestamo!");
                                        }
                                    }
                                } else {
                                    Log.i(TAG, "¡No ha cambiado a entregado aún!");
                                }
                            }
                        } else {
                            Log.i(TAG, "¡Seria raro que tenga una solicitud interna y no en el servidor!");
                        }
                    } else {
                        Log.i(TAG, "¡La solicitud fue rechazada");
                        mPrestamo = null;
                    }
                } else {
                    Log.i(TAG, "¡La solicitud esta finalizada!");
                }
            } else {
                Log.i(TAG, "¡Buscando alguna solicitud por estudiante!");
                mSolicitudPrestamo = mJSONParser.serviceSolicitudPrestamoPorEstudiante(integers[0]);
                if (mSolicitudPrestamo != null) {
                    Log.i(TAG, "¡Al iniciar sesión de nuevo, tengo una solicitud en el servidor");
                    mEstatusSolicitud = mSolicitudPrestamo.getEstatus();
                    Log.i(TAG, mEstatusSolicitud);
                    if (!mEstatusSolicitud.equals("finalizado")) {
                        if (!mEstatusSolicitud.equals("rechazado")) {
                            Log.i(TAG, "¡La solicitud aún esta vigente!");
                            mSolicitudPrestamoTable.insertData(
                                    mSolicitudPrestamo.getId(),
                                    mSolicitudPrestamo.getTipoPrestamo().getDescripcion(),
                                    mSolicitudPrestamo.getEstatus(),
                                    mSolicitudPrestamo.getTipoInstrumento().getDescripcion(),
                                    mSolicitudPrestamo.getFechaEmision(),
                                    mSolicitudPrestamo.getFechaVencimiento());
                            mPrestamo = mJSONParser.servicePrestamo(mSolicitudPrestamo.getId());
                            if (mPrestamo != null) {
                                mPrestamoTable.insertData(
                                        mPrestamo.getFechaEmision(),
                                        mPrestamo.getFechaVencimiento(),
                                        mPrestamo.getEstatus(),
                                        mPrestamo.getInstrumento().getTipoInstrumento().getDescripcion(),
                                        mPrestamo.getInstrumento().getModelo().getDescripcion(),
                                        mPrestamo.getInstrumento().getSerial(),
                                        mPrestamo.getInstrumento().getModelo().getMarca().getDescripcion(),
                                        mPrestamo.getId()
                                );
                            } else {
                                Log.i(TAG, "¡No tengo un prestamo aprobado!");
                            }
                        } else {
                            Log.i(TAG, "¡La utlima solicitud fue rechazada!");
                            mPrestamo = null;
                            mSolicitudPrestamo = null;
                        }
                    } else {
                        Log.i(TAG, "¡La solicitud ya expiro!");
                        mSolicitudPrestamo = null;
                        mPrestamo = null;
                    }
                } else {
                    Log.i(TAG, "¡No tengo ninguna solicitud!");
                }
            }
            return null;
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
        mSolicitudPrestamo = mSolicitudPrestamoTable.searchSolicitudPrestamo();
        mListDrawer.setItemChecked(3, true);
        mListDrawer.setSelection(3);
    }
}