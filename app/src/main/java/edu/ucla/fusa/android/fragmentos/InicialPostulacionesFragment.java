package edu.ucla.fusa.android.fragmentos;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.dd.CircularProgressButton;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.FloatingHintEditText;
import edu.ucla.fusa.android.validadores.ValidadorEmails;
import java.util.Calendar;

public class InicialPostulacionesFragment extends Fragment implements View.OnClickListener {

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
    private CheckBox participaConservatorio;
    private CheckBox participaCoro;
    private CheckBox poseeBeca;
    private CheckBox poseeInstrumento;
    private View view;
    private int year;

    public static InicialPostulacionesFragment newInstance() {
        InicialPostulacionesFragment fragment = new InicialPostulacionesFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_inicial_postulaciones, paramViewGroup, false);
        barraSuperior = (LinearLayout) view.findViewById(R.id.view_barra_superior_postulaciones);
        enviar = ((CircularProgressButton) view.findViewById(R.id.btn_enviar_solicitud_aspirante));
        enviar.setOnClickListener(this);
        nombre = ((FloatingHintEditText) view.findViewById(R.id.et_nombre_aspirante));
        apellido = ((FloatingHintEditText) view.findViewById(R.id.et_apellido_aspirante));
        fechaNacimiento = ((FloatingHintEditText) view.findViewById(R.id.et_fecha_nacimiento_aspirante));
        email = ((FloatingHintEditText) view.findViewById(R.id.et_email_aspirante));
        poseeInstrumento = ((CheckBox) view.findViewById(R.id.cb_instrumento_propio_aspirante));
        participaConservatorio = ((CheckBox) view.findViewById(R.id.cb_conservatorio_aspirante));
        poseeBeca = ((CheckBox) view.findViewById(R.id.cb_beca_aspirante));
        participaCoro = ((CheckBox) view.findViewById(R.id.cb_coro_aspirante));
        fechaNacimiento.setOnClickListener(this);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        if (getActivity().getActionBar().isShowing() != false) {
            getActivity().getActionBar().setTitle(R.string.titulo_action_bar_postularse);
            getActivity().getActionBar().setIcon(R.drawable.ic_estudiantes_blanco);
            barraSuperior.setVisibility(View.GONE);
        } else {
            barraSuperior.setVisibility(View.VISIBLE);
        }
        return view;
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.btn_enviar_solicitud_aspirante:
                if (ValidadorEmails.validarEmail(this.email.getText().toString()) != true) {
                    Toast.makeText(getActivity(), R.string.mensaje_correo_invalido, Toast.LENGTH_SHORT).show();
                }
                new Thread(new Runnable() {
                    public void run() {
                        while (contador < 100) {
                            contador = (1 + contador);
                            handler.post(new Runnable() {
                                public void run() {
                                    enviar.setIndeterminateProgressMode(true);
                                    enviar.setProgress(InicialPostulacionesFragment.this.contador);
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
            case R.id.et_fecha_nacimiento_aspirante:
                new DatePickerDialog(getActivity(), datePickerListener, year, month, day).show();
                break;
        }
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);

    }

    public void onResume() {
        super.onResume();
        if (getActivity().getActionBar().isShowing() != false) {
            getActivity().getActionBar().setTitle(R.string.titulo_action_bar_postularse);
            getActivity().getActionBar().setIcon(R.drawable.ic_estudiantes_blanco);
            barraSuperior.setVisibility(View.GONE);
        } else {
            barraSuperior.setVisibility(View.VISIBLE);
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker paramAnonymousDatePicker, int year, int month, int day) {
            fechaNacimiento.setText(day + "/" + (month + 1) + "/" + year);
        }
    };
}