package edu.ucla.fusa.android.fragmentos;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juanlabrador.dateslider.SliderContainer;
import com.juanlabrador.grouplayout.GroupContainer;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import at.markushi.ui.CircleButton;
import edu.ucla.fusa.android.DB.EstudianteTable;
import edu.ucla.fusa.android.DB.SolicitudPrestamoTable;
import edu.ucla.fusa.android.DB.TipoInstrumentoTable;
import edu.ucla.fusa.android.DB.TipoPrestamoTable;
import edu.ucla.fusa.android.DB.UserTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.VistasPrincipalesActivity;
import edu.ucla.fusa.android.modelo.academico.Estudiante;
import edu.ucla.fusa.android.modelo.academico.EstudiantePorAgrupacion;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import edu.ucla.fusa.android.modelo.instrumentos.SolicitudPrestamo;
import edu.ucla.fusa.android.modelo.instrumentos.TipoInstrumento;
import edu.ucla.fusa.android.modelo.instrumentos.TipoPrestamo;
import edu.ucla.fusa.android.modelo.seguridad.Usuario;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class SolicitudPrestamoFragment extends Fragment implements SliderContainer.OnTimeChangeListener , Toolbar.OnMenuItemClickListener {

    private static String ESTATUS = "en proceso";
    private static String TAG = "DrawerSolicitudPrestamoFragment";
    private VistasPrincipalesActivity mActivity;
    private GroupContainer mGrupoPrestamo;
    private GroupContainer mGrupoFechaEmision;
    private GroupContainer mGrupoFechaVencimiento;
    private GroupContainer mGrupoInstrumentos;
    private JSONParser jsonParser = new JSONParser();
    private SolicitudPrestamo mSolicitudPrestamo;
    private Estudiante mEstudiante;
    private EstudianteTable mEstudianteTable;
    private Toolbar mToolbar;
    private ArrayList<TipoPrestamo> mTiposPrestamos;
    private ArrayList<String> mCustomMenuPrestamo = new ArrayList<>();
    private ArrayList<TipoInstrumento> mTiposInstrumentos;
    private ArrayList<String> mCustomMenuInstrumento = new ArrayList<>();
    private NotificationManager mManager;
    private SliderContainer mContainerFechaEmision;
    private SliderContainer mContainerFechaVencimiento;
    private Calendar mFechaInicial;
    private SimpleDateFormat mDateFormat;
    private JSONParser mJSONParser;
    private CircularProgressBar mProgress;
    private CircleButton mRetryButton;
    private TextView mEmpty;
    private LinearLayout mContenedor;
    private LoadingTipoPrestamo mServicePrestamo;
    private LoadingTipoInstrumentos mServiceInstrumento;
    private UploadSolicitudPrestamo mServiceUpload;
    private EstudiantePorAgrupacion mEstudiantePorAgrupacion;
    private BuscarEstudianteAgrupacion mServiceValidar;

    public static SolicitudPrestamoFragment newInstance() {
        SolicitudPrestamoFragment fragment = new SolicitudPrestamoFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.prestamo_titulo_barra);
        mToolbar.getMenu().clear();
        mToolbar.setOnMenuItemClickListener(this);
        
        mActivity = (VistasPrincipalesActivity) getActivity();
        mEstudianteTable = new EstudianteTable(getActivity());
        mJSONParser = new JSONParser();
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arguments) {
        super.onCreateView(inflater, container, arguments);
        return inflater.inflate(R.layout.fragment_drawer_solicitud_prestamo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContenedor = (LinearLayout) view.findViewById(R.id.contenedor_solicitud);
        mGrupoPrestamo = (GroupContainer) view.findViewById(R.id.tipos_prestamos);
        mGrupoInstrumentos = (GroupContainer) view.findViewById(R.id.instrumentos_prestamo);

        mGrupoFechaEmision = (GroupContainer) view.findViewById(R.id.fecha_emision_titulo);
        mGrupoFechaEmision.addTextLayout(R.string.prestamo_fecha_emision);

        mGrupoFechaVencimiento = (GroupContainer) view.findViewById(R.id.fecha_vencimiento_titulo);
        mGrupoFechaVencimiento.addTextLayout(R.string.prestamo_fecha_vencimiento);

        mContainerFechaEmision = (SliderContainer) view.findViewById(R.id.fecha_emision);
        mContainerFechaEmision.setOnTimeChangeListener(this);
        mFechaInicial = Calendar.getInstance();
        mContainerFechaEmision.setMinTime(mFechaInicial);
        mContainerFechaEmision.setTime(mFechaInicial);

        mContainerFechaVencimiento = (SliderContainer) view.findViewById(R.id.fecha_vencimiento);
        mContainerFechaVencimiento.setOnTimeChangeListener(this);
        mFechaInicial = Calendar.getInstance();
        mContainerFechaVencimiento.setMinTime(mFechaInicial);
        mContainerFechaVencimiento.setTime(mFechaInicial);

        mProgress = (CircularProgressBar) view.findViewById(R.id.pb_cargando_solicitud);
        mEmpty = (TextView) view.findViewById(R.id.solicitud_vacia);
        mRetryButton = (CircleButton) view.findViewById(R.id.button_network_solicitud);
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContenedor.setVisibility(View.GONE);
                mProgress.setVisibility(View.VISIBLE);
                mEmpty.setVisibility(View.GONE);
                mRetryButton.setVisibility(View.GONE);
                mServicePrestamo = new LoadingTipoPrestamo();
                mServicePrestamo.execute();
            }
        });
        mEstudiante = mEstudianteTable.searchUser();
        mServiceValidar = new BuscarEstudianteAgrupacion();
        mServiceValidar.execute(mEstudiante.getId());
    }

    @Override
    public void onResume() {
        super.onResume();
        mToolbar.setVisibility(View.VISIBLE);
    }

    private void armarSolicitud(Date mFechaEmision, Date mFechaVencimiento) {
        mSolicitudPrestamo = new SolicitudPrestamo();
        mSolicitudPrestamo.setEstatus(ESTATUS);
        mSolicitudPrestamo.setFechaEmision(mDateFormat.format(mFechaEmision));
        mSolicitudPrestamo.setFechaVencimiento(mDateFormat.format(mFechaVencimiento));
        mSolicitudPrestamo.setEstudiantePorAgrupacion(mEstudiantePorAgrupacion);
        mSolicitudPrestamo.setTipoPrestamo(mTiposPrestamos.get(mGrupoPrestamo.getPopupLayoutAt(0).getItemPosition()));
        mSolicitudPrestamo.setTipoInstrumento(mTiposInstrumentos.get(mGrupoInstrumentos.getPopupLayoutAt(0).getItemPosition()));
        mServiceUpload = new UploadSolicitudPrestamo();
        mServiceUpload.execute(mSolicitudPrestamo);
    }

    private void validarSolicitud() {

        Date mFechaEmision = mContainerFechaEmision.getTime().getTime();
        Date mFechaVencimiento = mContainerFechaVencimiento.getTime().getTime();
        long mTiempo = mFechaVencimiento.getTime() - mFechaEmision.getTime();
        long dias = mTiempo / (1000 * 60 *  60 * 24);
        Log.i(TAG, "Dias: " + dias);

        if (mGrupoPrestamo.getPopupLayoutAt(0).getItemPosition() == 0) { // Fin de semana
            if (1 < dias && dias <= 4) {
                if (mContainerFechaEmision.getTime().get(Calendar.DAY_OF_WEEK) == 6 && mContainerFechaVencimiento.getTime().get(Calendar.DAY_OF_WEEK) == 2) {
                    Log.i(TAG, "¡Valido el fin de semana!");
                    Log.i(TAG, new SimpleDateFormat("dd-MM-yyyy HH:mm").format(mFechaEmision));
                    Log.i(TAG, new SimpleDateFormat("dd-MM-yyyy HH:mm").format(mFechaVencimiento));
                    armarSolicitud(mFechaEmision, mFechaVencimiento);
                } else {
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_fin_semana));
                }
            } else {
                SnackbarManager.show(
                        Snackbar.with(getActivity())
                                .type(SnackbarType.MULTI_LINE)
                                .text(R.string.mensaje_error_periodo_prestamo));
            }
        } else if (mGrupoPrestamo.getPopupLayoutAt(0).getItemPosition() == 1) { // Vacacional
            if (dias > 3 && dias < 365) {
                armarSolicitud(mFechaEmision, mFechaVencimiento);
            } else {
                SnackbarManager.show(
                        Snackbar.with(getActivity())
                                .type(SnackbarType.MULTI_LINE)
                                .text(R.string.mensaje_error_periodo_prestamo));
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.action_enviar:
                if (!mGrupoPrestamo.getPopupLayoutAt(0).getContent().equals("") &&
                        !mGrupoInstrumentos.getPopupLayoutAt(0).getContent().equals("")) {
                    validarSolicitud();
                } else {
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_complete_campos));
                }
                break;
        }
        return false;
    }

    @Override
    public void onTimeChange(Calendar calendar) {
        Calendar mTodayDate = Calendar.getInstance();
        mTodayDate.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
        mTodayDate.set(Calendar.HOUR_OF_DAY, 23);
        mTodayDate.set(Calendar.MINUTE, 50);

        if (mContainerFechaEmision.getTime().equals(calendar)) {
            setFechaEmision();
            if (calendar.after(mTodayDate.getTime()) || calendar.equals(mTodayDate.getTime())) { // Fecha de emision mayor o igual al actual
                // La fecha de emision es mayor a la fecha de vencimiento
                if (calendar.after(mContainerFechaVencimiento.getTime())) {
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_fecha_prestamo));
                }
            }
        } else {
            setFechaVencimiento();
            if (calendar.after(mTodayDate.getTime()) || calendar.equals(mTodayDate.getTime())) { // Fecha de emision mayor o igual al actual
                // La fecha de vencimiento es menor a la fecha de emision
                if (calendar.before(mContainerFechaEmision.getTime())) {
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_fecha_prestamo));
                }
            }
        }
    }

    private class UploadSolicitudPrestamo extends AsyncTask<SolicitudPrestamo, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mToolbar.getMenu().findItem(R.id.action_enviar).getActionView() == null) {
                mToolbar.getMenu().findItem(R.id.action_enviar).setActionView(R.layout.custom_progress_bar);
            }
            sendNotificacion();
        }

        @Override
        protected Integer doInBackground(SolicitudPrestamo... params) {
            return jsonParser.uploadSolicitudPrestamo(params[0]);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 100:
                    Log.i(TAG, "¡Solicitud enviada!");
                    mToolbar.getMenu().clear();
                    mActivity.actualizarSolicitud();
                    mManager.cancel(1);
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.prestamo_solicitud_enviada));
                    getFragmentManager().popBackStack();
                    break;
                case 0:
                    Log.i(TAG, "¡Error al cargar la solicitud, datos malos!");
                    mToolbar.getMenu().findItem(R.id.action_enviar).setActionView(null);
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_excepcion));
                    mManager.cancel(1);
                    //new UploadSolicitudPrestamo().execute(mSolicitudPrestamo);
                    break;
                case -1:
                    Log.i(TAG, "¡Error con el servidor!");
                    mToolbar.getMenu().findItem(R.id.action_enviar).setActionView(null);
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
                .setContentTitle(getString(R.string.prestamo_enviar))
                .setTicker(getString(R.string.prestamo_enviar))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        mNotificacion.setAutoCancel(true);
        mManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mManager.notify(1, mNotificacion.build());

    }

    protected void setFechaEmision() {
        final Calendar c = mContainerFechaEmision.getTime();
        mGrupoFechaEmision.getTextLayoutAt(0).setContent(String.format("%td %tb %tY", c, c, c));
    }

    protected void setFechaVencimiento() {
        final Calendar c = mContainerFechaVencimiento.getTime();
        mGrupoFechaVencimiento.getTextLayoutAt(0).setContent(String.format("%td %tb %tY", c, c, c));
        
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mServiceInstrumento != null) {
            if (!mServiceInstrumento.isCancelled()) {
                mServiceInstrumento.cancel(true);
            }
        }
        if (mServicePrestamo != null) {
            if (!mServicePrestamo.isCancelled()) {
                mServicePrestamo.cancel(true);
            }
        }
        
        if (mServiceUpload != null) {
            if (!mServiceUpload.isCancelled()) {
                mServiceUpload.cancel(true);
            }
        }

        if (mServiceValidar != null) {
            if (!mServiceValidar.isCancelled()) {
                mServiceValidar.cancel(true);
            }
        }
    }

    private class BuscarEstudianteAgrupacion extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... integers) {
            mEstudiantePorAgrupacion = mJSONParser.serviceEstudiantePorAgrupacion(integers[0]);
            if (mEstudiantePorAgrupacion == null) {
                return 0;
            } else if (mEstudiantePorAgrupacion.getId() != -1) {
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
                    Log.i(TAG, "¡Esta en una agrupacion!");
                    mServicePrestamo = new LoadingTipoPrestamo();
                    mServicePrestamo.execute();
                    break;
                case 0:
                    Log.i(TAG, "¡Problemas de conexión!");
                    mContenedor.setVisibility(View.GONE);
                    mProgress.setVisibility(View.GONE);
                    mEmpty.setVisibility(View.VISIBLE);
                    mRetryButton.setVisibility(View.VISIBLE);
                    break;
                case -1:
                    Log.i(TAG, "¡No esta en una agrupacion!");
                    mToolbar.getMenu().clear();
                    mActivity.actualizarSolicitud();
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.prestamo_solicitud_error));
                    getFragmentManager().popBackStack();
                    break;
            }
        }
    }
    
    // Tipo de Prestamo 

    private class LoadingTipoPrestamo extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            mTiposPrestamos = mJSONParser.serviceLoadingTipoPrestamo();
            if (mTiposPrestamos == null) {
                return 0;
            } else if (mTiposPrestamos.size() != 0) {
                for (TipoPrestamo mTipoPrestamo : mTiposPrestamos) {
                    mCustomMenuPrestamo.add(mTipoPrestamo.getDescripcion());
                }
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
                    Log.i(TAG, "¡Tenemos prestamos!");
                    mGrupoPrestamo.addPopupLayout(R.string.prestamo_periodo, mCustomMenuPrestamo);
                    mServiceInstrumento = new LoadingTipoInstrumentos();
                    mServiceInstrumento.execute();
                    break;
                case 0:
                    Log.i(TAG, "¡Problemas de conexión!");
                    mContenedor.setVisibility(View.GONE);
                    mProgress.setVisibility(View.GONE);
                    mEmpty.setVisibility(View.VISIBLE);
                    mRetryButton.setVisibility(View.VISIBLE);
                    break;
                case -1:
                    Log.i(TAG, "¡No hay tipos de préstamo!");
                    mToolbar.getMenu().clear();
                    mActivity.actualizarSolicitud();
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.prestamo_solicitud_vacio));
                    getFragmentManager().popBackStack();
                    break;
            }
        }
    }

    // Tipo de Instrumento

    private class LoadingTipoInstrumentos extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            mTiposInstrumentos = mJSONParser.serviceLoadingTipoInstrumento();
            if (mTiposInstrumentos == null) {
                return 0;
            } else if (mTiposInstrumentos.size() != 0) {
                for (TipoInstrumento mTipoInstrumento : mTiposInstrumentos) {
                    mCustomMenuInstrumento.add(mTipoInstrumento.getDescripcion());
                }
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
                    Log.i(TAG, "¡Tenemos instrumentos!");
                    mGrupoInstrumentos.addPopupLayout(R.string.prestamo_tipo_instrumento, mCustomMenuInstrumento);
                    mProgress.setVisibility(View.GONE);
                    mEmpty.setVisibility(View.GONE);
                    mRetryButton.setVisibility(View.GONE);
                    mContenedor.setVisibility(View.VISIBLE);
                    mToolbar.inflateMenu(R.menu.action_enviar);
                    break;
                case 0:
                    Log.i(TAG, "¡Problemas de conexión!");
                    mContenedor.setVisibility(View.GONE);
                    mProgress.setVisibility(View.GONE);
                    mEmpty.setVisibility(View.VISIBLE);
                    mRetryButton.setVisibility(View.VISIBLE);
                    break;
                case -1:
                    Log.i(TAG, "¡No hay tipos de instrumentos!");
                    mToolbar.getMenu().clear();
                    mActivity.actualizarSolicitud();
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.prestamo_solicitud_vacio));
                    getFragmentManager().popBackStack();
                    break;
            }
        }
    }
}
