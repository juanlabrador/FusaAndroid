package edu.ucla.fusa.android.fragmentos;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import edu.ucla.fusa.android.DB.EstudianteTable;
import edu.ucla.fusa.android.DB.UserTable;
import edu.ucla.fusa.android.VistasPrincipalesActivity;
import edu.ucla.fusa.android.modelo.academico.Estudiante;
import edu.ucla.fusa.android.modelo.evento.CalificarEvento;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import edu.ucla.fusa.android.modelo.seguridad.Usuario;
import me.drakeet.materialdialog.MaterialDialog;

import com.github.siyamed.shapeimageview.HexagonImageView;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.juanlabrador.grouplayout.GroupContainer;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.evento.Evento;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class CalendarioFragment extends Fragment implements CalendarPickerView.OnDateSelectedListener, CalendarPickerView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {

    private static String TAG = "CalendarioFragment";
    private CalendarPickerView mCalendario;
    private Calendar mMitadAño;
    private Calendar mDiaActual;
    private ArrayList<Evento> mEventos;
    private ArrayList<Date> mFechas;
    private ArrayList<Evento> mNuevosEventos;
    private ArrayList<Integer> mIds;
    private SimpleDateFormat mDateFormat;
    private Toolbar mToolbar;
    private CircularProgressBar mLoading;
    private ArrayAdapter<String> mAdapter;
    private ListView mList;
    private LinearLayout mContenedorLeyenda;
    private int mLastScrollY;
    private int mPreviousFirstVisibleItem;
    private Animation mAnimation;
    private int mScrollThreshold;
    private AbsListView mScroll;
    private JSONParser mJSONParser;
    private SwipeRefreshLayout mSwipeRefresh;
    private LoadingEventos mServiceEventos;
    private UpdateEventos mServiceUpdate;
    private ArrayList<Evento> mEventosTodos; 
    
    // Ventana de datos del evento
    private MaterialDialog mCustomView;
    private GroupContainer mGrupoEvento;
    private GroupContainer mGrupoLugar;
    private GroupContainer mGrupoDescripcion;
    private GroupContainer mBotonComentario;
    private GroupContainer mVerTodosComentarios;
    private Evento mEvento;
    private SimpleDateFormat mTimeFormat;
    private TextView mTituloEvento;
    private Calendar mMesAnterior;
    private Date mDateEvento;

    // Calificar un comentario
    private MaterialDialog mComentarioView;
    private GroupContainer mComentarioEvento;
    private RatingBar mCalificacion;
    private TextView mDescripcionCalificacion;
    private UserTable mUserTable;
    private Usuario mUsuario;
    private HexagonImageView mFoto;
    private Bitmap mBitmap;
    private TextView mNombre;
    private Bundle mCache;
    private CalificarEvento mCalificacionEvento;
    private NotificationManager mManager;
    private UploadComentario mServiceComentario;
    private BuscarComentario mServiceBuscarComentario;
    private CircularProgressBar mProgressEvento;
    private ScrollView mContenedorEvento;
    private Date mHora;
    private DrawerArrowDrawable mDrawerArrow;
    private Estudiante mEstudiante;
    private EstudianteTable mEstudianteTable;
    private ArrayList<Date> mFechasAgrupacion;
    private ArrayList<Evento> mEventosAgrupacion;
    private ArrayList<Date> mFechasTodos;
    private VistasPrincipalesActivity mActivity;

    public static CalendarioFragment newInstance() {
        CalendarioFragment fragment = new CalendarioFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.calendario_titulo_barra);
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mTimeFormat = new SimpleDateFormat("hh:mm aa");
        mFechas = new ArrayList<>();
        mFechasAgrupacion = new ArrayList<>();
        mFechasTodos = new ArrayList<>();
        mUserTable = new UserTable(getActivity());
        mEstudianteTable = new EstudianteTable(getActivity());
        mJSONParser = new JSONParser();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arguments) {
        super.onCreateView(inflater, container, arguments);
        return inflater.inflate(R.layout.fragment_drawer_calendar, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mToolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.fragment_calendar);
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.azul));
        mCalendario = (CalendarPickerView) view.findViewById(R.id.calendario);
        mCalendario.setOnDateSelectedListener(this);
        mCalendario.setOnScrollListener(this);
        mLoading = (CircularProgressBar) view.findViewById(R.id.pb_cargando_calendario);
        mContenedorLeyenda = (LinearLayout) view.findViewById(R.id.contenedor_leyenda);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMitadAño = Calendar.getInstance();
        mMitadAño.add(Calendar.MONTH, 5);
        mDiaActual = Calendar.getInstance();
        mMesAnterior = Calendar.getInstance();
        mMesAnterior.set(Calendar.DAY_OF_MONTH, 1);
        mMesAnterior.set(Calendar.MONTH, mMesAnterior.get(Calendar.MONTH) - 1);
        mEventosTodos = new ArrayList<>();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCache = new Bundle();
        Log.i(TAG, "En Pause");
        mCache.putParcelableArrayList("eventos", mEventos);
        mCache.putParcelableArrayList("eventos_estudiante", mEventosAgrupacion);
    }

    @Override
    public void onStop() {
        super.onStop();
        mCache = new Bundle();
        mCache.putParcelableArrayList("eventos", mEventos);
        mCache.putParcelableArrayList("eventos_estudiante", mEventosAgrupacion);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCache = new Bundle();
        mCache.putParcelableArrayList("eventos", mEventos);
        mCache.putParcelableArrayList("eventos_estudiante", mEventosAgrupacion);
        if (mServiceBuscarComentario != null) {
            if (!mServiceBuscarComentario.isCancelled()) {
                mServiceBuscarComentario.cancel(true);
            }
        }
        if (mServiceComentario != null) {
            if (!mServiceComentario.isCancelled()) {
                mServiceComentario.cancel(true);
            }
        }
        if (mServiceEventos != null) {
            if (!mServiceEventos.isCancelled()) {
                mServiceEventos.cancel(true);
            }
        }
        if (mServiceUpdate != null) {
            if (!mServiceUpdate.isCancelled()) {
                mServiceUpdate.cancel(true);
            }
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        
        mActivity = (VistasPrincipalesActivity) getActivity();
        mToolbar.setTitle(R.string.calendario_titulo_barra);
        mDrawerArrow = new DrawerArrowDrawable(getActivity()) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };
        
        mToolbar.setNavigationIcon(mDrawerArrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActivity.mNavigationDrawer.isDrawerOpen(mActivity.mListDrawer)) {
                    mActivity.mNavigationDrawer.closeDrawer(mActivity.mListDrawer);
                } else {
                    mActivity.mNavigationDrawer.openDrawer(mActivity.mListDrawer);
                }
            }
        });
        
        if (mCache != null) {
            mFechas.clear();
            mFechasTodos.clear();
            mEventosTodos.clear();
            mFechasAgrupacion.clear();
            mEventos = mCache.getParcelableArrayList("eventos");
            mEventosAgrupacion = mCache.getParcelableArrayList("eventos_estudiante");
            for(Evento mEvento : mEventos) {
                try {
                    mFechas.add(mDateFormat.parse(mEvento.getFecha()));
                    mFechasTodos.add(mDateFormat.parse(mEvento.getFecha()));
                    mEventosTodos.add(mEvento);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            for(Evento mEvento : mEventosAgrupacion) {
                try {
                    mFechasAgrupacion.add(mDateFormat.parse(mEvento.getFecha()));
                    mFechasTodos.add(mDateFormat.parse(mEvento.getFecha()));
                    mEventosTodos.add(mEvento);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Log.i(TAG, "Tamaño Lista Evento: " + mEventos.size());
            Log.i(TAG, "Tamaño Lista Evento Agrupacion: " + mEventosAgrupacion.size());
            Log.i(TAG, "Tamaño Lista Evento Todos: " + mEventosTodos.size());
            mLoading.setVisibility(View.GONE);
            mCalendario.setVisibility(View.VISIBLE);
            mCalendario.init(mMesAnterior.getTime(), mMitadAño.getTime())
                    .inMode(CalendarPickerView.SelectionMode.SINGLE)
                    .withHighlightedDates(mFechas)
                    .withHighlightedOthersDates(mFechasAgrupacion);
            Log.i(TAG, "¡Restaurando eventos!");
        } else {
            Log.i(TAG, "¡No hay eventos por restaurar!");
            mServiceEventos = new LoadingEventos();
            mServiceEventos.execute();
        }
    }

    @Override
    public void onDateSelected(Date date) {
        String mFechaCalendario = mDateFormat.format(date);
        String mFechaEvento;
        mUsuario = mUserTable.searchUser();
        mIds = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        final MaterialDialog mDialog = new MaterialDialog(getActivity());
        Log.i(TAG, mDateFormat.format(date));
        for (int i = 0; i < mFechasTodos.size(); i++) {
            mFechaEvento = mDateFormat.format(mFechasTodos.get(i));
            if (mFechaEvento.equals(mFechaCalendario)) {
                mAdapter.add(mEventosTodos.get(i).getNombre());
                mIds.add(mEventosTodos.get(i).getId());
            }
        }
        
        if (mAdapter.getCount() > 1) {
            Log.i(TAG, "¡Hay más de un evento!");
            mList = new ListView(getActivity());
            mList.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            float scale = getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (8 * scale + 0.5f);
            mList.setPadding(0, dpAsPixels, 0, dpAsPixels);
            mList.setDividerHeight(0);
            mList.setAdapter(mAdapter);
            mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    armarCustomLayout(mIds.get(i));
                    mDialog.dismiss();
                    Log.i(TAG, "¡Selecciono el evento " + i + " de la lista");
                }
            });
            
            mDialog.setTitle("Seleccione un evento")
                   .setContentView(mList)
                    .show();

        } else if (mAdapter.getCount() == 1){
            Log.i(TAG, "¡Hay solo un evento!");
            armarCustomLayout(mIds.get(0));
         
        } else {
            Log.i(TAG, "¡No hay eventos en ese dia!");
        }
    }

    private void armarCustomLayout(int id) {
        Log.i(TAG, "ID evento: " + id);
        mCustomView = new MaterialDialog(getActivity());
        for (Evento e : mEventosTodos) {  // Buscamos el evento dentro de la Lista.
            if (e.getId() == id) {
                mEvento = e;
                break;
            }
        }
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_evento, null);
            
        mContenedorEvento = (ScrollView) view.findViewById(R.id.contenedor_evento);
        mTituloEvento = (TextView) view.findViewById(R.id.titulo_evento);
        mTituloEvento.setText(mEvento.getNombre().toUpperCase());
        mGrupoEvento = (GroupContainer) view.findViewById(R.id.grupo_datos_evento);
        mGrupoEvento.addTextLayout(R.string.evento_fecha, mEvento.getFecha());

        mHora = new Date(mEvento.getHora());
        mGrupoEvento.addTextLayout(R.string.evento_hora, mTimeFormat.format(mHora));

        mGrupoDescripcion = (GroupContainer) view.findViewById(R.id.grupo_descripcion_evento);
        mGrupoDescripcion.addSimpleMultiTextLayout(mEvento.getDescripcion());

        mGrupoLugar = (GroupContainer) view.findViewById(R.id.grupo_lugar_evento);
        mGrupoLugar.addSimpleMultiTextLayout(mEvento.getLugar().getDescripcion());
        mGrupoLugar.addSimpleMultiTextLayout(mEvento.getLugar().getDireccion());

        mBotonComentario = (GroupContainer) view.findViewById(R.id.boton_calificar_evento);
        mVerTodosComentarios = (GroupContainer) view.findViewById(R.id.ver_todos_comentarios);
        mProgressEvento = (CircularProgressBar) view.findViewById(R.id.pb_cargando_evento);
        try {
            mDateEvento = mDateFormat.parse(mEvento.getFecha());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(mDateEvento.before(mDiaActual.getTime())) {
            //Validar si posee un comentario
            Log.i(TAG, "¡Ya expiro el evento!");
            mServiceBuscarComentario = new BuscarComentario();
            Log.i(TAG, "Evento a validar " + mEvento.getId());
            Log.i(TAG, "Usuario " + mUsuario.getUsername());
            mServiceBuscarComentario.execute(String.valueOf(mEvento.getId()) , mUsuario.getUsername());
            mVerTodosComentarios.addSimpleMultiTextLayout(R.string.calificacion_comentarios);
            mVerTodosComentarios.getSimpleMultiTextLayoutAt(0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCalificacionEvento != null) {
                        Bundle arguments = new Bundle();
                        arguments.putParcelable("comentario", mCalificacionEvento);
                        getFragmentManager()
                                .beginTransaction()
                                .addToBackStack(null)
                                .replace(R.id.frame_container, ComentariosEventoFragment.newInstance(arguments))
                                .commit();
                        mCustomView.dismiss();
                    }
                }
            });
            
            mBotonComentario.addSimpleMultiTextLayout(R.string.evento_boton_calificar_evento);
            mBotonComentario.getSimpleMultiTextLayoutAt(0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    armarLayoutComentario();
                }
            });
        } else {
            Log.i(TAG, "¡Aun esta vigente el evento!");
            mContenedorEvento.setVisibility(View.VISIBLE);
            mProgressEvento.setVisibility(View.GONE);
        }

        mCustomView.setView(view);
        mCustomView.setBackgroundResource(R.color.gris_fondo);
        mCustomView.setCanceledOnTouchOutside(true);
        mCustomView.show();
        
    }

    private void armarLayoutComentario() {
        mComentarioView = new MaterialDialog(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_calificar_evento, null);

        mFoto = (HexagonImageView) view.findViewById(R.id.iv_foto_perfil_calificando);
        mFoto.setImageResource(R.drawable.no_avatar);
        mBitmap = convertByteToImage(mUsuario.getFoto());
        if (mBitmap != null) {
            mFoto.setImageBitmap(mBitmap);
        }
        mNombre = (TextView) view.findViewById(R.id.tv_descripcion_usuario_calificacion);
        mNombre.setText(mUsuario.getNombre() + " " + mUsuario.getApellido());
        
        mComentarioEvento = (GroupContainer) view.findViewById(R.id.descripcion_comentario);
        mComentarioEvento.addMultiEditTextLayout(R.string.comentario_descripcion);
        mDescripcionCalificacion = (TextView) view.findViewById(R.id.tv_descripcion_calificacion);
        mCalificacion = (RatingBar) view.findViewById(R.id.calificando_evento);
        mCalificacion.setIsIndicator(false);
        mCalificacion.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mCalificacion.setRating(Math.round(v));
                mCalificacion.setIsIndicator(false);
                switch (Math.round(v)) {
                    case 1:
                        mDescripcionCalificacion.setText(getResources().getString(R.string.nivel_calificacion_evento_1));
                        break;
                    case 2:
                        mDescripcionCalificacion.setText(getResources().getString(R.string.nivel_calificacion_evento_2));
                        break;
                    case 3:
                        mDescripcionCalificacion.setText(getResources().getString(R.string.nivel_calificacion_evento_3));
                        break;
                    case 4:
                        mDescripcionCalificacion.setText(getResources().getString(R.string.nivel_calificacion_evento_4));
                        break;
                    case 5:
                        mDescripcionCalificacion.setText(getResources().getString(R.string.nivel_calificacion_evento_5));
                        break;
                }
            }
        });

        mComentarioView.setView(view);
        mComentarioView.setPositiveButton("Enviar", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCalificacion.getRating() != 0 && !mComentarioEvento.getMultiEditTextLayoutAt(0).getContent().equals("")) {
                    mCalificacionEvento = new CalificarEvento();
                    mCalificacionEvento.setUsuario(mUsuario);
                    mCalificacionEvento.setEvento(mEvento);
                    mCalificacionEvento.setComentario(mComentarioEvento.getMultiEditTextLayoutAt(0).getContent());
                    mCalificacionEvento.setFechaPublicacion(Calendar.getInstance().getTime().getTime());
                    mCalificacionEvento.setPonderacion((int) mCalificacion.getRating());
                    mCalificacionEvento.setEstatus("activo");
                    mCalificacionEvento.setCorreo(mUsuario.getCorreo());
                    mServiceComentario = new UploadComentario();
                    mServiceComentario.execute(mCalificacionEvento);
                }
            }
        });
        mComentarioView.show();

    }

    private Bitmap convertByteToImage(byte[] data) {
        if (data != null && data.length != 0) {
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        }
        return null;
    }

    @Override
    public void onDateUnselected(Date date) {

    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        
        // Validar que el refresh ocurra cuando este en el tope de la lista
        int topRowVerticalPosition =
                (mCalendario == null || mCalendario.getChildCount() == 0) ?
                        0 : mCalendario.getChildAt(0).getTop();
        mSwipeRefresh.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
        if(totalItemCount == 0) return;
        if (isSameRow(firstVisibleItem)) {
            int newScrollY = getTopItemScrollY();
            boolean isSignificantDelta = Math.abs(mLastScrollY - newScrollY) > mScrollThreshold;
            if (isSignificantDelta) {
                if (mLastScrollY > newScrollY) {
                    Log.i(TAG, "Scroll Up");
                    startAnimationUp();
                } else {
                    startAnimationDown();
                    Log.i(TAG, "Scroll Down");
                }
            }
            mLastScrollY = newScrollY;
        } else {
            if (firstVisibleItem > mPreviousFirstVisibleItem) {
                Log.i(TAG, "Scroll Up");
                startAnimationUp();
            } else {
                Log.i(TAG, "Scroll Down");
                startAnimationDown();
            }

            mLastScrollY = getTopItemScrollY();
            mPreviousFirstVisibleItem = firstVisibleItem;
        }
    }

    private boolean isSameRow(int firstVisibleItem) {
        return firstVisibleItem == mPreviousFirstVisibleItem;
    }

    private int getTopItemScrollY() {
        if (mScroll == null || mScroll.getChildAt(0) == null) return 0;
        View topChild = mScroll.getChildAt(0);
        return topChild.getTop();
    }

    private void startAnimationDown() {
        if (!mContenedorLeyenda.isShown()) {
            mContenedorLeyenda = (LinearLayout) getActivity().findViewById(R.id.contenedor_leyenda);
            mAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_down);
            mAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mContenedorLeyenda.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mContenedorLeyenda.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mContenedorLeyenda.startAnimation(mAnimation);
        }
    }

    private void startAnimationUp() {
        if (mContenedorLeyenda.isShown()) {
            mContenedorLeyenda = (LinearLayout) getActivity().findViewById(R.id.contenedor_leyenda);
            mAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_up);
            mAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mContenedorLeyenda.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mContenedorLeyenda.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mContenedorLeyenda.startAnimation(mAnimation);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mServiceEventos != null) {
            if (!mServiceEventos.isCancelled()) {
                mServiceEventos.cancel(true);
            }
        }
        if (mServiceUpdate != null) {
            if (!mServiceUpdate.isCancelled()) {
                mServiceUpdate.cancel(true);
            }
        }

        if (mServiceComentario != null) {
            if (!mServiceComentario.isCancelled()) {
                mServiceComentario.cancel(true);
            }
        }

        if (mServiceBuscarComentario != null) {
            if (!mServiceBuscarComentario.isCancelled()) {
                mServiceBuscarComentario.cancel(true);
            }
        }
    }

    @Override
    public void onRefresh() {
        if (mEventosTodos != null) {
            if (mEventosTodos.size() != 0) {
                mServiceUpdate = new UpdateEventos();
                mServiceUpdate.execute();
            } else {
                mServiceEventos = new LoadingEventos();
                mServiceEventos.execute();
            }
        } else {
            mServiceEventos = new LoadingEventos();
            mServiceEventos.execute();
        }
    }

    private class LoadingEventos extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mEstudiante = mEstudianteTable.searchUser();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            mEventos = mJSONParser.serviceNoEventosEstudiante(mEstudiante.getId());
            if (mEventos != null) {
                Log.i(TAG, "¡Hay eventos!");
                for(Evento mEvento : mEventos) {
                    mEventosTodos.add(mEvento);
                    try {
                        mFechas.add(mDateFormat.parse(mEvento.getFecha()));
                        mFechasTodos.add(mDateFormat.parse(mEvento.getFecha()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                mEventosAgrupacion = mJSONParser.serviceEventosEstudiante(mEstudiante.getId());
                if (mEventosAgrupacion != null) {
                    if (mEventosAgrupacion.size() != 0) {
                        Log.i(TAG, "¡Hay eventos personales!");
                        for(Evento mEvento : mEventosAgrupacion) {
                            mEventosTodos.add(mEvento);
                            try {
                                mFechasAgrupacion.add(mDateFormat.parse(mEvento.getFecha()));
                                mFechasTodos.add(mDateFormat.parse(mEvento.getFecha()));  
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        return 200;
                    } else {
                        return 100;
                    }
                } else {
                    return 100;
                }
            } else {
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mSwipeRefresh.setRefreshing(false);
            Log.i(TAG, "Tamaño Lista Evento: " + mEventos.size());
            Log.i(TAG, "Tamaño Lista Evento Agrupacion: " + mEventosAgrupacion.size());
            Log.i(TAG, "Tamaño Lista Evento Todos: " + mEventosTodos.size());
            switch (result) {
                case 200:
                    Log.i(TAG, "¡Hay eventos más del estudiante!");
                    mCalendario.init(mMesAnterior.getTime(), mMitadAño.getTime())
                            .inMode(CalendarPickerView.SelectionMode.SINGLE)
                            .withHighlightedDates(mFechas)
                            .withHighlightedOthersDates(mFechasAgrupacion);
                    break;
                case 100:
                    Log.i(TAG, "¡Hay eventos!");
                    mCalendario.init(mMesAnterior.getTime(), mMitadAño.getTime())
                            .inMode(CalendarPickerView.SelectionMode.SINGLE)
                            .withHighlightedDates(mFechas);
                    break;
                case -1:
                    Log.i(TAG, "No hay eventos!");
                    mCalendario.init(mMesAnterior.getTime(), mMitadAño.getTime())
                            .withSelectedDate(mDiaActual.getTime());
                    break;
            }
            mLoading.setVisibility(View.GONE);
            mCalendario.setVisibility(View.VISIBLE);
            mContenedorLeyenda.setVisibility(View.VISIBLE);
        }
    }

    private class UpdateEventos extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... integers) {
            mEventos = mJSONParser.serviceNoEventosEstudianteUpdate(mEstudiante.getId(), mEventos.size() -1);
            if (mEventos != null) {
                Log.i(TAG, "¡Hay eventos!");
                for(Evento mEvento : mEventos) {
                    try {
                        mFechas.add(mDateFormat.parse(mEvento.getFecha()));
                        mEventosTodos.add(mEvento);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                mEventosAgrupacion = mJSONParser.serviceEventosEstudianteUpdate(mEstudiante.getId(), mEventosAgrupacion.size() -1);
                if (mEventosAgrupacion != null) {
                    if (mEventosAgrupacion.size() != 0) {
                        Log.i(TAG, "¡Hay eventos personales!");
                        for(Evento mEvento : mEventosAgrupacion) {
                            try {
                                mFechasAgrupacion.add(mDateFormat.parse(mEvento.getFecha()));
                                mEventosTodos.add(mEvento);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        return 200;
                    } else {
                        return 100;
                    }
                } else {
                    return 100;
                }
            } else {
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mSwipeRefresh.setRefreshing(false);
            switch (result) {
                case 200:
                    Log.i(TAG, "¡Hay eventos más del estudiante nuevos!");
                    mCalendario.init(mMesAnterior.getTime(), mMitadAño.getTime())
                            .inMode(CalendarPickerView.SelectionMode.SINGLE)
                            .withHighlightedDates(mFechas)
                            .withHighlightedOthersDates(mFechasAgrupacion);
                    break;
                case 100:
                    Log.i(TAG, "¡Hay eventos nuevos!");
                    mCalendario.init(mMesAnterior.getTime(), mMitadAño.getTime())
                            .inMode(CalendarPickerView.SelectionMode.SINGLE)
                            .withHighlightedDates(mFechas);
                    break;
                case -1:
                    Log.i(TAG, "No hay eventos nuevos!");
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .text(R.string.calendario_sin_nuevos_eventos));
                    break;
            }
            mLoading.setVisibility(View.GONE);
            mCalendario.setVisibility(View.VISIBLE);
            mContenedorLeyenda.setVisibility(View.VISIBLE);
        }
    }

    private class UploadComentario extends AsyncTask<CalificarEvento, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sendNotificacion();
        }

        @Override
        protected Integer doInBackground(CalificarEvento... params) {
            mCalificacionEvento = mJSONParser.uploadComentario(params[0]);
            if (mCalificacionEvento != null) {
                return 100;
            } else {
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 100:
                    Log.i(TAG, "¡Solicitud enviada!");
                    mManager.cancel(1);
                    mComentarioView.dismiss();
                    mBotonComentario.setVisibility(View.GONE);
                    mVerTodosComentarios.setVisibility(View.VISIBLE);
                    break;
                case 0:
                    Log.i(TAG, "¡Error al cargar el comentario, datos malos!");
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_excepcion));
                    mManager.cancel(1);
                    //new UploadSolicitudPrestamo().execute(mSolicitudPrestamo);
                    break;
                case -1:
                    Log.i(TAG, "¡Error con el servidor!");
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_enviar));
                    mManager.cancel(1);
                    break;
            }
        }
    }

    private class BuscarComentario extends AsyncTask<String, Void, Integer> {


        @Override
        protected Integer doInBackground(String... params) {
            mCalificacionEvento = mJSONParser.serviceValidarComentario(params[0], params[1]);
            if (mCalificacionEvento != null) {
                if (mCalificacionEvento.getId() != -1) {
                    return 100;
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
                    Log.i(TAG, "¡Tiene un comentario!");
                    mVerTodosComentarios.setVisibility(View.VISIBLE);
                    mContenedorEvento.setVisibility(View.VISIBLE);
                    mProgressEvento.setVisibility(View.GONE);
                    break;
                case 0:
                    Log.i(TAG, "¡Error al cargar el comentario, datos malos!");
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_excepcion));
                    mCustomView.dismiss();
                    break;
                case -1:
                    Log.i(TAG, "¡No tiene un comentario!");

                    mBotonComentario.setVisibility(View.VISIBLE);
                    mContenedorEvento.setVisibility(View.VISIBLE);
                    mProgressEvento.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private void sendNotificacion() {

        NotificationCompat.Builder mNotificacion = new NotificationCompat.Builder(getActivity())
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.calificacion_enviar))
                .setTicker(getString(R.string.calificacion_enviar))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        mNotificacion.setAutoCancel(true);
        mManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mManager.notify(1, mNotificacion.build());

    }
}