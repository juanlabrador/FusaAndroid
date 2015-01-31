package edu.ucla.fusa.android.fragmentos;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.juanlabrador.dateslider.SliderContainer;
import com.juanlabrador.grouplayout.GroupContainer;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.ucla.fusa.android.DB.EstudianteTable;
import edu.ucla.fusa.android.DB.SolicitudPrestamoTable;
import edu.ucla.fusa.android.DB.TipoInstrumentoTable;
import edu.ucla.fusa.android.DB.TipoPrestamoTable;
import edu.ucla.fusa.android.DB.UserTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.VistasPrincipalesActivity;
import edu.ucla.fusa.android.modelo.academico.Estudiante;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import edu.ucla.fusa.android.modelo.instrumentos.SolicitudPrestamo;
import edu.ucla.fusa.android.modelo.instrumentos.TipoInstrumento;
import edu.ucla.fusa.android.modelo.instrumentos.TipoPrestamo;
import edu.ucla.fusa.android.modelo.seguridad.Usuario;

public class SolicitudPrestamoFragment extends Fragment implements SliderContainer.OnTimeChangeListener , Toolbar.OnMenuItemClickListener {

    private static String ESTATUS = "en proceso";
    private static String TAG = "DrawerSolicitudPrestamoFragment";
    private static VistasPrincipalesActivity mActivity;
    private GroupContainer mGrupoPrestamo;
    private GroupContainer mGrupoFechaEmision;
    private GroupContainer mGrupoFechaVencimiento;
    private GroupContainer mGrupoInstrumentos;
    private View mView;
    private JSONParser jsonParser = new JSONParser();
    private SolicitudPrestamo mSolicitudPrestamo;
    private Usuario mUsuario;
    private UserTable mUserTable;
    private Estudiante mEstudiante;
    private EstudianteTable mEstudianteTable;
    private Toolbar mToolbar;
    private TipoPrestamoTable mTipoPrestamoTable;
    private ArrayList<TipoPrestamo> mTiposPrestamos;
    private ArrayList<String> mCustomMenuPrestamo = new ArrayList<>();
    private TipoInstrumentoTable mTipoInstrumentoTable;
    private ArrayList<TipoInstrumento> mTiposInstrumentos;
    private ArrayList<String> mCustomMenuInstrumento = new ArrayList<>();
    private SolicitudPrestamoTable mSolicitudPrestamoTable;
    private int contador;
    private NotificationManager mManager;
    private SliderContainer mContainerFechaEmision;
    private SliderContainer mContainerFechaVencimiento;
    private Calendar mFechaInicial;

    public static SolicitudPrestamoFragment newInstance(VistasPrincipalesActivity activity) {
        SolicitudPrestamoFragment fragment = new SolicitudPrestamoFragment();
        fragment.setRetainInstance(true);
        mActivity = activity;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.prestamo_titulo_barra);
        mToolbar.getMenu().clear();
        mToolbar.inflateMenu(R.menu.action_enviar);
        mToolbar.setOnMenuItemClickListener(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arguments) {
        super.onCreateView(inflater, container, arguments);

        mEstudianteTable = new EstudianteTable(getActivity());
        mTipoPrestamoTable = new TipoPrestamoTable(getActivity());
        mTipoInstrumentoTable = new TipoInstrumentoTable(getActivity());
        mUserTable = new UserTable(getActivity());
        mSolicitudPrestamoTable = new SolicitudPrestamoTable(getActivity());

        mView = inflater.inflate(R.layout.fragment_drawer_solicitud_prestamo, container, false);

        mGrupoPrestamo = (GroupContainer) mView.findViewById(R.id.tipos_prestamos);
        mTiposPrestamos = mTipoPrestamoTable.searchTiposPrestamos();
        for (int i = 0; i < mTiposPrestamos.size(); i++) {
            mCustomMenuPrestamo.add(mTiposPrestamos.get(i).getDescripcion());
        }
        mGrupoPrestamo.addPopupLayout(R.string.prestamo_periodo, mCustomMenuPrestamo);
        
        mGrupoInstrumentos = (GroupContainer) mView.findViewById(R.id.instrumentos_prestamo);
        mTiposInstrumentos = mTipoInstrumentoTable.searchTiposInstrumentos();
        for (int i = 0; i < mTiposInstrumentos.size(); i++) {
            mCustomMenuInstrumento.add(mTiposInstrumentos.get(i).getDescripcion());
        }
        mGrupoInstrumentos.addPopupLayout(R.string.prestamo_tipo_instrumento, mCustomMenuInstrumento);

        mGrupoFechaEmision = (GroupContainer) mView.findViewById(R.id.fecha_emision_titulo);
        mGrupoFechaEmision.addTextLayout(R.string.prestamo_fecha_emision);

        mGrupoFechaVencimiento = (GroupContainer) mView.findViewById(R.id.fecha_vencimiento_titulo);
        mGrupoFechaVencimiento.addTextLayout(R.string.prestamo_fecha_vencimiento);

        mContainerFechaEmision = (SliderContainer) mView.findViewById(R.id.fecha_emision);
        mContainerFechaEmision.setOnTimeChangeListener(this);
        mFechaInicial = Calendar.getInstance();
        mContainerFechaEmision.setMinTime(mFechaInicial);
        mContainerFechaEmision.setTime(mFechaInicial);

        mContainerFechaVencimiento = (SliderContainer) mView.findViewById(R.id.fecha_vencimiento);
        mContainerFechaVencimiento.setOnTimeChangeListener(this);
        mFechaInicial = Calendar.getInstance();
        mContainerFechaVencimiento.setMinTime(mFechaInicial);
        mContainerFechaVencimiento.setTime(mFechaInicial);
        return mView;
    }
    
