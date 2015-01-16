package edu.ucla.fusa.android.fragmentos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.ucla.fusa.android.DB.EstudianteTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.academico.Estudiante;
import edu.ucla.fusa.android.modelo.herramientas.FloatingHintEditText;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import edu.ucla.fusa.android.modelo.instrumentos.SolicitudPrestamo;
import edu.ucla.fusa.android.modelo.instrumentos.TipoInstrumento;
import edu.ucla.fusa.android.modelo.instrumentos.TipoPrestamo;

public class SolicitudPrestamoFragment extends Fragment implements View.OnClickListener, DatePickerDialogFragment.DatePickerDialogHandler, TextWatcher {

    private static String TAG = "DrawerSolicitudPrestamoFragment";
    private Calendar calendar;
    private int day;
    private FloatingHintEditText desde;
    private CircularProgressButton enviar;
    private FloatingHintEditText hasta;
    private int month;
    private View view;
    private int year;
    private FloatingHintEditText tipoInstrumento;
    private FloatingHintEditText tipoPrestamo;
    private List<TipoPrestamo> tipoPrestamos = new ArrayList<TipoPrestamo>();
    private List<TipoInstrumento> tipoInstrumentos = new ArrayList<TipoInstrumento>();
    private JSONParser jsonParser = new JSONParser();
    private PopupMenu menuTipoPrestamo;
    private PopupMenu menuTipoInstrumento;
    private ImageView addTipoPrestamo;
    private ImageView addTipoInstrumento;
    private ImageView addFechaEmision;
    private ImageView addFechaVencimiento;
    private TipoInstrumento ti;
    private TipoPrestamo tp;
    private SolicitudPrestamo solicitud;
    private Estudiante estudiante;
    private EstudianteTable bd;

    public static SolicitudPrestamoFragment newInstance() {
        SolicitudPrestamoFragment fragment = new SolicitudPrestamoFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getActivity().getActionBar().setIcon(R.drawable.ic_solicitud_prestamo_blanco);
        getActivity().getActionBar().setTitle(R.string.contenido_prestamo_instrumento_action_bar_titulo);
        new LoadingTipoInstrumento().execute();
        new LoadingTipoPrestamo().execute();
        bd = new EstudianteTable(getActivity());
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_drawer_solicitud_prestamo, paramViewGroup, false);
        enviar = ((CircularProgressButton) view.findViewById(R.id.btn_enviar_solicitud_prestamo));
        desde = (FloatingHintEditText) view.findViewById(R.id.et_desde);
        hasta = (FloatingHintEditText) view.findViewById(R.id.et_hasta);
        tipoInstrumento = (FloatingHintEditText) view.findViewById(R.id.et_tipo_instrumento_prestamo);
        tipoPrestamo = (FloatingHintEditText) view.findViewById(R.id.et_tipo_prestamo);
        addTipoPrestamo = (ImageView) view.findViewById(R.id.add_prestamo);
        addTipoInstrumento = (ImageView) view.findViewById(R.id.add_instrumento_prestamo);
        addFechaEmision = (ImageView) view.findViewById(R.id.add_fecha_emision_prestamo);
        addFechaVencimiento = (ImageView) view.findViewById(R.id.add_fecha_vencimiento_prestamo);

        addTipoPrestamo.setOnClickListener(this);
        addTipoInstrumento.setOnClickListener(this);
        enviar.setOnClickListener(this);
        addFechaEmision.setOnClickListener(this);
        addFechaVencimiento.setOnClickListener(this);
        calendar = Calendar.getInstance();
        day =  calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        tipoInstrumento.setEnabled(false);
        addTipoInstrumento.setEnabled(false);

