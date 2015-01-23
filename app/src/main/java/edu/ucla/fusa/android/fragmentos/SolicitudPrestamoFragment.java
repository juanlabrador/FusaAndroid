package edu.ucla.fusa.android.fragmentos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;
import com.juanlabrador.GroupLayout;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

import java.text.ParseException;
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

public class SolicitudPrestamoFragment extends Fragment implements View.OnClickListener, DatePickerDialogFragment.DatePickerDialogHandler, Toolbar.OnMenuItemClickListener {

    private static String ESTATUS = "en proceso";
    private static String TAG = "DrawerSolicitudPrestamoFragment";
    private static VistasPrincipalesActivity mActivity;
    private GroupLayout mGrupoPrestamo;
    private GroupLayout mGrupoFechas;
    private GroupLayout mGrupoInstrumentos;
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

        mGrupoPrestamo = (GroupLayout) mView.findViewById(R.id.tipos_prestamos);
        mTiposPrestamos = mTipoPrestamoTable.searchTiposPrestamos();
        for (int i = 0; i < mTiposPrestamos.size(); i++) {
            mCustomMenuPrestamo.add(mTiposPrestamos.get(i).getDescripcion());
        }
        mGrupoPrestamo.addPopupLayout(R.string.prestamo_periodo, mCustomMenuPrestamo);
        
        mGrupoInstrumentos = (GroupLayout) mView.findViewById(R.id.instrumentos_prestamo);
        mTiposInstrumentos = mTipoInstrumentoTable.searchTiposInstrumentos();
        for (int i = 0; i < mTiposInstrumentos.size(); i++) {
            mCustomMenuInstrumento.add(mTiposInstrumentos.get(i).getDescripcion());
        }
        mGrupoInstrumentos.addPopupLayout(R.string.prestamo_tipo_instrumento, mCustomMenuInstrumento);

        mGrupoFechas = (GroupLayout) mView.findViewById(R.id.fechas_prestamo);
        mGrupoFechas.addOneButtonLayout(R.string.prestamo_fecha_emision, GroupLayout.ColorIcon.GRAY);
        mGrupoFechas.addOneButtonLayout(R.string.prestamo_fecha_vencimiento, GroupLayout.ColorIcon.GRAY);
        mGrupoFechas.getOneButtonLayoutAt(0).getButton().setOnClickListener(this);
        mGrupoFechas.getOneButtonLayoutAt(1).getButton().setOnClickListener(this);

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

        try {
            Date mFechaEmision = new SimpleDateFormat("dd-MM-yyyy").parse(mGrupoFechas.getOneButtonLayoutAt(0).getContent());
            Date mFechaVencimiento = new SimpleDateFormat("dd-MM-yyyy").parse(mGrupoFechas.getOneButtonLayoutAt(1).getContent());
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
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view) {
        if (view == mGrupoFechas.getOneButtonLayoutAt(0).getButton()) {
            new DatePickerBuilder()
                    .setReference(0)
                    .setFragmentManager(getChildFragmentManager())
                    .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                    .setTargetFragment(SolicitudPrestamoFragment.this)
                    .show();
        } else if (view == mGrupoFechas.getOneButtonLayoutAt(1).getButton()) {
            new DatePickerBuilder()
                    .setReference(1)
                    .setFragmentManager(getChildFragmentManager())
                    .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                    .setTargetFragment(SolicitudPrestamoFragment.this)
                    .show();
        }
    }