    private void armarSolicitud(Date mFechaEmison, Date mFechaVencimiento) {
        mSolicitudPrestamo = new SolicitudPrestamo();
        mSolicitudPrestamo.setEstatus(ESTATUS);
        mSolicitudPrestamo.setFechaEmision(mFechaEmison);
        mSolicitudPrestamo.setFechaVencimiento(mFechaVencimiento);
        mSolicitudPrestamo.setEstudiante(mEstudiante);
        mSolicitudPrestamo.setTipoPrestamo(mTiposPrestamos.get(mGrupoPrestamo.getPopupLayoutAt(0).getItemPosition()));
        mSolicitudPrestamo.setTipoInstrumento(mTiposInstrumentos.get(mGrupoInstrumentos.getPopupLayoutAt(0).getItemPosition()));
        new UploadSolicitudPrestamo().execute(mSolicitudPrestamo);
    }

    private void validarSolicitud() {

        Date mFechaEmision = mContainerFechaEmision.getTime().getTime();
        Date mFechaVencimiento = mContainerFechaVencimiento.getTime().getTime();
        mEstudiante = mEstudianteTable.searchUser();
        mUsuario = mUserTable.searchUser();
        mEstudiante.setUsuario(mUsuario);
        long mTiempo = mFechaVencimiento.getTime() - mFechaEmision.getTime();
        long dias = mTiempo / (1000 * 60 *  60 * 24);
        Log.i(TAG, "Dias: " + dias);

        if (mGrupoPrestamo.getPopupLayoutAt(0).getItemPosition() == 0) { // Fin de semana
            if (1 < dias && dias <= 4) {
                Calendar mFE = Calendar.getInstance();
                mFE.setTime(mFechaEmision);
                Calendar mFF = Calendar.getInstance();
                mFF.setTime(mFechaVencimiento);
                Log.i(TAG, "Viernes es 6 : " + mFE.get(Calendar.DAY_OF_WEEK));
                Log.i(TAG, "Lunes es 2 : " + mFF.get(Calendar.DAY_OF_WEEK));
                if (mFE.get(Calendar.DAY_OF_WEEK) == 6 && mFF.get(Calendar.DAY_OF_WEEK) == 2) {
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
            SystemClock.sleep(3000);
            int result;
            try {
                mSolicitudPrestamo = jsonParser.uploadSolicitudPrestamo(params[0]);
                if (mSolicitudPrestamo != null) {
                    Log.i(TAG, new SimpleDateFormat("dd-MM-yyyy").format(mSolicitudPrestamo.getFechaEmision()));
                    mSolicitudPrestamoTable.insertData(
                            mSolicitudPrestamo.getId(),
                            mSolicitudPrestamo.getTipoPrestamo().getDescripcion(),
                            mSolicitudPrestamo.getEstatus(),
                            mSolicitudPrestamo.getTipoInstrumento().getDescripcion(),
                            mSolicitudPrestamo.getFechaEmision(),
                            mSolicitudPrestamo.getFechaVencimiento()
                    );
                    result = 100;
                } else {
                    result = -1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = 0;
            }
            return result;
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
                    /*mToolbar.getMenu().findItem(R.id.action_enviar).setActionView(null);
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_excepcion));*/
                    Log.i(TAG, "Cantidad de llamados: " + contador++);
                    new UploadSolicitudPrestamo().execute(mSolicitudPrestamo);
                    break;
                case -1:
                    Log.i(TAG, "¡Error con el servidor!");
                    mToolbar.getMenu().findItem(R.id.action_enviar).setActionView(null);
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_enviar));
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
}
