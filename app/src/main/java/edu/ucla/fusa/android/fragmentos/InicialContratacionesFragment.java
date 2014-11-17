package edu.ucla.fusa.android.fragmentos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.FloatingHintEditText;
import edu.ucla.fusa.android.modelo.TimePickerDialogs;
import edu.ucla.fusa.android.validadores.ValidadorCedularRif;
import edu.ucla.fusa.android.validadores.ValidadorEmails;
import java.util.Calendar;

public class InicialContratacionesFragment extends Fragment implements View.OnClickListener {

    private LinearLayout barraSuperior;
    private Calendar calendar;
    private int day;
    private FloatingHintEditText email;
    private Button enviarContratacion;
    private FloatingHintEditText fecha;
    private FloatingHintEditText hora;
    private int hour;
    private FloatingHintEditText lugar;
    private int minute;
    private int month;
    private FloatingHintEditText rifoCedula;
    private FloatingHintEditText solicitante;
    private FloatingHintEditText tema;
    private View view;
    private int year;

    public static InicialContratacionesFragment newInstance() {
        InicialContratacionesFragment fragment = new InicialContratacionesFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_inicial_contrataciones, paramViewGroup, false);
        barraSuperior = ((LinearLayout) view.findViewById(R.id.view_barra_superior_contrataciones));
        enviarContratacion = ((Button) view.findViewById(R.id.btn_enviar_solicitud_contrataciones));
        enviarContratacion.setOnClickListener(this);
        rifoCedula = ((FloatingHintEditText) view.findViewById(R.id.et_cedula_contrataciones));
        solicitante = ((FloatingHintEditText) view.findViewById(R.id.et_solicitante_contrataciones));
        email = ((FloatingHintEditText) view.findViewById(R.id.et_email_contrataciones));
        tema = ((FloatingHintEditText) view.findViewById(R.id.et_nombre_evento_contrataciones));
        lugar = ((FloatingHintEditText) view.findViewById(R.id.et_lugar_evento_contrataciones));
        fecha = ((FloatingHintEditText) view.findViewById(R.id.et_fecha_evento_contrataciones));
        hora = ((FloatingHintEditText) view.findViewById(R.id.et_hora_evento_contrataciones));
        fecha.setOnClickListener(this);
        hora.setOnClickListener(this);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);

        if (getActivity().getActionBar().isShowing() != false) {
            getActivity().getActionBar().setTitle(R.string.titulo_action_bar_contrataciones);
            getActivity().getActionBar().setIcon(R.drawable.ic_eventos_blanco);
            barraSuperior.setVisibility(View.GONE);
        } else {
            barraSuperior.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.btn_enviar_solicitud_contrataciones:
                if (ValidadorCedularRif.validarCedulaRif(rifoCedula.getText().toString()) != true) {
                    Toast.makeText(getActivity(), R.string.mensaje_cedula_rif_invalido, Toast.LENGTH_SHORT).show();
                } else if (ValidadorEmails.validarEmail(email.getText().toString()) != true) {
                    Toast.makeText(getActivity(), R.string.mensaje_correo_invalido, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.et_fecha_evento_contrataciones:
                mostrarDatePicker().show();
                break;
            case R.id.et_hora_evento_contrataciones:
                mostrarTimePicker().show();
                break;
        }
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
    }

    public void onResume() {
        super.onResume();
        if (getActivity().getActionBar().isShowing() != false) {
            getActivity().getActionBar().setTitle(R.string.titulo_action_bar_contrataciones);
            getActivity().getActionBar().setIcon(R.drawable.ic_eventos_blanco);
            barraSuperior.setVisibility(View.GONE);
        } else {
            barraSuperior.setVisibility(View.VISIBLE);
        }
    }

    public Dialog mostrarDatePicker() {
        return new DatePickerDialog(getActivity(), this.datePickerListener, this.year, this.month, this.day);
    }

    public Dialog mostrarTimePicker() {
        return new TimePickerDialogs(getActivity(), this.timePickerListener, this.hour, this.minute, true);
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker paramAnonymousTimePicker, int hour, int minute) {
            hora.setText(hour + ":" + minute);
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker paramAnonymousDatePicker, int year, int month, int day) {
            fecha.setText(day + "/" + (month + 1) + "/" + year);
        }
    };
}