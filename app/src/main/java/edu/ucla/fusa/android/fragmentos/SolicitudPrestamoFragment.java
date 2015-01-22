package edu.ucla.fusa.android.fragmentos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import edu.ucla.fusa.android.DB.TipoPrestamoTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.academico.Estudiante;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import edu.ucla.fusa.android.modelo.instrumentos.SolicitudPrestamo;
import edu.ucla.fusa.android.modelo.instrumentos.TipoPrestamo;

public class SolicitudPrestamoFragment extends Fragment implements View.OnClickListener, DatePickerDialogFragment.DatePickerDialogHandler, Toolbar.OnMenuItemClickListener {

    private static String TAG = "DrawerSolicitudPrestamoFragment";
    private GroupLayout mGrupoPrestamo;
    private GroupLayout mGrupoFechas;
    private View mView;
    private JSONParser jsonParser = new JSONParser();
    private SolicitudPrestamo solicitud;
    private Estudiante mEstudiante;
    private EstudianteTable mEstudianteTable;
    private Toolbar mToolbar;
    private TipoPrestamoTable mTipoPrestamoTable;
    private ArrayList<TipoPrestamo> mTiposPrestamos;
    private ArrayList<String> mCustomMenu = new ArrayList<>();

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
        mToolbar.inflateMenu(R.menu.action_enviar);
        mToolbar.setOnMenuItemClickListener(this);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);

        mEstudianteTable = new EstudianteTable(getActivity());
        mTipoPrestamoTable = new TipoPrestamoTable(getActivity());

        mView = paramLayoutInflater.inflate(R.layout.fragment_drawer_solicitud_prestamo, paramViewGroup, false);

        mGrupoPrestamo = (GroupLayout) mView.findViewById(R.id.tipos_prestamos);
        mTiposPrestamos = mTipoPrestamoTable.searchTiposPrestamos();
        for (int i = 0; i < mTiposPrestamos.size(); i++) {
            mCustomMenu.add(mTiposPrestamos.get(i).getDescripcion());
        }
        mGrupoPrestamo.addPopupLayout(R.string.prestamo_periodo, mCustomMenu);

        mGrupoFechas = (GroupLayout) mView.findViewById(R.id.fechas_prestamo);
        mGrupoFechas.addOneButtonLayout(R.string.prestamo_fecha_emision, GroupLayout.ColorIcon.GRAY);
        mGrupoFechas.addOneButtonLayout(R.string.prestamo_fecha_vencimiento, GroupLayout.ColorIcon.GRAY);
        mGrupoFechas.getOneButtonLayoutAt(0).getButton().setOnClickListener(this);
        mGrupoFechas.getOneButtonLayoutAt(1).getButton().setOnClickListener(this);

        return mView;
    }

    private SolicitudPrestamo cargarDatosSolicitud() {

        try {
            Date mFechaEmison = new SimpleDateFormat("dd-MM-yyyy").parse(mGrupoFechas.getOneButtonLayoutAt(0).getContent());
            Date mFechaVencimiento = new SimpleDateFormat("dd-MM-yyyy").parse(mGrupoFechas.getOneButtonLayoutAt(1).getContent());
            mEstudiante = mEstudianteTable.searchUser();

            if (mGrupoPrestamo.getPopupLayoutAt(0).getmItemPosition() == 0) { // Fin de semana
                long mTiempo = mFechaVencimiento.getTime() - mFechaEmison.getTime();
                long dias = mTiempo / (1000 * 60 *  60 * 24);
                Log.i(TAG, "Dias: " + dias);
                if (1 < dias && dias <= 3) {
                   /* solicitud = new SolicitudPrestamo();
                    solicitud.setEstatus("activo");
                    solicitud.setFechaEmision(mFechaEmison);
                    solicitud.setFechaVencimiento(mFechaVencimiento);
                    solicitud.setEstudiante(mEstudiante);
                    solicitud.setTipoPrestamo(mTiposPrestamos.get(mGrupoPrestamo.getPopupLayoutAt(0).getmItemPosition()));
*/
                } else {
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_periodo_prestamo));
                }
            } else if (mGrupoPrestamo.getPopupLayoutAt(0).getmItemPosition() == 1) { // Vacacional
                long mTiempo = mFechaVencimiento.getTime() - mFechaEmison.getTime();
                long dias = mTiempo / (1000 * 60 *  60 * 24);
                long meses = mTiempo / (1000 * 60 * 60 * 24 * 30);
                Log.i(TAG, "Dias: " + dias);
                Log.i(TAG, "Meses: " + meses);
                if (dias > 3 && meses < 12) {
                    /*solicitud = new SolicitudPrestamo();
                    solicitud.setEstatus("activo");
                    solicitud.setFechaEmision(mFechaEmison);
                    solicitud.setFechaVencimiento(mFechaVencimiento);
                    solicitud.setEstudiante(mEstudiante);
                    solicitud.setTipoPrestamo(mTiposPrestamos.get(mGrupoPrestamo.getPopupLayoutAt(0).getmItemPosition()));
*/
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

        return solicitud;
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
            Date mFechaEscrita = new SimpleDateFormat("dd-MM-yyyy").parse(day + "-" + (month + 1) + "-" + year);
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

    /*private void showMenuTipoInstrumento(){
        menuTipoInstrumento = new PopupMenu(getActivity(), addTipoInstrumento);
        menuTipoInstrumento.getMenu().clear();
        for (int i = 0; i < tipoInstrumentos.size(); i++) {
            menuTipoInstrumento.getMenu().add(0, 0, i, tipoInstrumentos.get(i).getDescripcion());
        }
        menuTipoInstrumento.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                ti = tipoInstrumentos.get(menuItem.getOrder());
                Log.i(TAG, "Selecciono el instrumento " + ti.getDescripcion() + " de la posición " + menuItem.getOrder());
                tipoInstrumento.setText(menuItem.getTitle());
                return true;
            }
        });
        try {
            menuTipoInstrumento.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.action_enviar:
                if (!mGrupoPrestamo.getPopupLayoutAt(0).getContent().equals("") &&
                        !mGrupoFechas.getOneButtonLayoutAt(0).getContent().equals("")
                        && !mGrupoFechas.getOneButtonLayoutAt(1).getContent().equals("")) {
                    cargarDatosSolicitud();
                    //new uploadSolicitudPrestamo().execute(cargarDatosSolicitud());
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

    /*private class LoadingTipoInstrumento extends AsyncTask<Void, Void, Integer> {


        @Override
        protected Integer doInBackground(Void... voids) {
            tipoInstrumentos = jsonParser.serviceLoadingTipoInstrumento();
            if (tipoInstrumentos == null) {
                return 0;
            } else if (tipoInstrumentos.size() != 0) {
                return 100;
            } else {
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == 0) {
                Toast.makeText(getActivity(), R.string.mensaje_error_cargar_tipo_instrumento, Toast.LENGTH_SHORT).show();
            } else if (result == 100) {
                tipoInstrumento.setEnabled(true);
                addTipoInstrumento.setEnabled(true);
            }
        }
    }*/

    private class uploadSolicitudPrestamo extends AsyncTask<SolicitudPrestamo, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mToolbar.getMenu().findItem(R.id.action_enviar).setActionView(R.layout.custom_progress_bar);
        }

        @Override
        protected Integer doInBackground(SolicitudPrestamo... params) {
            int result;
            try {
                result = jsonParser.uploadSolicitudPrestamo(params[0]);
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
                    getFragmentManager().popBackStackImmediate();
                    break;
                case 0:
                    Log.i(TAG, "¡Error al cargar la solicitud, datos malos!");
                    mToolbar.getMenu().findItem(R.id.action_enviar).setActionView(null);
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_excepcion));
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