        tipoPrestamo.setEnabled(false);
        addTipoPrestamo.setEnabled(false);
        return view;
    }

    private SolicitudPrestamo cargarDatosSolicitud() {
        solicitud = new SolicitudPrestamo();
        solicitud.setEstatus("activo");
        solicitud.setTipoInstrumento(ti);
        solicitud.setTipoPrestamo(tp);

        try {
            solicitud.setFechaEmision(new SimpleDateFormat("dd-MM-yyyy").parse(desde.getText().toString()));
            solicitud.setFechaVencimiento(new SimpleDateFormat("dd-MM-yyyy").parse(hasta.getText().toString()));
            estudiante = bd.searchUser();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        solicitud.setEstudiante(estudiante);
        Log.i(TAG, estudiante.getNombre());
        Log.i(TAG, estudiante.getApellido());
        Log.i(TAG, "ID: " + estudiante.getId());
        Log.i(TAG, estudiante.getNombre());
        Log.i(TAG, estudiante.getApellido());
        Log.i(TAG, "ID: " + estudiante.getId());
        Log.i(TAG, estudiante.getNombre());
        Log.i(TAG, estudiante.getApellido());
        Log.i(TAG, "ID: " + estudiante.getId());
        return solicitud;
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.add_fecha_emision_prestamo:
                new DatePickerBuilder()
                        .setReference(0)
                        .setFragmentManager(getChildFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                        .setTargetFragment(SolicitudPrestamoFragment.this)
                        .show();
                break;
            case R.id.add_fecha_vencimiento_prestamo:
                new DatePickerBuilder()
                        .setReference(1)
                        .setFragmentManager(getChildFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                        .setTargetFragment(SolicitudPrestamoFragment.this)
                        .show();
                break;
            case R.id.add_instrumento_prestamo:
                showMenuTipoInstrumento();
                break;
            case R.id.add_prestamo:
                showMenuTipoPrestamo();
                break;
            case R.id.btn_enviar_solicitud_prestamo:
                if (enviar.getProgress() != 100) {
                    if (!tipoInstrumento.getText().toString().equals("") || !tipoPrestamo.getText().toString().equals("")
                            || !desde.getText().toString().equals("") || !hasta.getText().toString().equals("")) {
                        new uploadSolicitudPrestamo().execute(cargarDatosSolicitud());
                    } else {
                        Toast.makeText(getActivity(), R.string.mensaje_complete_campos_aspirante, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.mensaje_error_enviar_solicitud, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onDialogDateSet(int reference, int year, int month, int day) {
        switch (reference){
            case 0:
                if (month < 9)
                    desde.setText(day + "-0" + (month + 1) + "-" + year);
                else
                    desde.setText(day + "-" + (month + 1) + "-" + year);
                break;
            case 1:
                if (month < 9)
                    hasta.setText(day + "-0" + (month + 1) + "-" + year);
                else
                    hasta.setText(day + "-" + (month + 1) + "-" + year);
                break;
        }
    }

    private void showMenuTipoPrestamo(){
        menuTipoPrestamo.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                tp = tipoPrestamos.get(menuItem.getOrder());
                Log.i(TAG, "Selecciono el tipo de prestamo " + tp.getDescripcion() + " de la posición " + menuItem.getOrder());
                tipoPrestamo.setText(menuItem.getTitle());
                return true;
            }

        });
        try {
            menuTipoPrestamo.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMenuTipoInstrumento(){
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
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private class LoadingTipoPrestamo extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            menuTipoPrestamo = new PopupMenu(getActivity(), addTipoPrestamo);
            menuTipoPrestamo.getMenu().clear();
            tipoPrestamos = jsonParser.serviceLoadingTipoPrestamo();
            if (tipoPrestamos == null) {
                return 0;
            } else if (tipoPrestamos.size() != 0) {
                for (int i = 0; i < tipoPrestamos.size(); i++) {
                    menuTipoPrestamo.getMenu().add(0, 0, i, tipoPrestamos.get(i).getDescripcion());
                }
                return 100;
            } else {
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == 0) {
                Toast.makeText(getActivity(), R.string.mensaje_error_cargar_tipo_prestamo, Toast.LENGTH_SHORT).show();
            } else if (result == 100) {
                tipoPrestamo.setEnabled(true);
                addTipoPrestamo.setEnabled(true);
            }
        }
    }

    private class LoadingTipoInstrumento extends AsyncTask<Void, Void, Integer> {


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
    }

    private class uploadSolicitudPrestamo extends AsyncTask<SolicitudPrestamo, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            enviar.setProgress(0);
            enviar.setIndeterminateProgressMode(true);
            enviar.setProgress(50);
        }

        @Override
        protected Integer doInBackground(SolicitudPrestamo... params) {

            try {
                return jsonParser.uploadSolicitudPrestamo(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            enviar.setProgress(result);
            if (result == 100) {
                //getFragmentManager().popBackStackImmediate();
            } else {
                enviar.setErrorText("Error, ¡reintentar!");
            }
        }
    }

}
