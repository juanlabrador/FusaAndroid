package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;

import java.util.Calendar;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.herramientas.FloatingHintEditText;
import edu.ucla.fusa.android.validadores.ValidadorEmails;

public class InicialParticipaFragment extends Fragment implements View.OnClickListener, DatePickerDialogFragment.DatePickerDialogHandler, AdapterView.OnItemSelectedListener {

    private FloatingHintEditText apellido;
    private Bundle arguments;
    private LinearLayout barraSuperior;
    private Calendar calendar;
    int contador = 0;
    private int day;
    private FloatingHintEditText email;
    private CircularProgressButton enviar;
    private FloatingHintEditText fechaNacimiento;
    private Handler handler = new Handler();
    private int month;
    private FloatingHintEditText nombre;
    private FloatingHintEditText telefono;
    private View view;
    private Spinner especialidad;
    private int year;
    private String[] instrumentos;

    public static InicialParticipaFragment newInstance() {
        InicialParticipaFragment fragment = new InicialParticipaFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_inicial_participa, paramViewGroup, false);
        barraSuperior = (LinearLayout) view.findViewById(R.id.view_barra_superior_participa);
        enviar = ((CircularProgressButton) view.findViewById(R.id.btn_enviar_solicitud_participa));
        enviar.setOnClickListener(this);
        nombre = ((FloatingHintEditText) view.findViewById(R.id.et_nombre_participa));
        apellido = ((FloatingHintEditText) view.findViewById(R.id.et_apellido_participa));
        fechaNacimiento = ((FloatingHintEditText) view.findViewById(R.id.et_fecha_nacimiento_participa));
        email = ((FloatingHintEditText) view.findViewById(R.id.et_email_participa));
        telefono = (FloatingHintEditText) view.findViewById(R.id.et_telefono_participa);
        fechaNacimiento.setOnClickListener(this);

        instrumentos = getResources().getStringArray(R.array.instrumentos);
        especialidad = (Spinner) view.findViewById(R.id.spinner_especialidad_participa);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, instrumentos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        especialidad.setAdapter(adapter);
        especialidad.setOnItemSelectedListener(this);


        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        if (getActivity().getActionBar().isShowing() != false) {
            getActivity().getActionBar().setTitle(R.string.participa_titulo_superior);
            getActivity().getActionBar().setIcon(R.drawable.ic_profesores_blanco);
            barraSuperior.setVisibility(View.GONE);
        } else {
            barraSuperior.setVisibility(View.VISIBLE);
        }
        return view;
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.btn_enviar_solicitud_participa:
                if (ValidadorEmails.validarEmail(email.getText().toString()) != true) {
                    Toast.makeText(getActivity(), R.string.mensaje_correo_invalido, Toast.LENGTH_SHORT).show();
                }
                new Thread(new Runnable() {
                    public void run() {
                        while (contador < 100) {
                            contador = (1 + contador);
                            handler.post(new Runnable() {
                                public void run() {
                                    enviar.setIndeterminateProgressMode(true);
                                    enviar.setProgress(contador);
                                }
                            });
                            try {
                                Thread.sleep(50L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
                break;
            case R.id.et_fecha_nacimiento_participa:
                new DatePickerBuilder()
                        .setFragmentManager(getChildFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                        .setTargetFragment(InicialParticipaFragment.this)
                        .show();
                //new DatePickerDialog(getActivity(), datePickerListener, year, month, day).show();
                break;
        }
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);

    }

    public void onResume() {
        super.onResume();
        if (getActivity().getActionBar().isShowing() != false) {
            getActivity().getActionBar().setTitle(R.string.participa_titulo_superior);
            getActivity().getActionBar().setIcon(R.drawable.ic_profesores_blanco);
            barraSuperior.setVisibility(View.GONE);
        } else {
            barraSuperior.setVisibility(View.VISIBLE);
        }
    }

    /*private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            fechaNacimiento.setText(day + "/" + (month + 1) + "/" + year);

        }
    };*/

    @Override
    public void onDialogDateSet(int reference, int year, int month, int day) {
        fechaNacimiento.setText(day + "/" + (month + 1) + "/" + year);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}