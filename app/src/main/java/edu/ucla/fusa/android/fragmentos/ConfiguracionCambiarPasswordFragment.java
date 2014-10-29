package edu.ucla.fusa.android.fragmentos;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.validadores.ValidadorPasswords;

/**
 * Created by juanlabrador on 21/10/14.
 */
public class ConfiguracionCambiarPasswordFragment extends Fragment implements TextWatcher{

    private View view;
    private EditText antiguoPassword;
    private EditText nuevoPassword;
    private EditText repitaNuevoPassword;
    private ValidadorPasswords validador;
    private TextView status;
    private View barraMuyDebil;
    private View barraDebil;
    private View barraFuerte;
    private View barraMuyFuerte;
    private Button cambiarPassword;

    public static ConfiguracionCambiarPasswordFragment newInstance() {
        ConfiguracionCambiarPasswordFragment activity = new ConfiguracionCambiarPasswordFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public ConfiguracionCambiarPasswordFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_configuraciones_cambiar_password, container, false);

        antiguoPassword = (EditText) view.findViewById(R.id.etAntiguoPassword);
        nuevoPassword = (EditText) view.findViewById(R.id.etNuevoPassword);
        repitaNuevoPassword = (EditText) view.findViewById(R.id.etRepitaNuevoPassword);
        status = (TextView) view.findViewById(R.id.tvStatusPassword);

        antiguoPassword.addTextChangedListener(this);
        nuevoPassword.addTextChangedListener(this);
        repitaNuevoPassword.addTextChangedListener(this);

        barraMuyDebil = (View) view.findViewById(R.id.barraMuyDebil);
        barraDebil = (View) view.findViewById(R.id.barraDebil);
        barraFuerte = (View) view.findViewById(R.id.barraFuerte);
        barraMuyFuerte = (View) view.findViewById(R.id.barraMuyFuerte);

        cambiarPassword = (Button) view.findViewById(R.id.btnCambiarPassword);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().setIcon(R.drawable.ic_cambiar_password_white);
        getActivity().getActionBar().setTitle(R.string.configuracion_cuenta_cambiar_password);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    /* Evento que verifica la contraseña nueva */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //boolean validar = validador.validarPassword(String.valueOf(s));
        switch (s.length()) {
            case 0:
                status.setText("");
                barraMuyDebil.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                barraDebil.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                barraFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                barraMuyFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                break;
            case 1: case 2: case 3:
            case 4:
                status.setText(R.string.nivel_dificulad_contraseña_muy_debil);
                status.setTextColor(getResources().getColor(R.color.debil));
                barraMuyDebil.setBackgroundColor(getResources().getColor(R.color.debil));
                barraDebil.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                barraFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                barraMuyFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                break;
            case 5: case 6:
                status.setText(R.string.nivel_dificulad_contraseña_debil);
                status.setTextColor(getResources().getColor(R.color.debil));
                barraMuyDebil.setBackgroundColor(getResources().getColor(R.color.debil));
                barraDebil.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                barraFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                barraMuyFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                break;
            case 7: case 8:
                status.setText(R.string.nivel_dificulad_contraseña_normal);
                status.setTextColor(getResources().getColor(R.color.normal));
                barraMuyDebil.setBackgroundColor(getResources().getColor(R.color.normal));
                barraDebil.setBackgroundColor(getResources().getColor(R.color.normal));
                barraFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                barraMuyFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                break;
            case 9: case 10:
            case 11:
                status.setText(R.string.nivel_dificulad_contraseña_fuerte);
                status.setTextColor(getResources().getColor(R.color.mejor));
                barraMuyDebil.setBackgroundColor(getResources().getColor(R.color.mejor));
                barraDebil.setBackgroundColor(getResources().getColor(R.color.mejor));
                barraFuerte.setBackgroundColor(getResources().getColor(R.color.mejor));
                barraMuyFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            break;
            default:
                status.setText(R.string.nivel_dificulad_contraseña_muy_fuerte);
                status.setTextColor(getResources().getColor(R.color.fuerte));
                barraMuyDebil.setBackgroundColor(getResources().getColor(R.color.fuerte));
                barraDebil.setBackgroundColor(getResources().getColor(R.color.fuerte));
                barraFuerte.setBackgroundColor(getResources().getColor(R.color.fuerte));
                barraMuyFuerte.setBackgroundColor(getResources().getColor(R.color.fuerte));
                break;
        }

    }

    @Override
    public void afterTextChanged(Editable s) {
        if((antiguoPassword.getText().toString().trim().equals("")
                && nuevoPassword.getText().toString().trim().equals("") && repitaNuevoPassword.getText().toString().trim().equals("")) ||
                (!antiguoPassword.getText().toString().trim().equals("")
                        && nuevoPassword.getText().toString().trim().equals("") && repitaNuevoPassword.getText().toString().trim().equals("")) ||
                (antiguoPassword.getText().toString().trim().equals("")
                        && !nuevoPassword.getText().toString().trim().equals("") && repitaNuevoPassword.getText().toString().trim().equals("")) ||
                (antiguoPassword.getText().toString().trim().equals("")
                        && nuevoPassword.getText().toString().trim().equals("") && !repitaNuevoPassword.getText().toString().trim().equals("")) ||
                (!antiguoPassword.getText().toString().trim().equals("")
                        && !nuevoPassword.getText().toString().trim().equals("") && repitaNuevoPassword.getText().toString().trim().equals("")) ||
                (!antiguoPassword.getText().toString().trim().equals("")
                        && nuevoPassword.getText().toString().trim().equals("") && !repitaNuevoPassword.getText().toString().trim().equals("")) ||
                (antiguoPassword.getText().toString().trim().equals("")
                        && !nuevoPassword.getText().toString().trim().equals("") && !repitaNuevoPassword.getText().toString().trim().equals("")) ||
                !nuevoPassword.getText().toString().trim().equals(repitaNuevoPassword.getText().toString().trim())) {
            cambiarPassword.setEnabled(false);
            cambiarPassword.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }
        else {
            cambiarPassword.setBackgroundResource(R.drawable.style_button_background_white);
            cambiarPassword.setEnabled(true);
        }
    }
}