    @Override
    public void onDialogDateSet(int reference, int year, int month, int day) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) -1);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 50);
            Date mFechaEscrita = new SimpleDateFormat("dd-MM-yyyy").parse(day + "-" + (month + 1) + "-" + year);
            Log.i(TAG, new SimpleDateFormat("dd-MM-yyyy HH:mm").format(calendar.getTime()));
            Log.i(TAG, new SimpleDateFormat("dd-MM-yyyy HH:mm").format(mFechaEscrita));
            switch (reference) {
                case 0: // Desde
                    if (mFechaEscrita.after(calendar.getTime()) || mFechaEscrita.equals(calendar.getTime())) { // Fecha de emision mayor o igual al actual
                        if (mGrupoFechas.getOneButtonLayoutAt(1).getContent().equals("")) { // Si la fecha de vencimiento no se ha escrito
                            if (month < 9)
                                mGrupoFechas.getOneButtonLayoutAt(0).setContent(day + "-0" + (month + 1) + "-" + year);
                            else
                                mGrupoFechas.getOneButtonLayoutAt(0).setContent(day + "-" + (month + 1) + "-" + year);
                        } else {
                            // La fecha de emision es menor a la fecha de vencimiento
                            if (mFechaEscrita.before(new SimpleDateFormat("dd-MM-yyyy").parse(mGrupoFechas.getOneButtonLayoutAt(0).getContent()))) {
                                if (month < 9)
                                    mGrupoFechas.getOneButtonLayoutAt(0).setContent(day + "-0" + (month + 1) + "-" + year);
                                else
                                    mGrupoFechas.getOneButtonLayoutAt(0).setContent(day + "-" + (month + 1) + "-" + year);
                            } else {
                                SnackbarManager.show(
                                        Snackbar.with(getActivity())
                                                .type(SnackbarType.MULTI_LINE)
                                                .text(R.string.mensaje_error_fecha_prestamo));
                            }
                        }
                    } else {
                        SnackbarManager.show(
                                Snackbar.with(getActivity())
                                        .type(SnackbarType.MULTI_LINE)
                                        .text(R.string.mensaje_error_fecha));
                    }
                    break;
                case 1:  //Hasta
                    if (mFechaEscrita.after(calendar.getTime()) || mFechaEscrita.equals(calendar.getTime())) { // Fecha de emision mayor o igual al actual
                        if (!mGrupoFechas.getOneButtonLayoutAt(0).getContent().equals("")) { // Si la fecha de emision se ha escrito
                            // La fecha de vencimiento es mayor a la fecha de emision
                            if (mFechaEscrita.after(new SimpleDateFormat("dd-MM-yyyy").parse(mGrupoFechas.getOneButtonLayoutAt(0).getContent()))) {
                                if (month < 9)
                                    mGrupoFechas.getOneButtonLayoutAt(1).setContent(day + "-0" + (month + 1) + "-" + year);
                                else
                                    mGrupoFechas.getOneButtonLayoutAt(1).setContent(day + "-" + (month + 1) + "-" + year);
                            } else {
                                SnackbarManager.show(
                                        Snackbar.with(getActivity())
                                                .type(SnackbarType.MULTI_LINE)
                                                .text(R.string.mensaje_error_fecha_prestamo));
                            }
                        } else {
                            if (month < 9)
                                mGrupoFechas.getOneButtonLayoutAt(1).setContent(day + "-0" + (month + 1) + "-" + year);
                            else
                                mGrupoFechas.getOneButtonLayoutAt(1).setContent(day + "-" + (month + 1) + "-" + year);
                        }
                    } else {
                        SnackbarManager.show(
                                Snackbar.with(getActivity())
                                        .type(SnackbarType.MULTI_LINE)
                                        .text(R.string.mensaje_error_fecha));
                    }
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            SnackbarManager.show(
                    Snackbar.with(getActivity())
                            .type(SnackbarType.MULTI_LINE)
                            .text(R.string.mensaje_error_excepcion));
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.action_enviar:
                if (!mGrupoPrestamo.getPopupLayoutAt(0).getContent().equals("") &&
                        !mGrupoInstrumentos.getPopupLayoutAt(0).getContent().equals("") &&
                        !mGrupoFechas.getOneButtonLayoutAt(0).getContent().equals("")
                        && !mGrupoFechas.getOneButtonLayoutAt(1).getContent().equals("")) {
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

    private class UploadSolicitudPrestamo extends AsyncTask<SolicitudPrestamo, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mToolbar.getMenu().findItem(R.id.action_enviar).getActionView() == null) {
                mToolbar.getMenu().findItem(R.id.action_enviar).setActionView(R.layout.custom_progress_bar);
            }
        }

        @Override
        protected Integer doInBackground(SolicitudPrestamo... params) {
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
}
