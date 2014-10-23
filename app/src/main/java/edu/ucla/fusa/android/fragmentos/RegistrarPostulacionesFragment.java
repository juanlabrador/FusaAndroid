package edu.ucla.fusa.android.fragmentos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import edu.ucla.fusa.android.actividades.R;
import edu.ucla.fusa.android.validadores.ValidadorEmails;

/**
 * Created by juanlabrador on 18/10/14.
 */
public class RegistrarPostulacionesFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Button enviarPostulacion;
    private EditText nombre;
    private EditText apellido;
    private EditText fechaNacimiento;
    private EditText email;
    private CheckBox poseeInstrumento;
    private CheckBox poseeBeca;
    private CheckBox participaConservatorio;
    private CheckBox participaCoro;
    private Calendar calendar;
    private int day;
    private int month;
    private int year;
    private DialogFragment dialog;



    public static RegistrarPostulacionesFragment newInstance() {
        RegistrarPostulacionesFragment activity = new RegistrarPostulacionesFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public RegistrarPostulacionesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_registrar_postulaciones, container, false);

        enviarPostulacion = (Button) view.findViewById(R.id.btnEnviarSolicitud);
        enviarPostulacion.setOnClickListener(this);

        nombre = (EditText) view.findViewById(R.id.etNombre);
        apellido = (EditText) view.findViewById(R.id.etApellido);
        fechaNacimiento = (EditText) view.findViewById(R.id.etFechaNacimiento);
        email = (EditText) view.findViewById(R.id.etEmail);

        poseeInstrumento = (CheckBox) view.findViewById(R.id.cbInstrumentoPropio);
        poseeInstrumento = (CheckBox) view.findViewById(R.id.cbInstrumentoPropio);
        poseeInstrumento = (CheckBox) view.findViewById(R.id.cbInstrumentoPropio);
        poseeInstrumento = (CheckBox) view.findViewById(R.id.cbInstrumentoPropio);

        fechaNacimiento.setOnClickListener(this);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);


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
            case R.id.etFechaNacimiento:
                mostrarDatePicker().show();
            break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().show();
        getActivity().getActionBar().setTitle(R.string.titulo_action_bar_postularse);
    }

    public Dialog mostrarDatePicker() {
        return new DatePickerDialog(getActivity(), datePickerListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            fechaNacimiento.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year );
        }
    };

}
