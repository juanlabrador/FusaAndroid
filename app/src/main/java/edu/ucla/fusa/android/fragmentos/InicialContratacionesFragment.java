package edu.ucla.fusa.android.fragmentos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.VistasInicialesActivity;
import edu.ucla.fusa.android.validadores.ValidadorEmails;

/**
 * Created by juanlabrador on 18/10/14.
 *
 * Clase que se encarga de admnistrar los datos ingresados para una solicitud de contratacion
 * de un evento.
 *
 */
public class InicialContratacionesFragment extends Fragment implements View.OnClickListener, View.OnKeyListener {

    private View view;
    private Button enviarContratacion;
    private EditText rifoCedula;
    private EditText solicitante;
    private EditText email;
    private EditText tema;
    private EditText lugar;
    private EditText fecha;
    private EditText hora;
    private Calendar calendar;
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;



    public static InicialContratacionesFragment newInstance() {
        InicialContratacionesFragment activity = new InicialContratacionesFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public InicialContratacionesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_inicial_contrataciones, container, false);

        enviarContratacion = (Button) view.findViewById(R.id.btnEnviarSolicitud);
        enviarContratacion.setOnClickListener(this);

        rifoCedula = (EditText) view.findViewById(R.id.etRifoCedulaContrataciones);
        solicitante = (EditText) view.findViewById(R.id.etSolicitanteContrataciones);
        email = (EditText) view.findViewById(R.id.etCorreoContrataciones);
        tema = (EditText) view.findViewById(R.id.etTemaEventoContrataciones);
        lugar = (EditText) view.findViewById(R.id.etLugarEventoContrataciones);
        fecha = (EditText) view.findViewById(R.id.etFechaEventoContrataciones);
        hora = (EditText) view.findViewById(R.id.etHoraEventoContrataciones);

        fecha.setOnClickListener(this);
        hora.setOnClickListener(this);

        calendar = Calendar.getInstance();
        /** Inicializamos las variales de fecha y hora */
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEnviarSolicitud:
                boolean validar = ValidadorEmails.validarEmail(email.getText().toString());
                if (validar != true) {
                    Toast.makeText(getActivity(), R.string.mensaje_correo_invalido, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.etFechaEventoContrataciones:
                mostrarDatePicker().show();
                break;
            case R.id.etHoraEventoContrataciones:
                mostrarTimePicker().show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().show();
        getActivity().getActionBar().setIcon(android.R.color.transparent);
        getActivity().getActionBar().setTitle(R.string.titulo_action_bar_contrataciones);
    }

    /** Mostramos un dialogo incluyendo un DatePicker */
    public Dialog mostrarDatePicker() {
        return new DatePickerDialog(getActivity(), datePickerListener, year, month, day);
    }

    /** Mostramos un dialogo incluyendo un TimePicker */
    public Dialog mostrarTimePicker() {
        return new TimePickerDialog(getActivity(),timePickerListener, hour, minute, false);
    }

    /** Evento de la clase DatePickerDialog que captura la fecha indicada por el usuario */
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            fecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year );
        }
    };

    /** Evento de la clase DatePickerDialog que captura la hora indicada por el usuario */
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hora.setText(hourOfDay + ":" + minute);
        }
    };

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                startActivity(new Intent(getActivity(), VistasInicialesActivity.class).putExtra("position", 4));
                getActivity().finish();
                return true;
            }
        }
        return false;
    }

}
