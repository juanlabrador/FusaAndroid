package edu.ucla.fusa.android.fragmentos;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.HexagonImageView;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.juanlabrador.grouplayout.GroupContainer;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

import at.markushi.ui.CircleButton;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ListComentariosAdapter;
import edu.ucla.fusa.android.modelo.evento.CalificarEvento;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import me.drakeet.materialdialog.MaterialDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ComentariosEventoFragment extends ListFragment implements AbsListView.OnScrollListener, View.OnClickListener {

    private static final String TAG = "ComentariosEventoFragment";
    private ListComentariosAdapter mAdapter;
    private ArrayList<CalificarEvento> mCalificaciones;
    private RatingBar mPuntos;
    private TextView mNombre;
    private TextView mFechaPublicacion;
    private TextView mComentario;
    private ImageView mEditar;
    private Toolbar mToolbar;
    private DrawerArrowDrawable mDrawerArrow;
    private JSONParser mJSONParser;
    private CargarComentariosTask mServiceComentario;
    private CircularProgressBar mProgress;
    private CircleButton mRetryButton;
    private TextView mEmpty;
    private CalificarEvento mCalificacion;
    private SimpleDateFormat mDateFormat;
    private MaterialDialog mComentarioView;
    private Bitmap mBitmap;
    private RatingBar mNewPuntos;
    private GroupContainer mNewComentario;
    private HexagonImageView mFoto;
    private TextView mDescripcionCalificacion;
    private NotificationManager mManager;
    private UploadComentario mServiceUpload;

    public static ComentariosEventoFragment newInstance(Bundle arguments) {
        ComentariosEventoFragment fragment = new ComentariosEventoFragment();
        fragment.setRetainInstance(true);
        if (arguments != null) {
            fragment.setArguments(arguments);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_evento_comentarios, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNombre = (TextView) view.findViewById(R.id.tv_nombre_usuario_comentario);
        mFechaPublicacion = (TextView) view.findViewById(R.id.tv_fecha_publicacion_comentario);
        mEditar = (ImageView) view.findViewById(R.id.editar_comentario);
        mEditar.setOnClickListener(this);
        mPuntos = (RatingBar) view.findViewById(R.id.calificacion_evento_usuario);
        mComentario = (TextView) view.findViewById(R.id.comentario_evento_usuario);
        mProgress = (CircularProgressBar) view.findViewById(R.id.pb_cargando_comentarios);
        mRetryButton = (CircleButton) view.findViewById(R.id.button_network_comentario);
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress.setVisibility(View.VISIBLE);
                mEmpty.setVisibility(View.GONE);
                mRetryButton.setVisibility(View.GONE);
                getListView().setVisibility(View.GONE);
                mServiceComentario = new CargarComentariosTask();
                mServiceComentario.execute(mCalificacion.getEvento().getId(), mCalificacion.getId());
            }
        });
        mEmpty = (TextView) view.findViewById(R.id.comentarios_vacio);
        mDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (getArguments() != null) {
            mCalificacion = getArguments().getParcelable("comentario");
            mNombre.setText(mCalificacion.getUsuario().getNombre() + " " + mCalificacion.getUsuario().getApellido());
            mFechaPublicacion.setText(mDateFormat.format(new Date(mCalificacion.getFechaPublicacion())));
            mPuntos.setRating(mCalificacion.getPonderacion());
            mComentario.setText(mCalificacion.getComentario());
        }

        mJSONParser = new JSONParser();
        mServiceComentario = new CargarComentariosTask();
        mServiceComentario.execute(mCalificacion.getEvento().getId(), mCalificacion.getId());
    }

    public void onCreate(Bundle arguments) {
    super.onCreate(arguments);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.comentario_titulo_barra);

        mDrawerArrow = new DrawerArrowDrawable(getActivity()) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };

        mDrawerArrow.setProgress(1f);
        mToolbar.setNavigationIcon(mDrawerArrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mServiceComentario != null) {
            if (!mServiceComentario.isCancelled()) {
                mServiceComentario.cancel(true);
            }
        }

        if (mServiceUpload != null) {
            if (!mServiceUpload.isCancelled()) {
                mServiceUpload.cancel(true);
            }
        }
    }

    public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3) {}

    public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt) {
        
    }

    @Override
    public void onClick(View view) {
        armarLayoutComentario();
    }
    
    private Bitmap convertByteToImage(byte[] data) {
        if (data != null && data.length != 0) {
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        }
        return null;
    }

    private void armarLayoutComentario() {
        mComentarioView = new MaterialDialog(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_calificar_evento, null);

        mFoto = (HexagonImageView) view.findViewById(R.id.iv_foto_perfil_calificando);
        mFoto.setImageResource(R.drawable.no_avatar);
        mBitmap = convertByteToImage(mCalificacion.getUsuario().getFoto());
        if (mBitmap != null) {
            mFoto.setImageBitmap(mBitmap);
        }
        mNombre = (TextView) view.findViewById(R.id.tv_descripcion_usuario_calificacion);
        mNombre.setText(mCalificacion.getUsuario().getNombre() + " " + mCalificacion.getUsuario().getApellido());

        mNewComentario = (GroupContainer) view.findViewById(R.id.descripcion_comentario);
        mNewComentario.addMultiEditTextLayout(R.string.comentario_descripcion);
        mDescripcionCalificacion = (TextView) view.findViewById(R.id.tv_descripcion_calificacion);
        mNewPuntos = (RatingBar) view.findViewById(R.id.calificando_evento);
        mNewPuntos.setIsIndicator(false);
        mNewPuntos.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mNewPuntos.setRating(Math.round(v));
                mNewPuntos.setIsIndicator(false);
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

        mNewComentario.getMultiEditTextLayoutAt(0).setContent(mCalificacion.getComentario());
        mNewPuntos.setRating(mCalificacion.getPonderacion());

        mComentarioView.setView(view);
        mComentarioView.setPositiveButton("Enviar", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNewPuntos.getRating() != 0 && !mNewComentario.getMultiEditTextLayoutAt(0).getContent().equals("")) {
                    mCalificacion.setComentario(mNewComentario.getMultiEditTextLayoutAt(0).getContent());
                    mCalificacion.setFechaPublicacion(Calendar.getInstance().getTime().getTime());
                    mCalificacion.setPonderacion((int) mNewPuntos.getRating());
                    mServiceUpload = new UploadComentario();
                    mServiceUpload.execute(mCalificacion);
                }
            }
        });
        mComentarioView.show();

    }

    private class CargarComentariosTask extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Integer doInBackground(Integer... integers) {
            mCalificaciones = mJSONParser.serviceLoadingComentariosEvento(integers[0], integers[1]);
            if (mCalificaciones != null) {
                if (mCalificaciones.size() != 0) {
                    mAdapter = new ListComentariosAdapter(getActivity(), mCalificaciones, mCalificacion.getId());
                    return 100;
                } else {
                    return -1;
                }
            } else {
                return 0;
            }
        }

        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 100:
                    setListAdapter(mAdapter);
                    getListView().setVisibility(View.VISIBLE);
                    mProgress.setVisibility(View.GONE);
                    mEmpty.setVisibility(View.GONE);
                    mProgress.setVisibility(View.GONE);
                    break;
                case 0:
                    mEmpty.setText(R.string.mensaje_error_servidor);
                    mEmpty.setVisibility(View.VISIBLE);
                    mProgress.setVisibility(View.GONE);
                    mRetryButton.setVisibility(View.VISIBLE);
                    break;
                case -1:
                    mEmpty.setText(R.string.comentarios_vacio);
                    mEmpty.setVisibility(View.VISIBLE);
                    mProgress.setVisibility(View.GONE);
                    getListView().setVisibility(View.GONE);
                    break;
            }

        }

    }

    private class BuscarViejosComentarios extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void[] paramArrayOfVoid) {
            SystemClock.sleep(2000L);
            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            super.onPostExecute(paramVoid);
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
            mCalificacion = mJSONParser.uploadComentario(params[0]);
            if (mCalificacion != null) {
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
                    mComentario.setText(mCalificacion.getComentario());
                    mFechaPublicacion.setText(mDateFormat.format(new Date(mCalificacion.getFechaPublicacion())));
                    mPuntos.setRating(mCalificacion.getPonderacion());
                    mComentarioView.dismiss();
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